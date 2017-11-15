package com.balancedbytes.mystuff.games.rest;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.core.Link;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public abstract class RestDataCollection<T extends RestData> {
	
	private List<Link> links = new ArrayList<>();
	private List<T> elements = new ArrayList<>();
	
	private Integer total;	

	@XmlElement(name="link")
	@XmlJavaTypeAdapter(Link.JaxbAdapter.class)
	public List<Link> getLinks() {
		return this.links;
	}
	
	public void addLink(Link link) {
		if (link != null) {
			this.links.add(link);
		}
	}

	@XmlAttribute(name="entries")
	public int getEntries() {
		return this.elements.size();
	}
	
	@XmlAttribute(name="total")
	public Integer getTotal() {
		return this.total;
	}
	
	public void setTotal(Integer total) {
		this.total = total;
	}

	public void addElement(T element) {
		if (element != null) {
			this.elements.add(element);
		}
	}
	
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
