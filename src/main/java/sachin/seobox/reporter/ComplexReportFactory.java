package sachin.seobox.reporter;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

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

import sachin.seobox.common.SEOConfig;
import sachin.seobox.helpers.HelperUtils;
import sachin.seobox.helpers.StreamUtils;

public class ComplexReportFactory {
	private static ComplexReportFactory factory;
	private ExtentReports reporter;
	private StreamUtils stream = new StreamUtils();
	private SimpleDateFormat df = new SimpleDateFormat("h:mm:ss a");
	private File tests = new File(new File(SEOConfig.dataLocation).getParentFile(), "tests");
	private final MongoClient mongo;
	private MongoDatabase mongoDB;

	private ComplexReportFactory() {
		mongo = new MongoClient(SEOConfig.MONGODB_HOST, SEOConfig.MONGODB_PORT);
		mongoDB = mongo.getDatabase("SEOBOX");
		tests.mkdirs();
		reporter = new ExtentReports(SEOConfig.reportPath, true, DisplayOrder.OLDEST_FIRST, NetworkMode.ONLINE);
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
			Document json = new Document("name", test.getTest().getName());
			String id = test.getTest().getId().toString();
			json.append("id", id);
			List<Document> logs = new ArrayList<>();
			for (Log log : test.getTest().getLogList()) {
				Document arr = new Document("icon", ic.getIcon(log.getLogStatus()));
				arr.append("status", log.getLogStatus().name());
				arr.append("time", df.format(log.getTimestamp()));
				arr.append("step", log.getStepName());
				arr.append("detail", log.getDetails());
				logs.add(arr);
			}
			json.append("logCount", test.getTest().getLogList().size());
			json.append("logs", logs);
			try {
				getMongoDB().getCollection(SEOConfig.REPORT_TIME_STAMP).insertOne(json);
			} catch (Exception ex) {
				LoggerFactory.getLogger(ComplexReportFactory.class).error("Error: " + ex);
			}
			stream.writeTestCase(new File(tests, id + ".test"), test);
			test = null;
		}
	}

	public MongoDatabase getMongoDB() {
		return this.mongoDB;
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