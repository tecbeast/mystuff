package com.balancedbytes.mystuff.rest.json;

import java.io.IOException;

import javax.ws.rs.core.Link;

import com.balancedbytes.mystuff.MyStuffUtil;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

public class LinkSerializer extends JsonSerializer<Link> {

	@Override
	public void serialize(Link link, JsonGenerator jg, SerializerProvider sp)
			throws IOException, JsonProcessingException {
		jg.writeStartObject();
		/*
		jg.writeStringField("rel", link.getRel());
		jg.writeStringField("href", link.getUri().toString());
		*/
		String name = MyStuffUtil.isProvided(link.getRel()) ? link.getRel() : "href";
		String value = link.getUri().toString();
		jg.writeStringField(name, value);
		jg.writeEndObject();
	}
	
}
