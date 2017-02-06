package sachin.seobox.reporter;

import java.text.SimpleDateFormat;

import org.bson.Document;
import org.slf4j.LoggerFactory;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoDatabase;
import com.relevantcodes.extentreports.DisplayOrder;
import com.relevantcodes.extentreports.ExtentReports;
import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus;
import com.relevantcodes.extentreports.NetworkMode;
import com.relevantcodes.extentreports.model.Log;
import com.relevantcodes.extentreports.view.Icon;

import sachin.seobox.crawler.CrawlerConstants;
import sachin.seobox.helpers.HelperUtils;

public class ComplexReportFactory {
    private static ComplexReportFactory factory;
    private ExtentReports reporter;
    private SimpleDateFormat df = new SimpleDateFormat("h:mm:ss a");
    private final MongoClient mongo;
    private final MongoDatabase mongoDB;

    private ComplexReportFactory() {
	mongo = new MongoClient(CrawlerConstants.DB_HOST, CrawlerConstants.DB_PORT);
	reporter = new ExtentReports(CrawlerConstants.REPORT_PATH, true, DisplayOrder.OLDEST_FIRST, NetworkMode.ONLINE);
	mongoDB = mongo.getDatabase(CrawlerConstants.DB_NAME);
    }

    public synchronized static ComplexReportFactory getInstance() {
	if (factory != null)
	    return factory;
	factory = new ComplexReportFactory();
	return factory;
    }

    public ExtentReports getExtentReport() {
	return reporter;
    }

    public ExtentTest getTest(String testName, String testDescription) {
	return reporter.startTest(testName, testDescription);
    }

    public ExtentTest getTest(String testName) {
	return getTest(testName, "");
    }

    public void closeTest(String testName) {
	if (!testName.isEmpty()) {
	    ExtentTest test = getTest(testName);
	    reporter.endTest(test);
	}
    }

    public void closeTest(ExtentTest test) {
	if (test != null) {
	    test.log(LogStatus.INFO, "END", "Test Case Completed.");
	    test.setEndedTime(HelperUtils.getTestCaseTime(System.currentTimeMillis()));
	    reporter.endTest(test);
	    Icon ic = new Icon();
	    String id = test.getTest().getId().toString();
	    for (Log log : test.getTest().getLogList()) {
		Document arr = new Document("icon", ic.getIcon(log.getLogStatus()));
		arr.append("status", log.getLogStatus().name());
		arr.append("time", df.format(log.getTimestamp()));
		arr.append("step", log.getStepName());
		arr.append("detail", log.getDetails());
		arr.append("test_id", id);
		arr.append("test_name", test.getTest().getName());
		try {
		    mongoDB.getCollection(CrawlerConstants.REPORT_TIME_STAMP).insertOne(arr);
		} catch (Exception ex) {
		    LoggerFactory.getLogger(ComplexReportFactory.class).error("Error: " + ex);
		}
	    }
	    DashBoard.getInstance().addTest(test);
	    test = null;
	}
    }

    public MongoDatabase getMongoDB() {
	return mongoDB;
    }

    public void closeReport() {
	mongo.close();
	if (reporter != null) {
	    reporter.close();
	    reporter = null;
	}
	factory = null;
    }
}