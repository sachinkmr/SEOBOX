package sachin.seobox.common;

import java.util.Set;

import bithazard.sitemap.parser.SitemapParser;
import bithazard.sitemap.parser.model.Sitemap;

public class Demo {

	public static void main(String[] args) {
		SitemapParser sitemapParser = new SitemapParser();
		Set<String> sitemapLocations = sitemapParser.getSitemapLocations("https://www.google.com/");
		int sitemapLocationSize = sitemapLocations.size();
		String lastSitemapLocation = sitemapLocations.toArray(new String[sitemapLocationSize])[sitemapLocationSize - 1];
		Sitemap sitemap = sitemapParser.parseSitemap(lastSitemapLocation, true);
		System.out.println(lastSitemapLocation);
		System.out.println(sitemap.getSitemapEntries());
		System.out.println(sitemapLocationSize);
	}
}
