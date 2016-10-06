package sachin.seobox.common;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Demo {

	public static void main(String[] args) {
		Matcher m = Pattern.compile("^(http://www.dove.com/uk)|(https://www.dove.com/uk)", Pattern.CASE_INSENSITIVE)
				.matcher("http://www.dove.com");
		System.out.println(m.find());
	}

}
