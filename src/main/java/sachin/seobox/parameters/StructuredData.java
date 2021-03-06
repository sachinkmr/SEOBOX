package sachin.seobox.parameters;

import sachin.seobox.helpers.DBUtils;

public class StructuredData implements Runnable {
	private final String url;
	private final String html;

	public StructuredData(String url, String html) {
		this.url = url;
		this.html = html;
	}

	public String getUrl() {
		return this.url;
	}

	public String getHtml() {
		return this.html;
	}

	@Override
	public void run() {
		DBUtils.insertStructuredDataRecord(url, html);
		try {
			int r = (int) (Math.random() * (2000 - 1500)) + 1500;
			Thread.sleep(r);
		} catch (InterruptedException e) {

		}
	}

}
