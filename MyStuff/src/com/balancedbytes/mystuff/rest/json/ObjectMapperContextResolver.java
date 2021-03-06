package com.balancedbytes.mystuff.rest.json;

import javax.ws.rs.core.Link;
import javax.ws.rs.ext.ContextResolver;
import javax.ws.rs.ext.Provider;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;

@Provider
public class ObjectMapperContextResolver implements ContextResolver<ObjectMapper> {

	private final ObjectMapper mapper;

	public ObjectMapperContextResolver() {
		mapper = new ObjectMapper();
		SimpleModule simpleModule = new SimpleModule();
		simpleModule.addSerializer(Link.class, new LinkSerializer());
		mapper.registerModule(simpleModule);
	}

	@Override
	public ObjectMapper getContext(Class<?> type) {
		return mapper;
	}
	
}
