package com.cmdrest.Demo;

import java.util.List;
import java.util.Map;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import org.glassfish.jersey.client.ClientConfig;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class RestClient {
	
	private static final ClientConfig config = new ClientConfig();
	private static final Client client = ClientBuilder.newClient();
	private static final ObjectMapper mapper = new ObjectMapper();
	private WebTarget webTarget;
	private WebTarget resourceWebTarget;
	private Invocation.Builder invocationBuilder;
	
	/*
	 * 
	 * initiating parameters
	 * 
	 * */
	
	public RestClient() {
		webTarget = null;
		resourceWebTarget = null;
	}
	public RestClient(String rootApi) {
		webTarget = client.target(rootApi);
		resourceWebTarget = null;
	}
	
	/*
	 * 
	 * Setting root url of the api
	 * 
	 * */
	public Boolean setRootApi(String rootApi) {
		try {
			webTarget = client.target(rootApi);
			return true;
		}catch(Exception e) {
			return false;
		}
		
	}
	
	/*
	 * 
	 * Setting resource path of api url
	 * s
	 * */
	public Boolean setResourcePath(String path, MultivaluedMap<String, String> queryParam) {
		if(webTarget != null) {
			resourceWebTarget = webTarget.path(path);
			try {
				if(queryParam != null) {
					for (Map.Entry<String, List<String>> entry : queryParam.entrySet()) {
						resourceWebTarget = resourceWebTarget.queryParam(entry.getKey(), entry.getValue().toArray());
		            }
				}
				
				System.out.println(resourceWebTarget.getUri());
				invocationBuilder = resourceWebTarget.request(MediaType.APPLICATION_JSON);
				return true;
			} catch(Exception e) {
				return false;
			}
		}
		return false;
	}
	
	/*
	 * 
	 * Get Request
	 * 
	 * */
	
	public JsonNode getRequest() {
		if(webTarget != null && resourceWebTarget != null) {
			JsonNode json;
			try {
				String response = invocationBuilder.get(String.class);
				json = mapper.readTree(response);
				return json;
			} catch (Exception e) {
				// TODO Auto-generated catch block
				return null;
			}
			
			
		}
		return null;
	}
	
	/*
	 * 
	 * Post Request
	 * 
	 * */
	
	public void postRequest(String jsonBody) {
		
		try {
			Invocation response = invocationBuilder.buildPost(Entity.json(jsonBody));
			System.out.println("Response for post");
			System.out.println(response.invoke(String.class));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	
	}
	
	/*
	 * 
	 * Put Request
	 * 
	 * */
	
	public void putRequest(String jsonBody) {
		
		try {
			Invocation response = invocationBuilder.buildPut(Entity.json(jsonBody));
			System.out.println("Response for put");
			System.out.println(response.invoke(String.class));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	
	}
	
	/*
	 * 
	 * delete Request
	 * 
	 * */
	
	public void deleteRequest() {
		
		try {
			Invocation response = invocationBuilder.build("DELETE", Entity.json(null));
			System.out.println("Response for delete");
			System.out.println(response.invoke(String.class));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	
	}
	
}
