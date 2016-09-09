package sachin.seobox.parameters;

import org.jsoup.nodes.Element;

public abstract class Parameter {
	protected String tagText;
	protected String tagName;
	protected Element element;

	public Element getElement() {
		return element;
	}

	public void setElement(Element element) {
		this.element = element;
	}

	public Parameter(Element element) {
		super();
		this.element = element;
		this.tagName = element.nodeName();
		this.tagText = element.text();
	}

	public String getTagText() {
		return tagText;
	}

	public String getTagName() {
		return tagName;
	}

	public void getAttributeValue(String attributeKey) {
		element.attr(attributeKey);
	}
}
