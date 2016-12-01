package sachin.seobox.common;

import java.util.UUID;

public class Demo {

	public static void main(String[] args) {
		String str = new String("");
		for (char a : UUID.randomUUID().toString().replaceAll("-", "").toCharArray()) {
			if (Character.isDigit(a)) {
				str += Character.toString(Character.toChars(97 + Character.getNumericValue(a))[0]);
			} else {
				str += Character.toString(a);
			}

		}
		System.out.println(str);
	}

}
