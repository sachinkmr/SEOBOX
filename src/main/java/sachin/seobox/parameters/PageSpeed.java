package sachin.seobox.parameters;

import sachin.seobox.helpers.DBUtils;

public class PageSpeed implements Runnable {
	private final String url;

	public PageSpeed(String url) {
		this.url = url;
	}

	public String getUrl() {
		return this.url;
	}

	@Override
	public void run() {
		DBUtils.insertPageSpeedRecord(url);
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {

		}
	}

}
