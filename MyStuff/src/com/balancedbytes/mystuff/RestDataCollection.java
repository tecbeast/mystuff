package com.balancedbytes.mystuff;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.core.Link;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

public abstract class RestDataCollection<T extends RestData> {
	
	private List<Link> links = new ArrayList<>();
	private List<T> elements = new ArrayList<>();
	
	private Integer page;
	private Integer pagesTotal;
	private Integer entriesTotal;	

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

	public Integer getPage() {
		return page;
	}
	
	public void setPage(Integer page) {
		this.page = page;
	}
	
	public Integer getPagesTotal() {
		return pagesTotal;
	}
	
	public void setPagesTotal(Integer pagesTotal) {
		this.pagesTotal = pagesTotal;
	}

	@XmlElement(name="entries")
	public int getEntries() {
		return elements.size();
	}
	
	public Integer getEntriesTotal() {
		return entriesTotal;
	}
	
	public void setEntriesTotal(Integer entriesTotal) {
		this.entriesTotal = entriesTotal;
	}

	public void addElement(T element) {
		if (element != null) {
			elements.add(element);
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
