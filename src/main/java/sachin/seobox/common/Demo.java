package sachin.seobox.common;

import sachin.seobox.helpers.HelperUtils;

public class Demo {

	public static void main(String[] args) {
		for (int i = 0; i < 9999; i++)
			System.out.println(HelperUtils.getUUID());

		// SitemapParser sitemapParser = new SitemapParser();
		// Set<String> sitemapLocations =
		// sitemapParser.getSitemapLocations("https://www.google.com/");
		// int sitemapLocationSize = sitemapLocations.size();
		// String lastSitemapLocation = sitemapLocations.toArray(new
		// String[sitemapLocationSize])[sitemapLocationSize - 1];
		// Sitemap sitemap = sitemapParser.parseSitemap(lastSitemapLocation,
		// true);
		// System.out.println(lastSitemapLocation);
		// System.out.println(sitemap.getSitemapEntries());
		// System.out.println(sitemapLocationSize);
	}
}
