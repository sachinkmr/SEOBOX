package sachin.seobox.parameters;

import org.jsoup.nodes.Element;

public abstract class Parameter {
	protected String tagText;
	protected String tagName;
	protected Element element;
	protected boolean missing;
	protected boolean blank;
	protected boolean multiple;
	protected boolean overchar;

	public boolean hasError() {
		return missing | blank | multiple | overchar;
	}

	public boolean isMissing() {
		return this.missing;
	}

	public void setMissing(boolean missing) {
		this.missing = missing;
	}

	public boolean isBlank() {
		return this.blank;
	}

	public void setBlank(boolean blank) {
		this.blank = blank;
	}

	public boolean isMultiple() {
		return this.multiple;
	}

	public void setMultiple(boolean multiple) {
		this.multiple = multiple;
	}

	public boolean isOverchar() {
		return this.overchar;
	}

	public void setOverchar(boolean overchar) {
		this.overchar = overchar;
	}

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
