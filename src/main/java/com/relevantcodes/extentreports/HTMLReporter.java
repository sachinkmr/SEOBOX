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
import java.util.Map;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.relevantcodes.extentreports.model.Test;

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
	private String templateName = "Extent.ftl";
	private String filePath;
	Map<String, Object> templateMap = new HashMap<>();
	private Report report;

	@Override
	public void start(Report report) {
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
				// templateMap.put("dashboard",
				// ComplexReportFactory.getInstance().getDashboard());
				templateMap.put("dashboard", DashBoard.getInstance());
				// templateMap.put("dashboard", new DashBoard());
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

	@Override
	public synchronized void addTest(Test test) {
	}

	public SystemInfo getSystemInfo() {
		return report.getSystemInfo();
	}

	public Map<String, String> getSystemInfoMap() {
		return report.getSystemInfoMap();
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

}
