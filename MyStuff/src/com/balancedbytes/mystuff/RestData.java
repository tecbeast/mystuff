package com.balancedbytes.mystuff;

import java.net.URI;

import javax.ws.rs.core.UriBuilder;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlTransient;

@XmlAccessorType(XmlAccessType.FIELD)
public abstract class RestData {

	@XmlAttribute
	private String href;
	@XmlTransient
	private String id;
	
	public String getId() {
		return id;
	}
	
	public void setId(String id) {
		this.id = id;
	}

	public String getHref() {
		return href;
	}

	protected void setHref(String href) {
		this.href = href;
	}
	
	public void buildLink(URI uri) {
		if (uri != null) {
			setHref(UriBuilder.fromUri(uri).path(getId()).toString());
		}
	}

}
