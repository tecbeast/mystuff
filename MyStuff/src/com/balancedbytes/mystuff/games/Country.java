package com.balancedbytes.mystuff.games;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlValue;

@XmlRootElement
public class Country {
	
	private String code;  // primary key
	private String name;
	
	@XmlAttribute
	public String getCode() {
		return code;
	}
	
	public void setCode(String code) {
		this.code = code;
	}
	
	@XmlValue
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}

}
