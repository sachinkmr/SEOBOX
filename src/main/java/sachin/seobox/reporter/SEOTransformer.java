package sachin.seobox.reporter;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

import org.testng.IAnnotationTransformer;
import org.testng.annotations.ITestAnnotation;

public class SEOTransformer implements IAnnotationTransformer {

	@SuppressWarnings("rawtypes")
	@Override
	public void transform(ITestAnnotation annotation, Class testClass, Constructor testConstructor, Method testMethod) {
		if ("pageSpeed".equals(testMethod.getName())) {
			annotation.setEnabled(true);
		} else {
			annotation.setEnabled(false);
		}
		// if (CrawlerConstants.TESTS.contains(testMethod.getName())) {
		// annotation.setEnabled(true);
		// } else {
		// annotation.setEnabled(false);
		// }
	}

}
