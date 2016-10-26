package com.balancedbytes.mystuff;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.core.UriBuilder;
import javax.xml.bind.annotation.XmlAttribute;

public abstract class RestDataCollection<T extends RestData> {
	
	private String href;
	private List<T> children = new ArrayList<>();

	@XmlAttribute
	public String getHref() {
		return href;
	}

	public void setHref(String href) {
		this.href = href;
	}
	
	public void add(T child) {
		if (child != null) {
			children.add(child);
		}
	}
	
	protected List<T> getChildren() {
		return children;
	}
	
	public int size() {
		return children.size();
	}
	
	public void clear() {
		children.clear();
	}
	
	public void buildHrefOnChildren(UriBuilder uriBuilder) {
		for (T data : children) {
			data.buildHref(uriBuilder);
		}
	}

}
