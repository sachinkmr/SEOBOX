package sachin.seobox.reporter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.relevantcodes.extentreports.ExtentReports;
import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.model.Log;
import com.relevantcodes.extentreports.model.TestAttribute;

public class DashBoard {
    private int totalTests;
    private int totalSteps;
    private int passedTests;
    private int failedTests;
    private int otherTests;
    private int fatalTests;
    private int warningTests;
    private int errorTests;
    private int skippedTests;
    private int unknownTests;
    private int passedSteps;
    private int failedSteps;
    private int otherSteps;
    private int fatalSteps;
    private int warningSteps;
    private int errorSteps;
    private int infoSteps;
    private int skippedSteps;
    private int unknownSteps;
    private List<DashBoardCategory> dashBoardCategories;
    private Map<String, String> systemInfoMap;

    protected Map<String, String> getSystemInfoMap() {
	return systemInfoMap;
    }

    public void setSystemInfoMap(Map<String, String> systemInfoMap) {
        this.systemInfoMap = systemInfoMap;
    }

    public DashBoard() {
	dashBoardCategories = new ArrayList<>();
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

    public DashBoard getDashBoard() {
	ExtentReports report = ComplexReportFactory.getInstance().getExtentReport();
	for (ExtentTest test : report.getTestList()) {
	    TestCase tc = new TestCase();
	    tc.setId(test.getTest().getId().toString());
	    tc.setName(test.getTest().getName());
	    tc.setStatus(test.getRunStatus().name());
	    tc.setTime(test.getStartedTime());
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
		cat.setTotal(cat.getTotal() + 1);
		dashBoardCategories.add(cat);
	    }

	    this.totalSteps += test.getTest().getLogList().size();
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
		}
	    }
	}
	this.totalTests = report.getTestList().size();	
	return this;
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
	return otherTests;
    }

    public int getPassedSteps() {
	return passedSteps;
    }

    public int getFailedSteps() {
	return failedSteps;
    }

    public int getOtherSteps() {
	return otherSteps;
    }

}
