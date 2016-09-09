package sachin.seobox.seo;

import java.io.Serializable;

import edu.uci.ics.crawler4j.url.WebURL;

public class SEOLink extends WebURL implements Serializable{
    /**
     * 
     */
    private static final long serialVersionUID = -7533833894886984558L;
    private SEOPage page;
    private final String linkAddress;

    public SEOLink(String linkAddress) {
	this.linkAddress = linkAddress;
    }

    public SEOPage getPage() {
        return this.page;
    }

    public void setPage(SEOPage page) {
        this.page = page;
    }

    public String getLinkAddress() {
        return this.linkAddress;
    }
    
    
}
