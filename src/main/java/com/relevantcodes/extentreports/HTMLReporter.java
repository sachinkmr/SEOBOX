/*
* Copyright (c) 2015, Anshoo Arora (Relevant Codes).  All rights reserved.
* 
* Copyrights licensed under the New BSD License.
* 
* See the accompanying LICENSE file for terms.
*/

package com.relevantcodes.extentreports;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.relevantcodes.extentreports.model.Test;
import com.relevantcodes.extentreports.view.Icon;

import freemarker.ext.beans.BeansWrapper;
import freemarker.ext.beans.BeansWrapperBuilder;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.TemplateHashModel;
import freemarker.template.TemplateModelException;
import sachin.seobox.reporter.DashBoard;

/**
 * Concrete HTMLReporter class
 * 
 * @author Anshoo
 *
 */
public class HTMLReporter extends LogSettings implements IReporter {
    protected static final Logger logger = Logger.getLogger(HTMLReporter.class.getName());

    private Report report;

    private Map<String, Object> templateMap;

    private String templateName = "Extent.ftl";

    // path of the html file
    private String filePath;


    @Override
    public void start(Report report) {
	this.report = report;

	// prevent re-initialization
	if (templateMap != null) {
	    return;
	}

	ResourceBundle resourceBundle = ResourceBundle
		.getBundle("com.relevantcodes.extentreports.view.resources.localized", getDocumentLocale());

	templateMap = new HashMap<>();
	templateMap.put("report", this);
	templateMap.put("Icon", new Icon(report.getNetworkMode()));
	templateMap.put("resourceBundle", resourceBundle);

	BeansWrapperBuilder builder = new BeansWrapperBuilder(Configuration.VERSION_2_3_23);
	BeansWrapper beansWrapper = builder.build();

	try {
	    TemplateHashModel fieldTypeModel = (TemplateHashModel) beansWrapper.getEnumModels()
		    .get(LogStatus.class.getName());
	    templateMap.put("LogStatus", fieldTypeModel);
	} catch (TemplateModelException e) {
	    e.printStackTrace();
	}
    }

    @Override
    public synchronized void flush() {
	try {
	    Template template = getConfig().getTemplate(templateName);
	    BufferedWriter out = new BufferedWriter(new FileWriter(new File(filePath)));
	    try {
		DashBoard db= new DashBoard();
		db.setSystemInfoMap(report.getSystemInfoMap());
		templateMap.put("dashboard",db.getDashBoard());
		template.process(templateMap, out);
	    } catch (TemplateException e) {
		e.printStackTrace();
	    }
	    out.close();
	} catch (IOException e) {
	    logger.log(Level.SEVERE, "Template not found", e);
	}
    }

    private Configuration getConfig() {
	Configuration cfg = new Configuration(Configuration.VERSION_2_3_22);

	cfg.setClassForTemplateLoading(HTMLReporter.class, "view");
	cfg.setDefaultEncoding("UTF-8");

	return cfg;
    }

    @Override
    public void stop() {

    }

    @Override
    public void setTestRunnerLogs() {

    }

    // adds tests as HTML source
    @Override
    public synchronized void addTest(Test test) {
    }

    public Map<String, String> getConfigurationMap() {
	return report.getConfigurationMap();
    }

    // public Map<String, List<Test>> getCategoryTestMap() {
    // return report.getCategoryTestMap();
    // }

    // public Map<String, List<ExceptionInfo>> getExceptionTestMap() {
    // return report.getExceptionTestMap();
    // }

    public SystemInfo getSystemInfo() {
	return report.getSystemInfo();
    }

    public Map<String, String> getSystemInfoMap() {
	return report.getSystemInfoMap();
    }

    public List<ExtentTest> getTestList() {
	return report.getTestList();
    }

    public Date getStartedTime() {
	return new Date(report.getSuiteTimeInfo().getSuiteStartTimestamp());
    }

    public String getRunDuration() {
	return report.getRunDuration();
    }

    public String getRunDurationOverall() {
	return report.getRunDurationOverall();
    }

    public List<String> getTestRunnerLogList() {
	return report.getTestRunnerLogList();
    }

    public List<LogStatus> getLogStatusList() {
	return report.getLogStatusList();
    }

    public String getMongoDBObjectID() {
	String id = report.getMongoDBObjectID();

	if (id == null)
	    id = "";

	return id;
    }

    public UUID getReportId() {
	return report.getId();
    }

    public HTMLReporter(String filePath) {
	this.filePath = filePath;
    }

    private Locale getDocumentLocale() {
	return report.getDocumentLocale();
    }

    @Deprecated
    public class Config {
	@Deprecated
	public Config insertJs(String js) {
	    return this;
	}

	@Deprecated
	public Config insertCustomStyles(String styles) {
	    return this;
	}

	@Deprecated
	public Config addCustomStylesheet(String cssFilePath) {
	    return this;
	}

	@Deprecated
	public Config reportHeadline(String headline) {
	    return this;
	}

	@Deprecated
	public Config reportName(String name) {
	    return this;
	}

	@Deprecated
	public Config documentTitle(String title) {
	    return this;
	}

	public Config() {
	}
    }
}
