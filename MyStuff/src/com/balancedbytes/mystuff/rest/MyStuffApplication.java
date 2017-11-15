package com.balancedbytes.mystuff.rest;

import java.util.HashSet;
import java.util.Set;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;

import com.balancedbytes.mystuff.games.rest.api.AuthorsResource;
import com.balancedbytes.mystuff.games.rest.api.AwardsResource;
import com.balancedbytes.mystuff.games.rest.api.GamesResource;
import com.balancedbytes.mystuff.games.rest.api.ImagesResource;
import com.balancedbytes.mystuff.games.rest.api.NotesResource;
import com.balancedbytes.mystuff.games.rest.api.PublishersResource;
import com.balancedbytes.mystuff.rest.compress.GZIPWriterInterceptor;
import com.balancedbytes.mystuff.rest.json.ObjectMapperContextResolver;
import com.fasterxml.jackson.jaxrs.json.JacksonJaxbJsonProvider;

@ApplicationPath("/rest")
public class MyStuffApplication extends Application {

	@Override
	public Set<Class<?>> getClasses() {
		Set<Class<?>> set = new HashSet<>();
		// JSON providers
		set.add(JacksonJaxbJsonProvider.class);
		set.add(ObjectMapperContextResolver.class);
		// Compression interceptor
		set.add(GZIPWriterInterceptor.class);
		// REST resources
		set.add(AuthorsResource.class);
		set.add(AwardsResource.class);
		set.add(GamesResource.class);
		set.add(PublishersResource.class);
		set.add(ImagesResource.class);
		set.add(NotesResource.class);
		return set;
	}

}
