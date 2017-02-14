package sachin.seobox.common;

import sachin.seobox.helpers.HelperUtils;

public class Demo {

	public static void main(String[] args) {
		System.out.println(HelperUtils.getUUID());
		// Set<String> list = new HashSet<>();
		// try {
		// String xml = "<?xml version=\"1.0\" encoding=\"utf-8\"?><urlset
		// xmlns:xhtml=\"http://www.w3.org/1999/xhtml\"
		// xmlns:image=\"http://www.google.com/schemas/sitemap-image/1.1\"
		// xmlns:video=\"http://www.google.com/schemas/sitemap-video/1.1\"
		// xmlns=\"http://www.sitemaps.org/schemas/sitemap/0.9\"><url><loc>http://www.liptontea.com/</loc><changefreq>monthly</changefreq><lastmod>2017-02-03</lastmod><priority>0.5</priority></url><url><loc>http://www.liptontea.com/home/about</loc><changefreq>monthly</changefreq><lastmod>2017-02-03</lastmod><priority>0.5</priority></url><url><loc>http://www.liptontea.com/recipes</loc><changefreq>monthly</changefreq><lastmod>2017-02-03</lastmod><priority>0.5</priority></url><url><loc>http://www.liptontea.com/recipes/detail/42478/1/acai-berry-smoothie</loc><image:image><image:loc>http://www.liptontea.com</image:loc></image:image><changefreq>monthly</changefreq><lastmod>2017-02-03</lastmod><priority>0.5</priority></url><url><loc>http://www.liptontea.com/recipes/detail/25195/1/apple-n-mint-tea-toddy</loc><xhtml:link
		// rel=\"alternate\" hreflang=\"en-us\"
		// href=\"http://www.liptontea.com/recipes/detail/25195/1/apple-n-mint-tea-toddy\"
		// /><xhtml:link rel=\"alternate\" hreflang=\"en-us\"
		// href=\"http://www.liptontea.com/recipes/detail/25195/2/warm-apple-green-tea-ni\"
		// /><image:image><image:loc>http://www.liptontea.com</image:loc></image:image><changefreq>monthly</changefreq><lastmod>2017-02-03</lastmod><priority>0.5</priority></url><url><loc>http://www.liptontea.com/recipes/detail/25215/1/apple-cranberry-sparkler</loc><xhtml:link
		// rel=\"alternate\" hreflang=\"en-us\"
		// href=\"http://www.liptontea.com/recipes/detail/25215/1/apple-cranberry-sparkler\"
		// /><xhtml:link rel=\"alternate\" hreflang=\"en-us\"
		// href=\"http://www.liptontea.com/recipes/detail/25215/2/apple-cranberry-sparkler\"
		// /><image:image><image:loc>http://www.liptontea.com</image:loc></image:image><changefreq>monthly</changefreq><lastmod>2017-02-03</lastmod><priority>0.5</priority></url><url><loc>http://www.liptontea.com/recipes/detail/25215/2/apple-cranberry-sparkler</loc><xhtml:link
		// rel=\"alternate\" hreflang=\"en-us\"
		// href=\"http://www.liptontea.com/recipes/detail/25215/1/apple-cranberry-sparkler\"
		// /><xhtml:link rel=\"alternate\" hreflang=\"en-us\"
		// href=\"http://www.liptontea.com/recipes/detail/25215/2/apple-cranberry-sparkler\"
		// /><image:image><image:loc>http://www.liptontea.com</image:loc></image:image><changefreq>monthly</changefreq><lastmod>2017-02-03</lastmod><priority>0.5</priority></url><url><loc>http://www.liptontea.com/recipes/detail/34923/1/apple-cucumber-refresher</loc><image:image><image:loc>http://www.liptontea.com</image:loc></image:image><changefreq>monthly</changefreq><lastmod>2017-02-03</lastmod><priority>0.5</priority></url><url><loc>http://www.liptontea.com/recipes/detail/25255/1/autumn-apple-tea</loc><image:image><image:loc>https://s3-prod-calcmenu.unileversolutions.com/public_picture/picOriginal/P121062815100129_1.jpg</image:loc></image:image><changefreq>monthly</changefreq><lastmod>2017-02-03</lastmod><priority>0.5</priority></url><url><loc>http://www.liptontea.com/recipes/detail/25257/2/autumn-tea-brew</loc><image:image><image:loc>https://s3-prod-calcmenu.unileversolutions.com/public_picture/picOriginal/P121042815100129_1.jpg</image:loc></image:image><changefreq>monthly</changefreq><lastmod>2017-02-03</lastmod><priority>0.5</priority></url><url><loc>http://www.liptontea.com/recipes/detail/44206/1/basil-mojito-iced-tea</loc><image:image><image:loc>https://s3-prod-calcmenu.unileversolutions.com/public_picture/picOriginal/Basil_Mojito_Iced_Tea_Tight.jpg</image:loc></image:image><changefreq>monthly</changefreq><lastmod>2017-02-03</lastmod><priority>0.5</priority></url><url><loc>http://www.liptontea.com/recipes/detail/40700/1/basil-mo-tea-tos</loc><image:image><image:loc>https://s3-prod-calcmenu.unileversolutions.com/public_picture/picOriginal/BasilMojito10489.jpg</image:loc></image:image><changefreq>monthly</changefreq><lastmod>2017-02-03</lastmod><priority>0.5</priority></url><url><loc>http://www.liptontea.com/recipes/detail/25363/1/beat-the-heat-tea-shake</loc><image:image><image:loc>http://www.liptontea.com</image:loc></image:image><changefreq>monthly</changefreq><lastmod>2017-02-03</lastmod><priority>0.5</priority></url><url><loc>http://www.liptontea.com/recipes/detail/43622/1/berried-green-iced-tea-with-pomegranate</loc><image:image><image:loc>https://s3-prod-calcmenu.unileversolutions.com/public_picture/picOriginal/Berried_Green_Tea_Pomegrana.jpg</image:loc></image:image><changefreq>monthly</changefreq><lastmod>2017-02-03</lastmod><priority>0.5</priority></url><url><loc>http://www.liptontea.com/recipes/detail/44217/1/berried-iced-tea-parfaits</loc><image:image><image:loc>http://www.liptontea.com</image:loc></image:image><changefreq>monthly</changefreq><lastmod>2017-02-03</lastmod><priority>0.5</priority></url><url><loc>http://www.liptontea.com/recipes/detail/35314/1/berry-lemon-quencher</loc><image:image><image:loc>http://www.liptontea.com</image:loc></image:image><changefreq>monthly</changefreq><lastmod>2017-02-03</lastmod><priority>0.5</priority></url><url><loc>http://www.liptontea.com/recipes/detail/43618/1/berry-lime-iced-tea</loc><image:image><image:loc>http://www.liptontea.com</image:loc></image:image><changefreq>monthly</changefreq><lastmod>2017-02-03</lastmod><priority>0.5</priority></url><url><loc>http://www.liptontea.com/recipes/detail/34469/1/berry-quick-citrus-iced-tea</loc><image:image><image:loc>https://s3-prod-calcmenu.unileversolutions.com/public_picture/picOriginal/12421BerryQuickCitrusTea.jpg</image:loc></image:image><changefreq>monthly</changefreq><lastmod>2017-02-03</lastmod><priority>0.5</priority></url><url><loc>http://www.liptontea.com/recipes/detail/35322/1/berry-yogurt-smoothies</loc><image:image><image:loc>http://www.liptontea.com</image:loc></image:image><changefreq>monthly</changefreq><lastmod>2017-02-03</lastmod><priority>0.5</priority></url></urlset>";
		// StringReader sr = new StringReader(xml);
		// SAXBuilder saxReader = new SAXBuilder();
		// Document doc = saxReader.build(sr);
		// Element root = doc.getRootElement();
		// Namespace ns = Namespace.getNamespace("xhtml",
		// "http://www.w3.org/1999/xhtml");
		// Namespace img = Namespace.getNamespace("image",
		// "http://www.google.com/schemas/sitemap-image/1.1");
		// for (Element url : root.getChildren("url", root.getNamespace())) {
		// list.add(url.getChildText("loc", url.getNamespace()));
		// if (!url.getChildren("link", ns).isEmpty()) {
		// for (Element link : url.getChildren("link", ns)) {
		// list.add(link.getAttributeValue("href"));
		// }
		// }
		// try {
		// String imag = url.getChild("image", img).getChildText("loc", img);
		// list.add(imag);
		// } catch (Exception ex) {
		// }
		//
		// }
		// } catch (JDOMException | IOException e) {
		// e.printStackTrace();
		// }

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
