package sachin.seobox.common;

public class Demo {

	public static void main(String[] args) {
		// System.out.println(Thread.currentThread().getStackTrace()[0].getMethodName());
		// System.out.println(Thread.currentThread().getStackTrace()[1].getMethodName());
		// System.out.println(Thread.currentThread().getStackTrace());
		// System.out.println(Demo.class.getEnclosingMethod().getName());
		System.out.println(new Object() {
		}.getClass().getEnclosingMethod().getName());
	}

}
