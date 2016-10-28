package com.balancedbytes.mystuff;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.core.Link;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

public abstract class RestDataCollection<T extends RestData> {
	
	private List<Link> links = new ArrayList<>();
	private List<T> elements = new ArrayList<>();

	@XmlElement(name="link")
	@XmlJavaTypeAdapter(Link.JaxbAdapter.class)
	public List<Link> getLinks() {
		return links;
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
		links.clear();
		links.add(Link.fromUri(uriCollection).rel("self").build());
		for (T element : elements) {
			element.buildLink(uriElements);
		}
	}

}
