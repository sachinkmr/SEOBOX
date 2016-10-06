package sachin.seobox.common;

import java.io.File;

import org.apache.commons.io.FileUtils;

public class CleanUP {

	public static void main(String[] args) {
		FileUtils.deleteQuietly(new File("CrawlerConfigFile"));
	}

}
