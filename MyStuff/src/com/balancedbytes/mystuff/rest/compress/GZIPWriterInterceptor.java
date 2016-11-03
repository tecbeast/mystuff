package com.balancedbytes.mystuff.rest.compress;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.GZIPOutputStream;

import javax.validation.constraints.NotNull;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.Provider;
import javax.ws.rs.ext.WriterInterceptor;
import javax.ws.rs.ext.WriterInterceptorContext;

import jersey.repackaged.com.google.common.base.MoreObjects;

@Provider
@Compress
public class GZIPWriterInterceptor implements WriterInterceptor {
	
	private HttpHeaders httpHeaders;
	
	public GZIPWriterInterceptor(@Context @NotNull HttpHeaders httpHeaders) {
		this.httpHeaders = httpHeaders;
	}

	@Override
	public void aroundWriteTo(WriterInterceptorContext context) throws IOException, WebApplicationException {
		
		MultivaluedMap<String, String> requestHeaders = httpHeaders.getRequestHeaders();
		List<String> acceptEnconding = MoreObjects.firstNonNull(requestHeaders.get(HttpHeaders.ACCEPT_ENCODING), new ArrayList<String>());
		
		for (String encoding : acceptEnconding) {
			if (encoding.contains("gzip")) {
				MultivaluedMap<String, Object> responseHeaders = context.getHeaders();
				responseHeaders.add(HttpHeaders.CONTENT_ENCODING, "gzip");
				final OutputStream outputStream = context.getOutputStream();
				context.setOutputStream(new GZIPOutputStream(outputStream));
				break;
			}
		}
		
		context.proceed();
		
	}

}
