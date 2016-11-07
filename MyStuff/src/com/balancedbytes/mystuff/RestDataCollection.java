package com.balancedbytes.mystuff;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.core.Link;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import com.fasterxml.jackson.annotation.JsonIgnore;

public abstract class RestDataCollection<T extends RestData> {
	
	private List<Link> links = new ArrayList<>();
	private List<T> elements = new ArrayList<>();

	@XmlElement(name="link")
	@XmlJavaTypeAdapter(Link.JaxbAdapter.class)
	public List<Link> getLinks() {
		return links;
	}
	
	public void addLink(Link link) {
		if (link != null) {
			links.add(link);
		}
	}
	
	public void addElement(T element) {
		if (element != null) {
			elements.add(element);
		}
	}
	
	@JsonIgnore
	@XmlTransient
	public List<T> getElements() {
		return elements;
	}
	
	public boolean hasElements() {
		return (elements.size() > 0);
	}
	
	public void clearElements() {
		elements.clear();
	}
	
	public boolean hasLinks() {
		return (links.size() > 0);
	}
	
	public void clearLinks() {
		links.clear();
	}

}
