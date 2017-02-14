package sachin.seobox.seo;

import java.io.File;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus;

import edu.uci.ics.crawler4j.url.WebURL;
import sachin.seobox.crawler.CrawlerConstants;
import sachin.seobox.exception.SEOException;
import sachin.seobox.helpers.DBUtils;
import sachin.seobox.helpers.HelperUtils;
import sachin.seobox.helpers.StreamUtils;
import sachin.seobox.parameters.SEOPage;
import sachin.seobox.reporter.ComplexReportFactory;

public class PageLevel {
	protected static final Logger logger = LoggerFactory.getLogger(PageLevel.class);
	private File[] pages;
	private StreamUtils streamUtils;

	@BeforeClass
	public void getPages() {
		pages = new File(CrawlerConstants.DATA_LOCATION).listFiles();
		streamUtils = new StreamUtils();
	}

	@AfterClass
	public void afterClass() {
		pages = null;
		streamUtils = null;
	}

	@Test(testName = "OG Tags", description = "Verify that site does have all og tags", groups = {
			"OG Tags" }, enabled = true)
	public void OGTags() {
		Method caller = new Object() {
		}.getClass().getEnclosingMethod();
		ExtentTest test = HelperUtils.getTestLogger(caller);
		for (File file : pages) {
			SEOPage page = streamUtils.readFile(file);
			try {
				if (page.getPage().getWebURL().isInternalLink() && page.getPage().getStatusCode() == 200
						&& page.getPage().getContentType().contains("text/html")) {
					List<Element> list = page.getOgTags();
					boolean flag = true;
					for (Element element : list) {
						if (element.attr("property").equals("og:title") || element.attr("property").equals("og:type")
								|| element.attr("property").equals("og:url")
								|| element.attr("property").equals("og:image")) {
							flag = false;
						}
						if (null == element.attr("content") || element.attr("content").isEmpty()) {
							test.log(LogStatus.FAIL, "OG Tag content missing.<br/>" + element.attr("property"),
									page.getPage().getWebURL().getURL());
						}
					}
					if (flag) {
						test.log(LogStatus.FAIL, "Mendatory OG TAG Missing", page.getPage().getWebURL().getURL());
					} else {
						test.log(LogStatus.PASS, "All OG Tags are found.", page.getPage().getWebURL().getURL());
					}

				}
			} catch (Exception e) {
				logger.debug("Error in " + test.getTest().getName(), e);

			}
		}
		ComplexReportFactory.getInstance().closeTest(test);
	}

	@Test(testName = "Google Page Speed", description = "Verify page based on Google PageSpeed Insights", groups = {
			"Google APIs" }, enabled = true)
	public void pageSpeed() {
		Method caller = new Object() {
		}.getClass().getEnclosingMethod();
		ExtentTest test = HelperUtils.getTestLogger(caller);
		if (null != CrawlerConstants.USERNAME && !CrawlerConstants.USERNAME.trim().isEmpty()) {
			test.log(LogStatus.SKIP, "Skipping test because site has authentication", "");
		} else {
			for (File file : pages) {
				SEOPage page = streamUtils.readFile(file);
				if (page.getPage().getWebURL().isInternalLink() && page.getPage().getStatusCode() == 200
						&& page.getPage().getContentType().contains("text/html")) {
					try {
						// String key = UUID.randomUUID().toString();
						String key = HelperUtils.getUUID();
						JSONObject results = DBUtils.getPageSpeedRecord(page.getPage().getWebURL().getURL());
						if (results.getString("hasError").equals("true"))
							throw new SEOException(results.getString("error"));
						JSONObject mobile = results.getJSONObject("mobile");
						JSONObject desktop = results.getJSONObject("desktop");
						String d = results.getString("d");
						String m = results.getString("m");
						results = null;
						org.bson.Document arr = new org.bson.Document("mobile", mobile);
						arr.append("desktop", desktop);
						arr.append("key", key);
						String id = test.getTest().getId().toString();
						arr.append("test_id", id);
						arr.append("test_name", test.getTest().getName());
						LogStatus logStatus = HelperUtils.getPageSpeedTestStatus(m, d);
						test.log(logStatus, "<b>URL: </b><br/>" + page.getPage().getWebURL().getURL(),
								"<a href='#pageSpeedModal' class='googlePageSpeed waves-effect waves-light modal-trigger' data-key='"
										+ key + "' data-test-id='" + id
										+ "' data-type='desktop'><b>Desktop: &nbsp;</b></a>"
										+ d.replaceAll("[\\{\\}\\\"]|score", "").replaceAll(",", ", ").replaceAll("::",
												": ")
										+ "<br/><a href='#pageSpeedModal' class='googlePageSpeed waves-effect waves-light modal-trigger' data-type='mobile' data-key='"
										+ key + "' data-test-id='" + id + "'><b>Mobile: &nbsp;</b></a>"
										+ m.replaceAll("[\\{\\}\\\"]|score", "").replaceAll(",", ", ").replaceAll("::",
												": "));
						ComplexReportFactory.getInstance().getMongoDB()
								.getCollection(CrawlerConstants.REPORT_TIME_STAMP).insertOne(arr);
					} catch (SEOException e) {
						logger.debug("SEOException: ", e);
						test.log(LogStatus.SKIP, "<b>URL: </b><br/>" + page.getPage().getWebURL().getURL(),
								e.getMessage());
					} catch (Exception e) {
						logger.debug("Error in " + test.getTest().getName(), e);
					}
				}
			}
		}
		ComplexReportFactory.getInstance().closeTest(test);
	}

	@Test(testName = "Schema Markup Validation", description = "Verify page schema markup based on schema.org. It uses Googles API for Structure data/schema mark-up verification", groups = {
			"Google APIs" }, enabled = true)
	public void structuredData() {
		Method caller = new Object() {
		}.getClass().getEnclosingMethod();
		ExtentTest test = HelperUtils.getTestLogger(caller);
		for (File file : pages) {
			SEOPage page = streamUtils.readFile(file);
			if (page.getPage().getWebURL().isInternalLink() && page.getPage().getStatusCode() == 200
					&& page.getPage().getContentType().contains("text/html")) {
				try {
					// String key = UUID.randomUUID().toString();
					String key = HelperUtils.getUUID();
					JSONObject results = DBUtils.getStructuredDataRecord(page.getPage().getWebURL().getURL());
					if (results.getString("hasError").equals("true"))
						throw new SEOException(results.getString("error"));
					JSONObject data = new JSONObject(results.getString("data"));
					org.bson.Document arr = new org.bson.Document("data", data.toString());
					arr.append("key", key);
					String id = test.getTest().getId().toString();
					arr.append("test_id", id);
					arr.append("test_name", test.getTest().getName());

					ComplexReportFactory.getInstance().getMongoDB().getCollection(CrawlerConstants.REPORT_TIME_STAMP)
							.insertOne(arr);
					int errors = data.getInt("totalNumErrors");
					int warnings = data.getInt("totalNumWarnings");
					String snippets = HelperUtils.getStructuredDataMicros(data.getJSONArray("tripleGroups"));
					data = null;
					LogStatus logStatus = HelperUtils.getstructuredDataStatus(errors, warnings);
					String str = (logStatus == LogStatus.FAIL || logStatus == LogStatus.WARNING)
							? "<br/><a href='#pageStructureModal' class='structureData waves-effect waves-light modal-trigger' data-key='"
									+ key + "' data-test-id='" + id + "'>View Details: &nbsp;</a>"
							: "";
					test.log(logStatus,
							"Markup(s) used on page: <br/>" + snippets + "<br/><b>URL: </b>"
									+ page.getPage().getWebURL().getURL(),
							"Errors: " + errors + "<br/>Warnings :" + warnings + str);
				} catch (SEOException e) {
					logger.debug("SEOException: ", e);
					test.log(LogStatus.SKIP, "<b>URL: </b><br/>" + page.getPage().getWebURL().getURL(), e.getMessage());
				} catch (Exception e) {
					logger.debug("Error in " + test.getTest().getName(), e);
				}
			}

		}
		ComplexReportFactory.getInstance().closeTest(test);
	}

	@Test(testName = "NOODP Tags", description = "Verify that page has NOODP Robots meta tags", groups = {
			"Robots Tags" }, enabled = true)
	public void NOODPTags() {
		Method caller = new Object() {
		}.getClass().getEnclosingMethod();
		ExtentTest test = HelperUtils.getTestLogger(caller);
		for (File file : pages) {
			SEOPage page = streamUtils.readFile(file);
			try {
				if (page.getPage().getWebURL().isInternalLink() && page.getPage().getStatusCode() == 200
						&& page.getPage().getContentType().contains("text/html")) {
					List<Element> list = page.getRobotsTags();
					for (Element e : list) {
						if (e.attr("content").toUpperCase().contains("NOODP")) {
							test.log(LogStatus.PASS, "Meta Robots Tags NOODP is found.<br/><b>URL: </b>"
									+ page.getPage().getWebURL().getURL(), "");
						} else {
							test.log(LogStatus.FAIL, "Meta Robots Tags NOODP is not found.<br/><b>URL: </b>"
									+ page.getPage().getWebURL().getURL(), "");
						}
					}
				}
			} catch (Exception e) {
				logger.debug("Error occoured ", e);

			}
		}
		ComplexReportFactory.getInstance().closeTest(test);
	}

	@Test(testName = "NOYDIR Tags", description = "Verify that page has NOYDIR Robots meta tags", groups = {
			"Robots Tags" }, enabled = true)
	public void NOYDIRTags() {
		Method caller = new Object() {
		}.getClass().getEnclosingMethod();
		ExtentTest test = HelperUtils.getTestLogger(caller);
		for (File file : pages) {
			SEOPage page = streamUtils.readFile(file);
			try {
				if (page.getPage().getWebURL().isInternalLink() && page.getPage().getStatusCode() == 200
						&& page.getPage().getContentType().contains("text/html")) {
					List<Element> list = page.getRobotsTags();
					for (Element e : list) {
						if (e.attr("content").toUpperCase().contains("NOYDIR")) {
							test.log(LogStatus.PASS, "Meta Robots Tags NOYDIR is found.<br/><b>URL: </b>"
									+ page.getPage().getWebURL().getURL(), "");
						} else {
							test.log(LogStatus.WARNING, "Meta Robots Tags NOYDIR is not found.<br/><b>URL: </b>"
									+ page.getPage().getWebURL().getURL(), "");
						}
					}
				}
			} catch (Exception e) {
				logger.debug("Error in " + test.getTest().getName(), e);

			}
		}
		ComplexReportFactory.getInstance().closeTest(test);
	}

	@Test(testName = "NO INDEX Tags", description = "Verify that page has NOINDEX Robots meta tags", groups = {
			"Robots Tags" }, enabled = true)
	public void NOINDEXTags() {
		Method caller = new Object() {
		}.getClass().getEnclosingMethod();
		ExtentTest test = HelperUtils.getTestLogger(caller);
		for (File file : pages) {
			SEOPage page = streamUtils.readFile(file);
			try {
				if (page.getPage().getWebURL().isInternalLink() && page.getPage().getStatusCode() == 200
						&& page.getPage().getContentType().contains("text/html")) {
					List<Element> list = page.getRobotsTags();
					for (Element e : list) {
						if (e.attr("content").toUpperCase().contains("NOINDEX")) {
							test.log(LogStatus.WARNING, "Meta Robots Tags NOINDEX is found.<br/><b>URL: </b>"
									+ page.getPage().getWebURL().getURL(), "");
						} else {
							test.log(LogStatus.PASS, "Meta Robots Tags NOINDEX is not found.<br/><b>URL: </b>"
									+ page.getPage().getWebURL().getURL(), "");
						}
					}
				}
			} catch (Exception e) {
				logger.debug("Error in " + test.getTest().getName(), e);

			}
		}
		ComplexReportFactory.getInstance().closeTest(test);
	}

	@Test(testName = "NO FOLLOW Tags", description = "Verify that page has NO FOLLOW Robots meta tags", groups = {
			"Robots Tags" }, enabled = true)
	public void NOFOLLOWTags() {
		Method caller = new Object() {
		}.getClass().getEnclosingMethod();
		ExtentTest test = HelperUtils.getTestLogger(caller);
		for (File file : pages) {
			SEOPage page = streamUtils.readFile(file);
			try {
				if (page.getPage().getWebURL().isInternalLink() && page.getPage().getStatusCode() == 200
						&& page.getPage().getContentType().contains("text/html")) {
					List<Element> list = page.getRobotsTags();
					for (Element e : list) {
						if (e.attr("content").toUpperCase().contains("NOFOLLOW")) {
							test.log(LogStatus.WARNING, "Meta Robots Tags NOFOLLOW is found.<br/><b>URL: </b>"
									+ page.getPage().getWebURL().getURL(), "");
						} else {
							test.log(LogStatus.FAIL, "Meta Robots Tags NOFOLLOW is found.<br/><b>URL: </b>"
									+ page.getPage().getWebURL().getURL(), "");
						}
					}
				}
			} catch (Exception e) {
				logger.debug("Error in " + test.getTest().getName(), e);

			}
		}
		ComplexReportFactory.getInstance().closeTest(test);
	}

	@Test(testName = "HREF Language Tags", description = "Verify that multilingual site does have HREF Language Tag in head tag", groups = {
			"HREF Language Tags" }, enabled = true)
	public void HREFLanguageTags() {
		Method caller = new Object() {
		}.getClass().getEnclosingMethod();
		ExtentTest test = HelperUtils.getTestLogger(caller);
		if (CrawlerConstants.MULTI_LINGUAL) {
			for (File file : pages) {
				SEOPage page = streamUtils.readFile(file);
				try {
					if (page.getPage().getWebURL().isInternalLink() && page.getPage().getStatusCode() == 200
							&& page.getPage().getContentType().contains("text/html")) {
						if (page.getPage().getStatusCode() == 200
								&& page.getPage().getContentType().contains("text/html")) {
							Document document = Jsoup.parse(page.getHtml(), CrawlerConstants.SITE);
							Element head = document.getElementsByTag("head").first();
							List<Element> links = head.select("link[rel=alternate]");
							if (links.isEmpty()) {
								test.log(LogStatus.FAIL, "HREF Language tag is missing.",
										page.getPage().getWebURL().getURL());
							} else {
								for (Element element : links) {
									if (element.hasAttr("hreflang") && !element.attr("hreflang").isEmpty()
											&& element.hasAttr("href") && !element.attr("href").isEmpty()) {
										test.log(LogStatus.PASS,
												"URL has HREF Language Tag.<br/>" + element.toString().substring(1,
														element.toString().length() - 1),
												page.getPage().getWebURL().getURL());
									} else {
										test.log(LogStatus.FAIL, "hreflang or href are missing or empty.",
												page.getPage().getWebURL().getURL());
									}

								}
							}
						}
					}
				} catch (Exception e) {
					logger.debug("Error in " + test.getTest().getName(), e);

				}
			}
		} else {
			test.log(LogStatus.PASS, "Site is not multi lengual. So this test case is not applicable", "");
		}
		ComplexReportFactory.getInstance().closeTest(test);
	}

	@Test(testName = "Multiple H1 Tags", description = "Verify that page has only one H1 Tag", groups = {
			"H1 Tag" }, enabled = true)
	public void multipleH1Tags() {
		Method caller = new Object() {
		}.getClass().getEnclosingMethod();
		ExtentTest test = HelperUtils.getTestLogger(caller);
		for (File file : pages) {
			SEOPage page = streamUtils.readFile(file);
			try {
				if (page.getPage().getWebURL().isInternalLink() && page.getPage().getStatusCode() == 200
						&& page.getPage().getContentType().contains("text/html")) {
					List<Element> list = page.getH1Tags();
					if (!list.isEmpty()) {
						if (list.size() == 1) {
							test.log(LogStatus.PASS, "Only one H1 Tags found", page.getPage().getWebURL().getURL());
						} else {
							test.log(LogStatus.FAIL, "Multiple H1 Tags on page", page.getPage().getWebURL().getURL());
						}
					}
				}
			} catch (Exception e) {
				logger.debug("Error in " + test.getTest().getName(), e);

			}
		}
		ComplexReportFactory.getInstance().closeTest(test);
	}

	@Test(testName = "Missing H1 Tags", description = "Verify that page do not has missing H1 Tag", groups = {
			"H1 Tag" }, enabled = true)
	public void missingH1Tags() {
		Method caller = new Object() {
		}.getClass().getEnclosingMethod();
		ExtentTest test = HelperUtils.getTestLogger(caller);
		for (File file : pages) {
			SEOPage page = streamUtils.readFile(file);
			try {
				if (page.getPage().getWebURL().isInternalLink() && page.getPage().getStatusCode() == 200
						&& page.getPage().getContentType().contains("text/html")) {
					List<Element> list = page.getH1Tags();
					if (!list.isEmpty()) {
						test.log(LogStatus.PASS, "Page has H1 Tag(s).", page.getPage().getWebURL().getURL());
					} else {
						test.log(LogStatus.FAIL, "H1 tags is missing from page", page.getPage().getWebURL().getURL());
					}
				}
			} catch (Exception e) {
				logger.debug("Error in " + test.getTest().getName(), e);

			}
		}
		ComplexReportFactory.getInstance().closeTest(test);
	}

	@Test(testName = "Over Character H1 Tags", description = "Verify that H1 Tag is not over character", groups = {
			"H1 Tag" }, enabled = true)
	public void overCharacterH1Tags() {
		Method caller = new Object() {
		}.getClass().getEnclosingMethod();
		ExtentTest test = HelperUtils.getTestLogger(caller);
		for (File file : pages) {
			SEOPage page = streamUtils.readFile(file);
			try {
				if (page.getPage().getWebURL().isInternalLink() && page.getPage().getStatusCode() == 200
						&& page.getPage().getContentType().contains("text/html")) {
					List<Element> list = page.getH1Tags();
					if (!list.isEmpty()) {
						for (Element e : list) {
							if (e.text().length() <= CrawlerConstants.H1_CHARACTERS_LIMIT) {
								test.log(LogStatus.PASS,
										"H1 text is not over character.<br/><b>H1: </b>" + e.text()
												+ "<br/><b>URl: </b>" + page.getPage().getWebURL().getURL(),
										"<b>H1 Character Count: </b>" + e.text().length()
												+ "<br/><b>H1 Character Limit: </b>"
												+ CrawlerConstants.H1_CHARACTERS_LIMIT);
							} else {
								test.log(LogStatus.FAIL,
										"H1 text is over character.<br/><b>H1: </b>" + e.text() + "<br/><b>URl: </b>"
												+ page.getPage().getWebURL().getURL(),
										"<b>H1 Character Count: </b>" + e.text().length()
												+ "<br/><b>H1 Character Limit: </b>"
												+ CrawlerConstants.H1_CHARACTERS_LIMIT);
							}
						}
					}
				}
			} catch (Exception e) {
				logger.debug("Error in " + test.getTest().getName(), e);

			}
		}
		ComplexReportFactory.getInstance().closeTest(test);
	}

	@Test(testName = "Blank H1 Tags", description = "Verify that H1 Tag is not blank", groups = {
			"H1 Tag" }, enabled = true)
	public void blankH1Tags() {
		Method caller = new Object() {
		}.getClass().getEnclosingMethod();
		ExtentTest test = HelperUtils.getTestLogger(caller);
		for (File file : pages) {
			SEOPage page = streamUtils.readFile(file);
			try {
				if (page.getPage().getWebURL().isInternalLink() && page.getPage().getStatusCode() == 200
						&& page.getPage().getContentType().contains("text/html")) {
					List<Element> list = page.getH1Tags();
					if (!list.isEmpty()) {
						for (Element e : list) {
							if (!e.text().isEmpty()) {
								test.log(LogStatus.PASS, "<b>H1: </b>" + e.text() + "<br/><b>URl: </b>"
										+ page.getPage().getWebURL().getURL(), "H1 text is not blank.");
							} else {
								test.log(LogStatus.FAIL, "<b>URl: </b>" + page.getPage().getWebURL().getURL(),
										"H1 text is blank.");
							}
						}
					}
				}
			} catch (Exception e) {
				logger.debug("Error in " + test.getTest().getName(), e);

			}
		}
		ComplexReportFactory.getInstance().closeTest(test);
	}

	@Test(testName = "Over Character H2 Tags", description = "Verify that H2 Tag is not over character", groups = {
			"H2 Tag" }, enabled = true)
	public void overCharacterH2Tags() {
		Method caller = new Object() {
		}.getClass().getEnclosingMethod();
		ExtentTest test = HelperUtils.getTestLogger(caller);
		for (File file : pages) {
			SEOPage page = streamUtils.readFile(file);
			try {
				if (page.getPage().getWebURL().isInternalLink() && page.getPage().getStatusCode() == 200
						&& page.getPage().getContentType().contains("text/html")) {
					List<Element> list = page.getH2Tags();
					if (!list.isEmpty()) {
						for (Element e : list) {
							if (e.text().length() <= CrawlerConstants.H2_CHARACTERS_LIMIT) {
								test.log(LogStatus.PASS,
										"H2 text is not over character.<br/><b>H2: </b>" + e.text()
												+ "<br/><b>URL: </b>" + page.getPage().getWebURL().getURL(),
										"<b>H2 Character Count: </b>" + e.text().length()
												+ "<br/><b>H2 Character Limit: </b>"
												+ CrawlerConstants.H2_CHARACTERS_LIMIT);
							} else {
								test.log(LogStatus.FAIL,
										"H2 text is over character.<br/><b>H2: </b>" + e.text() + "<br/><b>URL: </b>"
												+ page.getPage().getWebURL().getURL(),
										"<b>H2 Character Count: </b>" + e.text().length()
												+ "<br/><b>H2 Character Limit: </b>"
												+ CrawlerConstants.H2_CHARACTERS_LIMIT);
							}
						}
					}
				}
			} catch (Exception e) {
				logger.debug("Error in " + test.getTest().getName(), e);

			}
		}
		ComplexReportFactory.getInstance().closeTest(test);
	}

	@Test(testName = "Blank H2 Tags", description = "Verify that H2 Tag is not blank", groups = {
			"H2 Tag" }, enabled = true)
	public void blankH2Tags() {
		Method caller = new Object() {
		}.getClass().getEnclosingMethod();
		ExtentTest test = HelperUtils.getTestLogger(caller);
		for (File file : pages) {
			SEOPage page = streamUtils.readFile(file);
			try {
				if (page.getPage().getWebURL().isInternalLink() && page.getPage().getStatusCode() == 200
						&& page.getPage().getContentType().contains("text/html")) {
					List<Element> list = page.getH2Tags();
					if (!list.isEmpty()) {
						for (Element e : list) {
							if (!e.text().isEmpty()) {
								test.log(LogStatus.PASS, "<b>H2: </b>" + e.text() + "<br/><b>URL: </b>"
										+ page.getPage().getWebURL().getURL(), "H2 text is not blank.");
							} else {
								test.log(LogStatus.FAIL, "<b>URL: </b>" + page.getPage().getWebURL().getURL(),
										"H2 text is blank.");
							}
						}
					}
				}
			} catch (Exception e) {
				logger.debug("Error in " + test.getTest().getName(), e);

			}
		}
		ComplexReportFactory.getInstance().closeTest(test);
	}

	@Test(testName = "Image Alt Text", description = "Verify that image alt text is not missing", groups = {
			"Image" }, enabled = true)
	public void imageAltText() {
		Method caller = new Object() {
		}.getClass().getEnclosingMethod();
		ExtentTest test = HelperUtils.getTestLogger(caller);
		for (File file : pages) {
			SEOPage page = streamUtils.readFile(file);
			try {
				if (page.getPage().getWebURL().isInternalLink() && page.getPage().getStatusCode() == 200
						&& page.getPage().getContentType().contains("text/html")) {
					List<Element> list = page.getImages();
					if (!list.isEmpty()) {
						for (Element e : list) {
							if (e.hasAttr("alt") && !e.attr("alt").isEmpty()) {
								test.log(LogStatus.PASS,
										"<b>Parent URL: </b>" + page.getPage().getWebURL().getURL()
												+ "<br/><b>Image URL: </b>" + e.attr("abs:src"),
										"Image has alt Text.<br/><b>Alt Text: </b>" + e.attr("alt"));

							} else {
								test.log(LogStatus.FAIL,
										"<b>Parent URL: </b>" + page.getPage().getWebURL().getURL()
												+ "<br/><b>Image URL: </b>" + e.attr("abs:src"),
										"Image does not has alt Text.");
							}
						}
					}
				}
			} catch (Exception e) {
				logger.debug("Error in " + test.getTest().getName(), e);

			}
		}
		ComplexReportFactory.getInstance().closeTest(test);
	}

	@Test(testName = "Image Title Text", description = "Verify that image title text is not missing", groups = {
			"Image" }, enabled = true)
	public void imageTitleText() {
		Method caller = new Object() {
		}.getClass().getEnclosingMethod();
		ExtentTest test = HelperUtils.getTestLogger(caller);
		for (File file : pages) {
			SEOPage page = streamUtils.readFile(file);
			try {
				if (page.getPage().getWebURL().isInternalLink() && page.getPage().getStatusCode() == 200
						&& page.getPage().getContentType().contains("text/html")) {
					List<Element> list = page.getImages();
					if (!list.isEmpty()) {
						for (Element e : list) {
							if (e.hasAttr("title") && !e.attr("title").isEmpty()) {
								test.log(LogStatus.PASS,
										"<b>Parent URL: </b>" + page.getPage().getWebURL().getURL()
												+ "<br/><b>Image URL: </b>" + e.attr("abs:src"),
										"Image has title Text.<br/><b>Alt Text: </b>" + e.attr("title"));

							} else {
								test.log(LogStatus.FAIL,
										"<b>Parent URL: </b>" + page.getPage().getWebURL().getURL()
												+ "<br/><b>Image URL: </b>" + e.attr("abs:src"),
										"Image does not has title Text.");
							}
						}
					}
				}
			} catch (Exception e) {
				logger.debug("Error in " + test.getTest().getName(), e);
			}
		}
		ComplexReportFactory.getInstance().closeTest(test);
	}

	@Test(testName = "Image Source", description = "Verify that image Source is not missing", groups = {
			"Image" }, enabled = true)
	public void imageSRC() {
		Method caller = new Object() {
		}.getClass().getEnclosingMethod();
		ExtentTest test = HelperUtils.getTestLogger(caller);
		for (File file : pages) {
			SEOPage page = streamUtils.readFile(file);
			try {
				if (page.getPage().getWebURL().isInternalLink() && page.getPage().getStatusCode() == 200
						&& page.getPage().getContentType().contains("text/html")) {
					List<Element> list = page.getImages();
					if (!list.isEmpty()) {
						for (Element e : list) {
							if (e.hasAttr("src") && !e.attr("src").isEmpty()) {
								test.log(LogStatus.PASS,
										"<b>Parent URL: </b>" + page.getPage().getWebURL().getURL()
												+ "<br/><b>Image URL: </b>" + e.attr("abs:src"),
										"Image has source.<br/>");

							} else {
								test.log(LogStatus.FAIL, "<b>Parent URL: </b>" + page.getPage().getWebURL().getURL(),
										"Image does not has source.");
							}
						}
					}
				}
			} catch (Exception e) {
				logger.debug("Error in " + test.getTest().getName(), e);
			}
		}
		ComplexReportFactory.getInstance().closeTest(test);
	}

	@Test(testName = "Content And HTML Ratio", description = "Verify that Content and HTML Ratio does not exceed on page", groups = {
			"Content-HTML" }, enabled = true)
	public void contentAndHTMLRatio() {
		Method caller = new Object() {
		}.getClass().getEnclosingMethod();
		ExtentTest test = HelperUtils.getTestLogger(caller);
		for (File file : pages) {
			SEOPage page = streamUtils.readFile(file);
			try {
				if (page.getPage().getWebURL().isInternalLink() && page.getPage().getStatusCode() == 200
						&& page.getPage().getContentType().contains("text/html")) {
					String html = page.getHtml();
					String content = Jsoup.parse(html).text();
					byte ratio = (byte) ((content.length() * 100) / html.length());
					if (ratio <= CrawlerConstants.CONTENT_HTML_RATIO) {
						test.log(LogStatus.PASS,
								"Content and HTML Ratio is within range. <br/><b>URL: </b>"
										+ page.getPage().getWebURL().getURL(),
								"<b>Page Ratio: </b>" + ratio + "<br/><b>Recommended: </b>"
										+ CrawlerConstants.CONTENT_HTML_RATIO);
					} else {
						test.log(LogStatus.FAIL,
								"Content and HTML Ratio is not within range. <br/><b>URL: </b>"
										+ page.getPage().getWebURL().getURL(),
								"<b>Page Ratio: </b>" + ratio + "<br/><b>Recommended: </b>"
										+ CrawlerConstants.CONTENT_HTML_RATIO);
					}
				}
			} catch (Exception e) {
				logger.debug("Error in " + test.getTest().getName(), e);

			}
		}
		ComplexReportFactory.getInstance().closeTest(test);
	}

	@Test(testName = "Internal Outgoing Links", description = "Verify Internal outgoing links count on the page", groups = {
			"Links" }, enabled = true)
	public void internalOutgoingLinksCount() {
		Method caller = new Object() {
		}.getClass().getEnclosingMethod();
		ExtentTest test = HelperUtils.getTestLogger(caller);
		for (File file : pages) {
			SEOPage page = streamUtils.readFile(file);
			try {
				if (page.getPage().getWebURL().isInternalLink() && page.getPage().getStatusCode() == 200
						&& page.getPage().getContentType().contains("text/html")) {
					Set<WebURL> links = page.getPage().getParseData().getOutgoingUrls();
					int x = 0;
					for (WebURL url : links) {
						if (url.isInternalLink())
							x++;
					}
					if (x > CrawlerConstants.MAXIMUM_LINKS_COUNTS) {
						test.log(LogStatus.FAIL,
								"Internal outgoing links count is exceeding. <br/><b>URL: </b>"
										+ page.getPage().getWebURL().getURL(),
								"<b>Links Count: </b>" + x + "<br/><b>Recommended: </b>"
										+ CrawlerConstants.MAXIMUM_LINKS_COUNTS);
					} else {
						test.log(LogStatus.PASS,
								"Internal outgoing links count is withing range. <br/><b>URL: </b>"
										+ page.getPage().getWebURL().getURL(),
								"<b>Links Count: </b>" + x + "<br/><b>Recommended: </b>"
										+ CrawlerConstants.MAXIMUM_LINKS_COUNTS);
					}
				}
			} catch (Exception e) {
				logger.debug("Error in " + test.getTest().getName(), e);

			}
		}
		ComplexReportFactory.getInstance().closeTest(test);
	}

	@Test(testName = "External Outgoing Links", description = "Verify External outgoing links count on the page", groups = {
			"Links" }, enabled = true)
	public void externalOutgoingLinksCount() {
		Method caller = new Object() {
		}.getClass().getEnclosingMethod();
		ExtentTest test = HelperUtils.getTestLogger(caller);
		for (File file : pages) {
			SEOPage page = streamUtils.readFile(file);
			try {
				if (page.getPage().getWebURL().isInternalLink() && page.getPage().getStatusCode() == 200
						&& page.getPage().getContentType().contains("text/html")) {
					Set<WebURL> links = page.getPage().getParseData().getOutgoingUrls();
					int x = 0;
					for (WebURL url : links) {
						if (!url.isInternalLink())
							x++;
					}
					if (x > CrawlerConstants.MAXIMUM_EXTERNAL_LINKS_COUNTS) {
						test.log(LogStatus.FAIL,
								"External outgoing links count is exceeding. <br/><b>URL: </b>"
										+ page.getPage().getWebURL().getURL(),
								"<b>Links Count: </b>" + x + "<br/><b>Recommended: </b>"
										+ CrawlerConstants.MAXIMUM_EXTERNAL_LINKS_COUNTS);
					} else {
						test.log(LogStatus.PASS,
								"External outgoing links count is withing range. <br/><b>URL: </b>"
										+ page.getPage().getWebURL().getURL(),
								"<b>Links Count: </b>" + x + "<br/><b>Recommended: </b>"
										+ CrawlerConstants.MAXIMUM_EXTERNAL_LINKS_COUNTS);
					}
				}
			} catch (Exception e) {
				logger.debug("Error in " + test.getTest().getName(), e);

			}
		}
		ComplexReportFactory.getInstance().closeTest(test);

	}

	@Test(testName = "Missing Title", description = "Verify missing title tag on page", groups = {
			"Title Tag" }, enabled = true)
	public void missingTitle() {
		Method caller = new Object() {
		}.getClass().getEnclosingMethod();
		ExtentTest test = HelperUtils.getTestLogger(caller);
		for (File file : pages) {
			SEOPage page = streamUtils.readFile(file);
			try {
				if (page.getPage().getWebURL().isInternalLink() && page.getPage().getStatusCode() == 200
						&& page.getPage().getContentType().contains("text/html")) {
					List<Element> links = page.getTitle();
					if (links.isEmpty()) {
						test.log(LogStatus.FAIL,
								"Title tag is missing.<br/><b>URL: </b>" + page.getPage().getWebURL().getURL(), "");
					} else {
						test.log(LogStatus.PASS,
								"Title tag found.<br/><b>URL: </b>" + page.getPage().getWebURL().getURL(), "");

					}
				}
			} catch (Exception e) {
				logger.debug("Error in " + test.getTest().getName(), e);

			}
		}
		ComplexReportFactory.getInstance().closeTest(test);
	}

	@Test(testName = "Multiple Title", description = "Verify multiple title tag on page", groups = {
			"Title Tag" }, enabled = true)
	public void multipleTitle() {
		Method caller = new Object() {
		}.getClass().getEnclosingMethod();
		ExtentTest test = HelperUtils.getTestLogger(caller);
		for (File file : pages) {
			SEOPage page = streamUtils.readFile(file);
			try {
				if (page.getPage().getWebURL().isInternalLink() && page.getPage().getStatusCode() == 200
						&& page.getPage().getContentType().contains("text/html")) {
					List<Element> links = page.getTitle();
					if (links.size() > 1) {
						test.log(LogStatus.FAIL,
								"Multiple title tags found.<br/><b>URL: </b>" + page.getPage().getWebURL().getURL(),
								"");
					} else {
						test.log(LogStatus.PASS, "There are no multiple title tags.<br/><b>URL: </b>"
								+ page.getPage().getWebURL().getURL(), "");

					}
				}
			} catch (Exception e) {
				logger.debug("Error in " + test.getTest().getName(), e);

			}
		}
		ComplexReportFactory.getInstance().closeTest(test);
	}

	@Test(testName = "Title Minimum Length", description = "Verify minimum length for title tag", groups = {
			"Title Tag" }, enabled = true)
	public void titleMinimumLength() {
		Method caller = new Object() {
		}.getClass().getEnclosingMethod();
		ExtentTest test = HelperUtils.getTestLogger(caller);
		for (File file : pages) {
			SEOPage page = streamUtils.readFile(file);
			try {
				if (page.getPage().getWebURL().isInternalLink() && page.getPage().getStatusCode() == 200
						&& page.getPage().getContentType().contains("text/html")) {
					List<Element> links = page.getTitle();
					for (Element e : links) {
						if (e.text().length() < CrawlerConstants.TITLE_CHARACTERS_LIMIT_MIN) {
							test.log(LogStatus.FAIL,
									"Title Length is less than required length.<br/><b>Title: </b>" + e.text()
											+ " <br/><b>URL: </b>" + page.getPage().getWebURL().getURL(),
									"<b>Title Length: </b>" + e.text().length() + "<br/><b>Recommended: </b>"
											+ CrawlerConstants.TITLE_CHARACTERS_LIMIT_MIN);
						} else {
							test.log(LogStatus.PASS,
									"Title Length is as expected.<br/><b>Title: </b>" + e.text() + " <br/><b>URL: </b>"
											+ page.getPage().getWebURL().getURL(),
									"<b>Title Length: </b>" + e.text().length() + "<br/><b>Recommended: </b>"
											+ CrawlerConstants.TITLE_CHARACTERS_LIMIT_MIN);
						}
					}
				}
			} catch (Exception e) {
				logger.debug("Error in " + test.getTest().getName(), e);

			}
		}
		ComplexReportFactory.getInstance().closeTest(test);
	}

	@Test(testName = "Title Maximum Length", description = "Verify maximum length for title tag", groups = {
			"Title Tag" }, enabled = true)
	public void titleMaximumLength() {
		Method caller = new Object() {
		}.getClass().getEnclosingMethod();
		ExtentTest test = HelperUtils.getTestLogger(caller);
		for (File file : pages) {
			SEOPage page = streamUtils.readFile(file);
			try {
				if (page.getPage().getWebURL().isInternalLink() && page.getPage().getStatusCode() == 200
						&& page.getPage().getContentType().contains("text/html")) {
					List<Element> links = page.getTitle();
					for (Element e : links) {
						if (e.text().length() > CrawlerConstants.TITLE_CHARACTERS_LIMIT) {
							test.log(LogStatus.FAIL,
									"Title Length is greater than required length.<br/><b>Title: </b>" + e.text()
											+ " <br/><b>URL: </b>" + page.getPage().getWebURL().getURL(),
									"<b>Title Length: </b>" + e.text().length() + "<br/><b>Recommended: </b>"
											+ CrawlerConstants.TITLE_CHARACTERS_LIMIT);
						} else {
							test.log(LogStatus.PASS,
									"Title Length is as expected.<br/><b>Title: </b>" + e.text() + " <br/><b>URL: </b>"
											+ page.getPage().getWebURL().getURL(),
									"<b>Title Length: </b>" + e.text().length() + "<br/><b>Recommended: </b>"
											+ CrawlerConstants.TITLE_CHARACTERS_LIMIT);
						}
					}
				}
			} catch (Exception e) {
				logger.debug("Error in " + test.getTest().getName(), e);

			}
		}
		ComplexReportFactory.getInstance().closeTest(test);
	}

	@Test(testName = "Meta Description Length", description = "Verify length for description tag", groups = {
			"Meta Description" }, enabled = true)
	public void descriptionLength() {
		Method caller = new Object() {
		}.getClass().getEnclosingMethod();
		ExtentTest test = HelperUtils.getTestLogger(caller);
		for (File file : pages) {
			SEOPage page = streamUtils.readFile(file);
			try {
				if (page.getPage().getWebURL().isInternalLink() && page.getPage().getStatusCode() == 200
						&& page.getPage().getContentType().contains("text/html")) {
					List<Element> links = page.getMetaDescription();
					for (Element e : links) {
						if (e.attr("content").length() > CrawlerConstants.META_DESCRIPTION_CHARACTERS_LIMIT) {
							test.log(LogStatus.FAIL,
									"Meta Description Length is greater than required length.<br/><b>Description: </b>"
											+ e.attr("content") + " <br/><b>URL: </b>"
											+ page.getPage().getWebURL().getURL(),
									"<b>Length: </b>" + e.attr("content").length() + "<br/><b>Recommended: </b>"
											+ CrawlerConstants.META_DESCRIPTION_CHARACTERS_LIMIT);
						} else {
							test.log(LogStatus.PASS,
									"Meta Description is as expected.<br/><b>Description: </b>" + e.attr("content")
											+ " <br/><b>URL: </b>" + page.getPage().getWebURL().getURL(),
									"<b>Length: </b>" + e.attr("content").length() + "<br/><b>Recommended: </b>"
											+ CrawlerConstants.META_DESCRIPTION_CHARACTERS_LIMIT);
						}
					}
				}
			} catch (Exception e) {
				logger.debug("Error in " + test.getTest().getName(), e);

			}
		}
		ComplexReportFactory.getInstance().closeTest(test);
	}

	@Test(testName = "Blank Meta Description", description = "Verify that description tag content is not blank", groups = {
			"Meta Description" }, enabled = true)
	public void blankDescription() {
		Method caller = new Object() {
		}.getClass().getEnclosingMethod();
		ExtentTest test = HelperUtils.getTestLogger(caller);
		for (File file : pages) {
			SEOPage page = streamUtils.readFile(file);
			try {
				if (page.getPage().getWebURL().isInternalLink() && page.getPage().getStatusCode() == 200
						&& page.getPage().getContentType().contains("text/html")) {
					List<Element> links = page.getMetaDescription();
					for (Element e : links) {
						if (e.attr("content").isEmpty()) {
							test.log(LogStatus.FAIL, "<b>URL: </b>" + page.getPage().getWebURL().getURL(),
									"Meta Description content is blank.");
						} else {
							test.log(LogStatus.PASS, "<b>Description: </b>" + e.attr("content") + " <br/><b>URL: </b>"
									+ page.getPage().getWebURL().getURL(), "Meta Description is not blank.");
						}
					}
				}
			} catch (Exception e) {
				logger.debug("Error in " + test.getTest().getName(), e);

			}
		}
		ComplexReportFactory.getInstance().closeTest(test);
	}

	@Test(testName = "Missing Meta Description", description = "Verify for missing description for page", groups = {
			"Meta Description" }, enabled = true)
	public void missingDescription() {
		Method caller = new Object() {
		}.getClass().getEnclosingMethod();
		ExtentTest test = HelperUtils.getTestLogger(caller);
		for (File file : pages) {
			SEOPage page = streamUtils.readFile(file);
			try {
				if (page.getPage().getWebURL().isInternalLink() && page.getPage().getStatusCode() == 200
						&& page.getPage().getContentType().contains("text/html")) {
					List<Element> links = page.getMetaDescription();
					if (links.isEmpty()) {
						test.log(LogStatus.FAIL,
								"Meta Description is missing <br/><b>URL: </b>" + page.getPage().getWebURL().getURL(),
								"");
					} else {
						test.log(LogStatus.PASS,
								"Meta Description found<br/><b>URL: </b>" + page.getPage().getWebURL().getURL(), "");
					}
				}
			} catch (Exception e) {
				logger.debug("Error in " + test.getTest().getName(), e);

			}
		}
		ComplexReportFactory.getInstance().closeTest(test);
	}

	@Test(testName = "Multiple Meta Description", description = "Verify for multiple description for page", groups = {
			"Meta Description" }, enabled = true)
	public void multipleDescription() {
		Method caller = new Object() {
		}.getClass().getEnclosingMethod();
		ExtentTest test = HelperUtils.getTestLogger(caller);
		for (File file : pages) {
			SEOPage page = streamUtils.readFile(file);
			try {
				if (page.getPage().getWebURL().isInternalLink() && page.getPage().getStatusCode() == 200
						&& page.getPage().getContentType().contains("text/html")) {
					List<Element> links = page.getMetaDescription();
					if (links.size() > 1) {
						test.log(LogStatus.FAIL,
								"Meta Description are multiple <br/><b>URL: </b>" + page.getPage().getWebURL().getURL(),
								"");
					} else {
						test.log(LogStatus.PASS, "There are no multiple description tags.<br/><b>URL: </b>"
								+ page.getPage().getWebURL().getURL(), "");
					}
				}
			} catch (Exception e) {
				logger.debug("Error in " + test.getTest().getName(), e);

			}
		}
		ComplexReportFactory.getInstance().closeTest(test);
	}

	@Test(testName = "Canonical URL Length", description = "Verify length for canonical tag", groups = {
			"Canonical Tag" }, enabled = true)
	public void canonicalLength() {
		Method caller = new Object() {
		}.getClass().getEnclosingMethod();
		ExtentTest test = HelperUtils.getTestLogger(caller);
		for (File file : pages) {
			SEOPage page = streamUtils.readFile(file);
			try {
				if (page.getPage().getWebURL().isInternalLink() && page.getPage().getStatusCode() == 200
						&& page.getPage().getContentType().contains("text/html")) {
					List<Element> links = page.getCanonical();
					for (Element e : links) {
						if (e.attr("href").length() > CrawlerConstants.CANONICAL_URL_CHARACTERS_LIMIT) {
							test.log(LogStatus.FAIL,
									"Canonical URL Length is greater than required length.<br/><b>Canonical URL: </b>"
											+ e.attr("href") + " <br/><b>Page URL: </b>"
											+ page.getPage().getWebURL().getURL(),
									"<b>Length: </b>" + e.attr("href").length() + "<br/><b>Recommended: </b>"
											+ CrawlerConstants.CANONICAL_URL_CHARACTERS_LIMIT);
						} else {
							test.log(LogStatus.PASS,
									"Canonical URL Length is as expected.<br/><b>Canonical URL: </b>" + e.attr("href")
											+ " <br/><b>Page URL: </b>" + page.getPage().getWebURL().getURL(),
									"<b>Length: </b>" + e.attr("href").length() + "<br/><b>Recommended: </b>"
											+ CrawlerConstants.CANONICAL_URL_CHARACTERS_LIMIT);
						}
					}
				}
			} catch (Exception e) {
				logger.debug("Error in " + test.getTest().getName(), e);

			}
		}
		ComplexReportFactory.getInstance().closeTest(test);
	}

	@Test(testName = "Blank Canonical URL", description = "Verify for blank canonical tag url", groups = {
			"Canonical Tag" }, enabled = true)
	public void blankCanonicalURL() {

		Method caller = new Object() {
		}.getClass().getEnclosingMethod();
		ExtentTest test = HelperUtils.getTestLogger(caller);
		for (File file : pages) {
			SEOPage page = streamUtils.readFile(file);
			try {
				if (page.getPage().getWebURL().isInternalLink() && page.getPage().getStatusCode() == 200
						&& page.getPage().getContentType().contains("text/html")) {
					List<Element> links = page.getCanonical();
					for (Element e : links) {
						if (e.attr("href").isEmpty()) {
							test.log(LogStatus.FAIL,
									"Canonical URL is empty. <br/><b>Page URL: </b>"
											+ page.getPage().getWebURL().getURL(),
									"<b>Length: </b>" + e.attr("href").length() + "<br/><b>Recommended: </b>"
											+ CrawlerConstants.CANONICAL_URL_CHARACTERS_LIMIT);
						} else {
							test.log(LogStatus.PASS,
									"Canonical URL is not empty.<br/><b>Canonical URL: </b>" + e.attr("href")
											+ " <br/><b>Page URL: </b>" + page.getPage().getWebURL().getURL(),
									"<b>Length: </b>" + e.attr("href").length() + "<br/><b>Recommended: </b>"
											+ CrawlerConstants.CANONICAL_URL_CHARACTERS_LIMIT);
						}
					}
				}
			} catch (Exception e) {
				logger.debug("Error in " + test.getTest().getName(), e);

			}
		}
		ComplexReportFactory.getInstance().closeTest(test);
	}

	@Test(testName = "Missing Canonical URL", description = "Verify for missing canonical tags for page", groups = {
			"Canonical Tag" }, enabled = true)
	public void missingCanonicalTags() {
		Method caller = new Object() {
		}.getClass().getEnclosingMethod();
		ExtentTest test = HelperUtils.getTestLogger(caller);
		for (File file : pages) {
			SEOPage page = streamUtils.readFile(file);
			try {
				if (page.getPage().getWebURL().isInternalLink() && page.getPage().getStatusCode() == 200
						&& page.getPage().getContentType().contains("text/html")) {
					List<Element> links = page.getCanonical();
					if (links.isEmpty()) {
						test.log(LogStatus.FAIL, "<b>URL: </b>" + page.getPage().getWebURL().getURL(),
								"Canonical tag is missing");
					} else {
						test.log(LogStatus.PASS, "<b>URL: </b>" + page.getPage().getWebURL().getURL(),
								"Canonical tag found.");
					}
				}
			} catch (Exception e) {
				logger.debug("Error in " + test.getTest().getName(), e);

			}
		}
		ComplexReportFactory.getInstance().closeTest(test);
	}

	@Test(testName = "Multiple Canonical Tags", description = "Verify for multiple canonical tags for page", groups = {
			"Canonical Tag" }, enabled = true)
	public void multipleCanonicalTags() {
		Method caller = new Object() {
		}.getClass().getEnclosingMethod();
		ExtentTest test = HelperUtils.getTestLogger(caller);
		for (File file : pages) {
			SEOPage page = streamUtils.readFile(file);
			try {
				if (page.getPage().getWebURL().isInternalLink() && page.getPage().getStatusCode() == 200
						&& page.getPage().getContentType().contains("text/html")) {
					List<Element> links = page.getCanonical();
					if (links.size() > 1) {
						test.log(LogStatus.FAIL,
								"Canonical tags are multiple <br/><b>URL: </b>" + page.getPage().getWebURL().getURL(),
								"");
					} else {
						test.log(LogStatus.PASS, "There are no multiple canonical tags.<br/><b>URL: </b>"
								+ page.getPage().getWebURL().getURL(), "");
					}
				}
			} catch (Exception e) {
				logger.debug("Error in " + test.getTest().getName(), e);

			}
		}
		ComplexReportFactory.getInstance().closeTest(test);
	}

	@Test(testName = "Div instead of Table", description = "Verify that if page has tables", groups = {
			"Div instead of Table" }, enabled = true)
	public void useDivInsteadOfTable() {
		Method caller = new Object() {
		}.getClass().getEnclosingMethod();
		ExtentTest test = HelperUtils.getTestLogger(caller);
		for (File file : pages) {
			SEOPage page = streamUtils.readFile(file);
			try {
				if (page.getPage().getWebURL().isInternalLink() && page.getPage().getStatusCode() == 200
						&& page.getPage().getContentType().contains("text/html")) {
					List<Element> links = Jsoup.parse(page.getHtml()).select("table");
					if (links.size() > 0) {
						test.log(LogStatus.FAIL,
								"page has tables <br/><b>URL: </b>" + page.getPage().getWebURL().getURL(), "");
					} else {
						test.log(LogStatus.PASS,
								"There are no tables in page.<br/><b>URL: </b>" + page.getPage().getWebURL().getURL(),
								"");
					}
				}
			} catch (Exception e) {
				logger.debug("Error in " + test.getTest().getName(), e);
			}
		}
		ComplexReportFactory.getInstance().closeTest(test);
	}

	@Test(testName = "No Frames", description = "Verify that if page has frames", groups = {
			"No Frames" }, enabled = true)
	public void noFrames() {
		Method caller = new Object() {
		}.getClass().getEnclosingMethod();
		ExtentTest test = HelperUtils.getTestLogger(caller);
		for (File file : pages) {
			SEOPage page = streamUtils.readFile(file);
			try {
				if (page.getPage().getWebURL().isInternalLink() && page.getPage().getStatusCode() == 200
						&& page.getPage().getContentType().contains("text/html")) {
					List<Element> links = Jsoup.parse(page.getHtml()).select("frame");
					if (links.size() > 0) {
						test.log(LogStatus.FAIL,
								"page has frames <br/><b>URL: </b>" + page.getPage().getWebURL().getURL(), "");
					} else {
						test.log(LogStatus.PASS,
								"There are no frames in page.<br/><b>URL: </b>" + page.getPage().getWebURL().getURL(),
								"");
					}
				}
			} catch (Exception e) {
				logger.debug("Error in " + test.getTest().getName(), e);

			}
		}
		ComplexReportFactory.getInstance().closeTest(test);
	}

	@Test(testName = "No Flash", description = "Verify that if page has flash content", groups = {
			"No Flash" }, enabled = true)
	public void noFlash() {
		Method caller = new Object() {
		}.getClass().getEnclosingMethod();
		ExtentTest test = HelperUtils.getTestLogger(caller);
		for (File file : pages) {
			SEOPage page = streamUtils.readFile(file);
			try {
				if (page.getPage().getWebURL().isInternalLink() && page.getPage().getStatusCode() == 200
						&& page.getPage().getContentType().contains("text/html")) {
					List<Element> links = Jsoup.parse(page.getHtml()).select("*[data]");
					if (links.size() > 0) {
						boolean flag = false;
						for (Element e : links) {
							if (e.attr("data").toLowerCase().contains(".swf")) {
								test.log(LogStatus.FAIL, "<b>URL: </b>" + page.getPage().getWebURL().getURL(),
										"Page has flash content ");
								flag = true;
							}
						}
						if (!flag) {
							test.log(LogStatus.PASS, "<b>URL: </b>" + page.getPage().getWebURL().getURL(),
									"There is no flash content in page.");
						}
					} else {
						test.log(LogStatus.PASS, "<b>URL: </b>" + page.getPage().getWebURL().getURL(),
								"There is no flash content in page.");
					}
				}
			} catch (Exception e) {
				logger.debug("Error in " + test.getTest().getName(), e);

			}
		}
		ComplexReportFactory.getInstance().closeTest(test);
	}

	@Test(testName = "Page Depth", description = "Depth of the page is calculated from the domain of site. This means minimum click required to reach on the page from home page of the site", groups = {
			"URL Structure" }, enabled = true)
	public void pageDepth() {
		Method caller = new Object() {
		}.getClass().getEnclosingMethod();
		ExtentTest test = HelperUtils.getTestLogger(caller);
		for (File file : pages) {
			SEOPage page = streamUtils.readFile(file);
			try {
				if (page.getPage().getWebURL().isInternalLink() && page.getPage().getStatusCode() == 200
						&& page.getPage().getContentType().contains("text/html")) {
					int depth = page.getPage().getWebURL().getDepth();
					if (depth == 4) {
						test.log(LogStatus.WARNING, "<b>URL: </b>" + page.getPage().getWebURL().getURL(),
								"Page Depth: " + depth);
					} else if (depth > 4) {
						test.log(LogStatus.FAIL, "<b>URL: </b>" + page.getPage().getWebURL().getURL(),
								"Page Depth: " + depth);
					} else {
						test.log(LogStatus.PASS, "<b>URL: </b>" + page.getPage().getWebURL().getURL(),
								"Page Depth: " + depth);
					}
				}
			} catch (Exception e) {
				logger.debug("Error in " + test.getTest().getName(), e);

			}
		}
		ComplexReportFactory.getInstance().closeTest(test);
	}

	@Test(testName = "Duplicate Meta Description", description = "Verify that site does not have duplicate meta description values", groups = {
			"Meta Description" }, enabled = true)
	public void duplicateDescription() {
		Method caller = new Object() {
		}.getClass().getEnclosingMethod();
		ExtentTest test = HelperUtils.getTestLogger(caller);
		try {
			Map<String, String> map = new HashMap<>();
			boolean flag = true;
			for (File file : pages) {
				SEOPage page = streamUtils.readFile(file);
				try {
					if (page.getPage().getWebURL().isInternalLink() && page.getPage().getStatusCode() == 200
							&& page.getPage().getContentType().contains("text/html")) {
						String key = page.getMetaDescription().get(0).attr("content");
						String value = page.getPage().getWebURL().getURL();
						if (map.containsKey(key)) {
							map.put(key, map.get(key) + "<br/>" + value);
							flag = false;
						} else {
							map.put(key, value);
						}
					}
				} catch (Exception e) {
					logger.debug("Error in " + test.getTest().getName(), e);

				}
			}
			for (String key : map.keySet()) {
				if (map.get(key).split("<br/>").length > 2) {
					test.log(LogStatus.FAIL, map.get(key), "<b>Description: </b>" + key);
				}
			}
			if (flag) {
				test.log(LogStatus.PASS, "No duplicate descriptions found,");
			}
		} catch (Exception e) {
			logger.debug("Error in " + test.getTest().getName(), e);
			test.log(LogStatus.FAIL, "Test Step Failed");
		}
		ComplexReportFactory.getInstance().closeTest(test);
	}

	@Test(testName = "Duplicate Titles", description = "Verify that site does not have duplicate title values", groups = {
			"Title Tag" }, enabled = true)
	public void duplicateTitle() {
		Method caller = new Object() {
		}.getClass().getEnclosingMethod();
		ExtentTest test = HelperUtils.getTestLogger(caller);
		try {
			Map<String, String> map = new HashMap<>();
			boolean flag = true;
			for (File file : pages) {
				SEOPage page = streamUtils.readFile(file);
				try {
					if (page.getPage().getWebURL().isInternalLink() && page.getPage().getStatusCode() == 200
							&& page.getPage().getContentType().contains("text/html")) {
						String key = page.getTitle().get(0).text();
						String value = page.getPage().getWebURL().getURL();
						if (map.containsKey(key)) {
							map.put(key, map.get(key) + "<br/>" + value);
							flag = false;
						} else {
							map.put(key, value);
						}
					}
				} catch (Exception e) {
					logger.debug("Error in " + test.getTest().getName(), e);

				}
			}
			for (String key : map.keySet()) {
				if (map.get(key).split("<br/>").length > 2) {
					test.log(LogStatus.FAIL, map.get(key), "<b>Title: </b>" + key);
				}
			}

			if (flag) {
				test.log(LogStatus.PASS, "No duplicate titles found,");
			}
		} catch (Exception e) {
			logger.debug("Error in " + test.getTest().getName(), e);
			test.log(LogStatus.FAIL, "Test Step Failed", e);
		}
		ComplexReportFactory.getInstance().closeTest(test);
	}

	@Test(testName = "Duplicate Body Content", description = "Verify that pages do not have duplicate content", groups = {
			"Content-HTML" }, enabled = true)
	public void duplicateBodyContent() {
		Method caller = new Object() {
		}.getClass().getEnclosingMethod();
		ExtentTest test = HelperUtils.getTestLogger(caller);
		try {
			Map<String, String> map = new HashMap<>();
			boolean flag = true;
			for (File file : pages) {
				SEOPage page = streamUtils.readFile(file);
				try {
					if (page.getPage().getWebURL().isInternalLink() && page.getPage().getStatusCode() == 200
							&& page.getPage().getContentType().contains("text/html")) {
						String key = Integer.toString(
								Jsoup.parse(page.getHtml(), CrawlerConstants.SITE).select("body").text().hashCode());
						String value = page.getPage().getWebURL().getURL();
						if (map.containsKey(key)) {
							map.put(key, map.get(key) + "<br/>" + value);
							flag = false;
						} else {
							map.put(key, value);
						}
					}
				} catch (Exception e) {
					logger.debug("Error in " + test.getTest().getName(), e);
				}
			}
			for (String key : map.keySet()) {
				if (map.get(key).split("<br/>").length > 2) {
					test.log(LogStatus.FAIL, "<b>Duplicate Body Content: </b>", map.get(key));
				}
			}

			if (flag) {
				test.log(LogStatus.PASS, "No duplicate body content found,");
			}
		} catch (Exception e) {
			logger.debug("Error in " + test.getTest().getName(), e);
			test.log(LogStatus.FAIL, "Test Step Failed", e);
		}
		ComplexReportFactory.getInstance().closeTest(test);
	}

	@Test(testName = "Duplicate H1 Tags", description = "Verify that site does not have duplicate h1 tag content", groups = {
			"H1 Tag" }, enabled = true)
	public void duplicateH1Tag() {
		Method caller = new Object() {
		}.getClass().getEnclosingMethod();
		ExtentTest test = HelperUtils.getTestLogger(caller);
		try {
			Map<String, String> map = new HashMap<>();
			boolean flag = true;
			for (File file : pages) {
				SEOPage page = streamUtils.readFile(file);
				try {
					if (page.getPage().getWebURL().isInternalLink() && page.getPage().getStatusCode() == 200
							&& page.getPage().getContentType().contains("text/html")) {
						String key = page.getH1Tags().get(0).text();
						String value = page.getPage().getWebURL().getURL();
						if (map.containsKey(key)) {
							map.put(key, map.get(key) + "<br/>" + value);
							flag = false;
						} else {
							map.put(key, value);
						}
					}
				} catch (Exception e) {
					logger.debug("Error in " + test.getTest().getName(), e);

				}
			}
			for (String key : map.keySet()) {
				if (map.get(key).split("<br/>").length > 2) {
					test.log(LogStatus.FAIL, map.get(key), "<b>H1 Tag: </b>" + key);
				}
			}

			if (flag) {
				test.log(LogStatus.PASS, "No duplicate H1 Tag found,");
			}
		} catch (Exception e) {
			logger.debug("Error in " + test.getTest().getName(), e);
			test.log(LogStatus.FAIL, "Test Step Failed", e);
		}
		ComplexReportFactory.getInstance().closeTest(test);
	}

	@Test(testName = "Anchor Link Text", description = "Verify that anchor link should have text in it.", groups = {
			"Anchor Link" }, enabled = true)
	public void anchorLinkText() {
		Method caller = new Object() {
		}.getClass().getEnclosingMethod();
		ExtentTest test = HelperUtils.getTestLogger(caller);
		for (File file : pages) {
			SEOPage page = streamUtils.readFile(file);
			try {
				if (page.getPage().getWebURL().isInternalLink() && page.getPage().getStatusCode() == 200
						&& page.getPage().getContentType().contains("text/html")) {
					List<Element> links = Jsoup.parse(page.getHtml()).getElementsByTag("a");
					if (links.size() > 0) {
						for (Element e : links) {
							if (!e.text().isEmpty()) {
								test.log(LogStatus.PASS, "<b>Anchor: </b>" + e.attr("href") + "<br/><b>URL: </b>"
										+ page.getPage().getWebURL().getURL(), "Anchor has text.");
							} else if (e.select("img").size() <= 0 && e.html().isEmpty()) {
								test.log(LogStatus.FAIL, "<b>Anchor: </b>" + e.attr("href") + "<br/><b>URL: </b>"
										+ page.getPage().getWebURL().getURL(), "Anchor does not have text");
							}
						}
					} else {
						test.log(LogStatus.PASS, "<b>URL: </b>" + page.getPage().getWebURL().getURL(),
								"There is anchor tag on page.");
					}
				}
			} catch (Exception e) {
				logger.debug("Error in " + test.getTest().getName(), e);

			}
		}
		ComplexReportFactory.getInstance().closeTest(test);
	}

	@Test(testName = "Anchor External Link", description = "Verify that external anchor link should have no-follow", groups = {
			"Anchor Link" }, enabled = true)
	public void anchorExtenalLink() {
		Method caller = new Object() {
		}.getClass().getEnclosingMethod();
		ExtentTest test = HelperUtils.getTestLogger(caller);
		for (File file : pages) {
			SEOPage page = streamUtils.readFile(file);
			try {
				if (page.getPage().getWebURL().isInternalLink() && page.getPage().getStatusCode() == 200
						&& page.getPage().getContentType().contains("text/html")) {
					Set<String> urls = new HashSet<>();
					for (WebURL url : page.getPage().getParseData().getOutgoingUrls()) {
						if (!url.isInternalLink())
							urls.add(url.getURL());
					}
					List<Element> links = Jsoup.parse(page.getHtml()).getElementsByTag("a");
					for (Element e : links) {
						if (urls.contains(e.absUrl("href"))) {
							if (e.attr("rel").toLowerCase().contains("nofollow")) {
								test.log(LogStatus.PASS,
										"<b>Anchor: </b>" + e.attr("href") + "<br/><b>URL: </b>"
												+ page.getPage().getWebURL().getURL(),
										"External Anchor Links have nofollow attribute value.");
							} else if (e.select("img").size() <= 0 && e.html().isEmpty()) {
								test.log(LogStatus.FAIL,
										"<b>Anchor: </b>" + e.attr("href") + "<br/><b>URL: </b>"
												+ page.getPage().getWebURL().getURL(),
										"External Anchor Link does not have nofollow attribute value.");
							}
						}
					}
				}
			} catch (Exception e) {
				logger.debug("Error in " + test.getTest().getName(), e);
			}
		}
		ComplexReportFactory.getInstance().closeTest(test);
	}
}
