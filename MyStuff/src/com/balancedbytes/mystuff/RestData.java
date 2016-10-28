package com.balancedbytes.mystuff;

import java.net.URI;

import javax.ws.rs.core.Link;
import javax.ws.rs.core.UriBuilder;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

@XmlAccessorType(XmlAccessType.FIELD)
public abstract class RestData {

	@XmlAttribute
	private String id;
	@XmlJavaTypeAdapter(Link.JaxbAdapter.class)
	private Link link;
	
	public String getId() {
		return id;
	}
	
	public void setId(String id) {
		this.id = id;
	}

	public Link getLink() {
		return link;
	}
	
	public void buildLink(URI uri) {
		if (uri != null) {
			UriBuilder uriBuilder = UriBuilder.fromUri(uri).path(getId());
			link = Link.fromUriBuilder(uriBuilder).rel("self").build();
		}
	}

}
