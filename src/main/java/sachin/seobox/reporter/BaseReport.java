package sachin.seobox.reporter;

import java.lang.reflect.Method;

import org.testng.annotations.AfterMethod;

import com.relevantcodes.extentreports.ExtentTest;

public class BaseReport {
    @AfterMethod
    public void closeTestCase(Method caller) {
	ExtentTest test = ComplexReportFactory.getInstance().getTest(caller.getName());
	ComplexReportFactory.getInstance().closeTest(test);
    }
}
