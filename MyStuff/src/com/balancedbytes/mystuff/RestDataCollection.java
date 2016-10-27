package com.balancedbytes.mystuff;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAttribute;

public abstract class RestDataCollection<T extends RestData> {
	
	private String href;
	private List<T> elements = new ArrayList<>();

	@XmlAttribute
	public String getHref() {
		return href;
	}

	protected void setHref(String href) {
		this.href = href;
	}
	
	public void add(T element) {
		if (element != null) {
			elements.add(element);
		}
	}
	
	protected List<T> getElements() {
		return elements;
	}
	
	public int size() {
		return elements.size();
	}
	
	public void clear() {
		elements.clear();
	}
	
	public void buildLinks(URI uriCollection, URI uriElements) {
		setHref(uriCollection.toString());
		for (T element : elements) {
			element.buildLink(uriElements);
		}
	}

}
