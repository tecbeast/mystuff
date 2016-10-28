package com.balancedbytes.mystuff;

import java.util.HashSet;
import java.util.Set;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;

import org.codehaus.jackson.jaxrs.JacksonJaxbJsonProvider;

import com.balancedbytes.mystuff.games.rest.AuthorsResource;
import com.balancedbytes.mystuff.games.rest.AwardsResource;
import com.balancedbytes.mystuff.games.rest.GamesResource;
import com.balancedbytes.mystuff.games.rest.PublishersResource;

@ApplicationPath("/rest")
public class MyStuffApplication extends Application {

	@Override
	public Set<Class<?>> getClasses() {
		Set<Class<?>> set = new HashSet<>();
		set.add(JacksonJaxbJsonProvider.class);
		set.add(AuthorsResource.class);
		set.add(AwardsResource.class);
		set.add(GamesResource.class);
		set.add(PublishersResource.class);
		return set;
	}
	
	@Override
	public Set<Object> getSingletons() {
		return super.getSingletons();
	}

}
