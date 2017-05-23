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
				mobile = NetUtils.getPageSpeedData(url, "mobile");
			} while ((mobile == null || mobile.has("error")) && ++i < 3);
			i = 0;
			do {
				desktop = NetUtils.getPageSpeedData(url, "desktop");
			} while ((desktop == null || desktop.has("error")) && ++i < 3);
			if (desktop != null && !desktop.has("error")) {
				arr.append("desktop", desktop.toString());
			} else {
				throw new SEOException("Unable to fatch page speed desktop data");
			}
			if (mobile != null && !mobile.has("error")) {
				arr.append("mobile", mobile.toString());
			} else {
				throw new SEOException("Unable to fatch page speed mobile data");
			}

		} catch (SEOException e) {
			logger.debug("Unable to fatch page speed data", e);
			arr.append("hasError", "true");
			arr.append("error", e.getMessage());
		}
		ComplexReportFactory.getInstance().getMongoDB().getCollection(CrawlerConstants.REPORT_TIME_STAMP).insertOne(arr);
	}

	public static JSONObject getPageSpeedRecord(String url) {
		Document doc = ComplexReportFactory.getInstance().getMongoDB().getCollection(CrawlerConstants.REPORT_TIME_STAMP).findOneAndDelete(Filters.and(Filters.exists("page_speed_results"), Filters.eq("url", url)));
		// Document doc =
		// ComplexReportFactory.getInstance().getMongoDB().getCollection(CrawlerConstants.REPORT_TIME_STAMP)
		// .find(Filters.and(Filters.exists("page_speed_results"),
		// Filters.eq("url", url))).first();
		JSONObject json = new JSONObject();
		json.put("hasError", doc.getString("hasError"));
		json.put("error", doc.getString("error"));
		json.put("d", new JSONObject(doc.getString("desktop")).getJSONObject("ruleGroups").toString());
		json.put("m", new JSONObject(doc.getString("mobile")).getJSONObject("ruleGroups").toString());
		json.put("mobile", APIUtils.parsePageSpeedResponse(doc.getString("mobile")));
		json.put("desktop", APIUtils.parsePageSpeedResponse(doc.getString("desktop")));
		return json;
	}

	public static void insertStructuredDataRecord(String url, String html) {
		JSONObject json = new JSONObject();
		Document arr = new Document("hasError", "false");
		arr.append("url", url);
		arr.append("structured_data_results", "true");
		try {
			int i = 0;
			do {
				json = NetUtils.getStructuredData(html, url);
			} while ((json == null) && ++i < 3);
		} catch (SEOException e) {
			logger.debug("Unable to fatch google structured data", e);
			arr.append("hasError", "true");
			arr.append("error", e.getMessage());
		}
		if (json != null) {
			arr.append("data", json.toString());
		}
		ComplexReportFactory.getInstance().getMongoDB().getCollection(CrawlerConstants.REPORT_TIME_STAMP).insertOne(arr);
	}

	public static JSONObject getStructuredDataRecord(String url) {
		Document doc = ComplexReportFactory.getInstance().getMongoDB().getCollection(CrawlerConstants.REPORT_TIME_STAMP).findOneAndDelete(Filters.and(Filters.exists("structured_data_results"), Filters.eq("url", url)));
		// Document doc =
		// ComplexReportFactory.getInstance().getMongoDB().getCollection(CrawlerConstants.REPORT_TIME_STAMP)
		// .find(Filters.and(Filters.exists("structured_data_results"),
		// Filters.eq("url", url))).first();

		JSONObject json = new JSONObject();
		json.put("hasError", doc.getString("hasError"));
		json.put("error", doc.getString("error"));
		json.put("data", APIUtils.parseStructuredDataResponse(doc.getString("data")));
		return json;
	}
}
