package sachin.seobox.helpers;

import org.bson.Document;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mongodb.client.model.Filters;

import sachin.seobox.crawler.CrawlerConstants;
import sachin.seobox.exception.SEOException;
import sachin.seobox.reporter.ComplexReportFactory;

public class DBUtils {
    protected static final Logger logger = LoggerFactory.getLogger(DBUtils.class);

    public static void insertPageSpeedRecord(String url) {
	JSONObject desktop = null;
	JSONObject mobile = null;
	Document arr = new Document("hasError", "false");
	arr.append("url", url);
	arr.append("page_speed_results", "true");
	try {
	    int i = 0;
	    do {
		desktop = HttpRequestUtils.getPageSpeedData(url, "desktop");
		mobile = HttpRequestUtils.getPageSpeedData(url, "mobile");
	    } while ((desktop == null || mobile == null) && ++i < 3);
	    if (desktop != null) {
		arr.append("desktop", desktop.toString());
	    }
	    if (mobile != null) {
		arr.append("mobile", mobile.toString());
	    }
	} catch (SEOException e) {
	    logger.debug("Unable to fatch page speed data", e);
	    arr.append("hasError", "true");
	    arr.append("error", e.toString());
	}
	ComplexReportFactory.getInstance().getMongoDB().getCollection(CrawlerConstants.REPORT_TIME_STAMP).insertOne(arr);
    }

    public static JSONObject getPageSpeedRecord(String url) {
	Document doc = ComplexReportFactory.getInstance().getMongoDB().getCollection(CrawlerConstants.REPORT_TIME_STAMP)
		.findOneAndDelete(Filters.and(Filters.exists("page_speed_results"), Filters.eq("url", url)));
	return new JSONObject(doc.toJson());
    }

    public static void insertStructuredDataRecord(String url, String html) {
	JSONObject json = new JSONObject();
	Document arr = new Document("hasError", "false");
	arr.append("url", url);
	arr.append("structured_data_results", "true");
	try {
	    int i = 0;
	    do {
		json = HttpRequestUtils.getStructuredData(html, url);
	    } while ((json == null) && ++i < 3);
	} catch (SEOException e) {
	    logger.debug("Unable to fatch google structured data", e);
	    arr.append("hasError", "true");
	    arr.append("error", e.toString());
	}
	if (json != null) {
	    arr.append("data", json.toString());
	}
	ComplexReportFactory.getInstance().getMongoDB().getCollection(CrawlerConstants.REPORT_TIME_STAMP).insertOne(arr);
    }

    public static JSONObject getStructuredDataRecord(String url) {
	Document doc = ComplexReportFactory.getInstance().getMongoDB().getCollection(CrawlerConstants.REPORT_TIME_STAMP)
		.findOneAndDelete(Filters.and(Filters.exists("structured_data_results"), Filters.eq("url", url)));
	return new JSONObject(doc.toJson());
    }
}
