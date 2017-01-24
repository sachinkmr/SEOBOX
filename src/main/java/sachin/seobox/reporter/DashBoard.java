package sachin.seobox.reporter;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.relevantcodes.extentreports.ExtentReports;
import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus;
import com.relevantcodes.extentreports.model.Log;
import com.relevantcodes.extentreports.model.TestAttribute;

import sachin.seobox.common.SEOConfig;

public class DashBoard {
	private static DashBoard dashBoard;
	private int totalTests;
	private int totalSteps;
	private int passedTests;
	private int failedTests;
	private int fatalTests;
	private int warningTests;
	private int errorTests;
	private int skippedTests;
	private int unknownTests;
	private int passedSteps;
	private int failedSteps;
	private int fatalSteps;
	private int warningSteps;
	private int errorSteps;
	private int infoSteps;
	private int skippedSteps;
	private int unknownSteps;
	private List<DashBoardCategory> dashBoardCategories;
	private List<LogStatus> logStatusList;
	private String currentSuiteRunDuration;
	private String runDurationOverall;
	private Date startedTime;

	public synchronized static DashBoard getInstance() {
		if (null == dashBoard) {
			dashBoard = new DashBoard();
		}
		return dashBoard;
	}

	public String getIp() {
		try {
			return InetAddress.getLocalHost().getHostAddress();
		} catch (UnknownHostException e) {

		}
		return "10.207.16.9";
	}

	public Date getStartedTime() {
		return this.startedTime;
	}

	public String getRunDuration() {
		return this.currentSuiteRunDuration;
	}

	public String getRunDurationOverall() {
		return this.runDurationOverall;
	}

	public List<LogStatus> getLogStatusList() {
		return this.logStatusList;
	}

	public Map<String, String> getSystemInfo() {
		Map<String, String> map = new LinkedHashMap<>();
		map.put("Web Site", SEOConfig.site);
		try {
			if (System.getProperty("machine") != null && !System.getProperty("machine").isEmpty()) {
				map.put("Requester IP", System.getProperty("machine"));
			} else {
				map.put("Machine", InetAddress.getLocalHost().getHostName());
			}
		} catch (UnknownHostException e) {
		}
		return map;
	}

	public List<DashBoardCategory> getDashBoardCategories() {
		return dashBoardCategories;
	}

	public int getTotalTests() {
		return totalTests;
	}

	public int getTotalSteps() {
		return totalSteps;
	}

	void addTest(ExtentTest test) {
		TestCase tc = new TestCase();
		tc.setId(test.getTest().getId().toString());
		tc.setName(test.getTest().getName());
		tc.setStatus(test.getRunStatus());
		tc.setTime(test.getStartedTime());
		tc.setEndedTime(test.getEndedTime());
		tc.setRunDuration(test.getTest().getRunDuration());
		tc.setDesc(test.getDescription());
		for (TestAttribute attr : test.getTest().getCategoryList()) {
			DashBoardCategory cat = new DashBoardCategory(attr.getName());
			if (dashBoardCategories.contains(cat)) {
				cat = dashBoardCategories.remove(dashBoardCategories.indexOf(cat));
			}
			if (test.getRunStatus().name().equalsIgnoreCase("pass")) {
				cat.setPassed(cat.getPassed() + 1);
			} else if (test.getRunStatus().name().equalsIgnoreCase("fail")
					|| test.getRunStatus().name().equalsIgnoreCase("fatal")
					|| test.getRunStatus().name().equalsIgnoreCase("error")) {
				cat.setFailed(cat.getFailed() + 1);
			}
			tc.setCats(attr.getName());
			cat.setTotal(cat.getTotal() + 1);
			cat.getTestCases().add(tc);
			dashBoardCategories.add(cat);
		}
		this.totalSteps += test.getTest().getLogList().size();
		this.totalTests++;
		if (test.getRunStatus().name().equalsIgnoreCase("fail")) {
			failedTests++;
		} else if (test.getRunStatus().name().equalsIgnoreCase("pass")) {
			passedTests++;
		} else if (test.getRunStatus().name().equalsIgnoreCase("fatal")) {
			fatalTests++;
		} else if (test.getRunStatus().name().equalsIgnoreCase("error")) {
			errorTests++;
		} else if (test.getRunStatus().name().equalsIgnoreCase("skip")) {
			skippedTests++;
		} else if (test.getRunStatus().name().equalsIgnoreCase("warning")) {
			warningTests++;
		} else if (test.getRunStatus().name().equalsIgnoreCase("unknown")) {
			unknownTests++;
		}
		for (Log log : test.getTest().getLogList()) {
			if (log.getLogStatus().name().equalsIgnoreCase("fail")) {
				failedSteps++;
			} else if (log.getLogStatus().name().equalsIgnoreCase("pass")) {
				passedSteps++;
			} else if (log.getLogStatus().name().equalsIgnoreCase("fatal")) {
				fatalSteps++;
			} else if (log.getLogStatus().name().equalsIgnoreCase("error")) {
				errorSteps++;
			} else if (log.getLogStatus().name().equalsIgnoreCase("skip")) {
				skippedSteps++;
			} else if (log.getLogStatus().name().equalsIgnoreCase("warning")) {
				warningSteps++;
			} else if (log.getLogStatus().name().equalsIgnoreCase("unknown")) {
				unknownSteps++;
			} else if (log.getLogStatus().name().equalsIgnoreCase("info")) {
				infoSteps++;
			}
		}

		LogStatus status = test.getRunStatus();
		if (status == LogStatus.FATAL || status == LogStatus.ERROR || status == LogStatus.WARNING
				|| status == LogStatus.UNKNOWN) {
			if (!logStatusList.contains(status)) {
				logStatusList.add(status);
			}
		}
	}

	private DashBoard() {
		dashBoardCategories = new ArrayList<>();
		logStatusList = new ArrayList<>();
		ExtentReports report = ComplexReportFactory.getInstance().getExtentReport();
		currentSuiteRunDuration = report.getRunDuration();
		runDurationOverall = report.getRunDurationOverall();
		startedTime = report.getStartedTime();
	}

	public String getReportName() {
		return SEOConfig.REPORT_TIME_STAMP;
	}

	public Set<TestCase> getTestCases() {
		Set<TestCase> list = new HashSet<>();
		for (DashBoardCategory cat : dashBoardCategories) {
			list.addAll(cat.getTestCases());
		}
		return list;
	}

	public int getFatalTests() {
		return fatalTests;
	}

	public int getWarningTests() {
		return warningTests;
	}

	public int getErrorTests() {
		return errorTests;
	}

	public int getSkippedTests() {
		return skippedTests;
	}

	public int getUnknownTests() {
		return unknownTests;
	}

	public int getFatalSteps() {
		return fatalSteps;
	}

	public int getWarningSteps() {
		return warningSteps;
	}

	public int getErrorSteps() {
		return errorSteps;
	}

	public int getInfoSteps() {
		return infoSteps;
	}

	public int getSkippedSteps() {
		return skippedSteps;
	}

	public int getUnknownSteps() {
		return unknownSteps;
	}

	public int getPassedTests() {
		return passedTests;
	}

	public int getFailedTests() {
		return failedTests;
	}

	public int getOtherTests() {
		return skippedTests + warningTests + unknownTests;
	}

	public int getPassedSteps() {
		return passedSteps;
	}

	public int getFailedSteps() {
		return failedSteps;
	}

	public int getOtherSteps() {
		return skippedSteps + warningSteps + unknownSteps + infoSteps;
	}

}
