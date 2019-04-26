package com.balancedbytes.tool.kodi;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.NTCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.config.AuthSchemes;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.EntityBuilder;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.ContentType;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import com.eclipsesource.json.Json;
import com.eclipsesource.json.JsonArray;
import com.eclipsesource.json.JsonObject;
import com.eclipsesource.json.JsonValue;

public class TheTvDbApiClient implements AutoCloseable {
	
	private static Log LOG = LogFactory.getLog(TheTvDbApiClient.class);
	
	private Properties fProperties;
	private String fPropertyPath;
	private CloseableHttpClient fClient;
	private RequestConfig fConfig;
	private HttpHost fTarget;
	private HttpHost fProxy;
	private String fToken;
	
	public TheTvDbApiClient(String propertyPath) {
		fPropertyPath = propertyPath;
		fProperties = new Properties(); 
		fClient = HttpClients.createDefault();
	}

	private void init() throws IOException {
		
		try (InputStream in = TheTvDbApiClient.class.getClass().getResourceAsStream(fPropertyPath)) {
			fProperties.load(in);
		}
		
		String proxyUser = fProperties.getProperty("proxy.user");
		String proxyPassword = fProperties.getProperty("proxy.password");
		String proxyDomain = fProperties.getProperty("proxy.domain");
		
		if (KodiTools.isProvided(proxyUser, proxyPassword, proxyDomain)) {
			CredentialsProvider credsProvider = new BasicCredentialsProvider();
			credsProvider.setCredentials(
				AuthScope.ANY,
				new NTCredentials(
					proxyUser.trim(),
					proxyPassword.trim(),
					System.getenv("COMPUTERNAME"),
					proxyDomain.trim()
				)
			);
			fClient = HttpClients.custom().setDefaultCredentialsProvider(credsProvider).build();
		}
		
		String proxyHost = fProperties.getProperty("proxy.host");
		int proxyPort = Integer.parseInt(fProperties.getProperty("proxy.port", "80"));
		String proxyScheme = fProperties.getProperty("proxy.scheme", "http");
		
		if (KodiTools.isProvided(proxyHost)) {
			fProxy = new HttpHost(proxyHost.trim(), proxyPort, proxyScheme.trim());
		}

		if (fProxy != null) {
			fConfig = RequestConfig.custom()
				.setProxyPreferredAuthSchemes(Arrays.asList(AuthSchemes.NTLM, AuthSchemes.BASIC))
				.setProxy(fProxy)
				.build();
		}

		String targetHost = fProperties.getProperty("thetvdb.host", "api.thetvdb.com");
		int targetPort = Integer.parseInt(fProperties.getProperty("thetvdb.port", "443"));
		String targetScheme = fProperties.getProperty("thetvdb.scheme", "https");
		
		fTarget = new HttpHost(targetHost.trim(), targetPort, targetScheme.trim());

	}
	
	@Override
	public void close() throws IOException {
		if (fClient != null) {
			fClient.close();
		}
	}
	
	public boolean login() throws IOException {
		
		init();
		
		JsonObject jsonRequest = new JsonObject();
        jsonRequest.add("apikey", fProperties.getProperty("thetvdb.apikey", ""));
        jsonRequest.add("userkey", fProperties.getProperty("thetvdb.userkey", ""));
        jsonRequest.add("username", fProperties.getProperty("thetvdb.username", ""));

	    HttpPost httpPost = new HttpPost("/login");
	    httpPost.addHeader("Content-Type", "application/json");
	    httpPost.setEntity(EntityBuilder.create().setText(jsonRequest.toString()).build());
        
        JsonObject jsonResponse = executeRequest(httpPost);
        if (jsonResponse != null) {
        	fToken = jsonResponse.getString("token", null);
        }
        
        return (fToken != null);
		
	}
	
	public JsonObject searchSeries(String name) throws IOException {
		try {
			URIBuilder uriBuilder = new URIBuilder().setPath("/search/series").setParameter("name", name);
			return executeRequest(new HttpGet(uriBuilder.build()));
		} catch (URISyntaxException use) {
			return null;
		}
	}

	public JsonObject queryEpisodes(int seriesId, int airedSeason) throws IOException {
		try {
			URIBuilder uriBuilder = new URIBuilder().setPath("/series/" + seriesId + "/episodes/query");
			if (airedSeason > 0) {
				uriBuilder.setParameter("airedSeason", Integer.toString(airedSeason));
			}
			return executeRequest(new HttpGet(uriBuilder.build()));
		} catch (URISyntaxException use) {
			return null;
		}
	}

	private JsonObject executeRequest(HttpRequestBase request) throws IOException {
	    request.addHeader("Accept", "application/json");
	    request.addHeader("Accept-Language", "de");
	    if (fToken != null) {
	    	request.addHeader("Authorization", "Bearer " + fToken);
	    }
	    if (fConfig != null) {
	    	request.setConfig(fConfig);
	    }
	    if (LOG.isDebugEnabled()) {
	    	StringBuilder msg = new StringBuilder();
	    	msg.append("Executing request ").append(request.getRequestLine());
	    	msg.append(" to ").append(fTarget);
	    	if (fProxy != null) {
	    		msg.append(" via ").append(fProxy);
	    	}
	    	LOG.debug(msg.toString());
	    }
	    ResponseHandler<JsonObject> responseHandler = new ResponseHandler<JsonObject>() {
		    @Override
		    public JsonObject handleResponse(HttpResponse response) throws IOException {
		    	if (response.getStatusLine().getStatusCode() >= 300) {
		    		StringBuilder msg = new StringBuilder();
		    		msg.append("Status: ").append(response.getStatusLine().getStatusCode());
		    		msg.append(" ").append(response.getStatusLine().getReasonPhrase());
		    		throw new IOException(msg.toString());
		    	}
		    	if (response.getEntity() == null) {
		    		throw new IOException("Http Response contained no entity.");
		        }
	        	ContentType contentType = ContentType.getOrDefault(response.getEntity());
	        	Reader reader = new InputStreamReader(response.getEntity().getContent(), contentType.getCharset());
	        	return Json.parse(reader).asObject();
		    }
		};
		return fClient.execute(fTarget, request, responseHandler);
	}
	
	public static void main(String[] args) {
		try (TheTvDbApiClient client = new TheTvDbApiClient(args[0])) {
			if (client.login()) {
				int seriesId = 0;
				JsonObject jsonObject = client.searchSeries("Misfits");
				if (jsonObject != null) {
					JsonArray jsonData = jsonObject.get("data").asArray();
					if (jsonData.size() > 0) {
						seriesId = jsonData.get(0).asObject().get("id").asInt();
					}
				}
				if (seriesId > 0) {
					jsonObject = client.queryEpisodes(seriesId, 1);
					if (jsonObject != null) {
						JsonArray jsonData = jsonObject.get("data").asArray();
						for (int i = 0; i < jsonData.size(); i++) {
							JsonObject episode = jsonData.get(i).asObject();
							JsonValue airedSeason = episode.get("airedSeason");
							JsonValue airedEpisodeNumber = episode.get("airedEpisodeNumber");
							JsonValue episodeName = episode.get("episodeName");
							if (KodiTools.isNotNull(airedSeason, airedEpisodeNumber, episodeName)) {
								StringBuilder line = new StringBuilder();
								line.append("S").append(KodiTools.formatWithLeadingZeroes(airedSeason.asInt()));
								line.append("E").append(KodiTools.formatWithLeadingZeroes(airedEpisodeNumber.asInt()));
								line.append(" ").append(episodeName.asString());
								System.out.println(line);
							}
						}
					}
				}
			}
		} catch (Exception any) {
			any.printStackTrace();
		}
	}

}
