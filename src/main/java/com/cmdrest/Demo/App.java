package com.cmdrest.Demo;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.Callable;

import javax.ws.rs.core.MultivaluedHashMap;
import javax.ws.rs.core.MultivaluedMap;

import org.slf4j.LoggerFactory;

import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;
import picocli.CommandLine.Parameters;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import ch.qos.logback.classic.Logger;

/**
 * Hello world!
 *
 */


@Command(name = "rest", mixinStandardHelpOptions = true, version = "rest 1.0",
		description = "Fulfills api requests using cli")
class DemoRest implements Callable<Integer>{
	private static final ObjectMapper mapper = new ObjectMapper();
	private String[] valueList;
	private String[] keyList;
	private MultivaluedMap<String, String> queryParams;
	
	@Option(
	      names = {"--root", "-r"}, description = "Root api link", required = true
	)
	private String rootApi;
	
	@Option( names = { "--endpoint", "-e" }, description = "endpoint", required = true)
	private String endPoint;
	
	@Option( names = "--op", description = "type of request [GET, POST, PUT, DELETE]", required = true)
	private String method;
	
	@Option( names = "--resource", description = "resource needed for method POST, DELETE, PUT, PATCH")
	private String resource;
	
	@Option( names = {"--queryKeys", "-qk"}, description = "keys for the query parameters key1,key2,..,keyn")
	private String keys;
	
	@Option( names = {"--queryValues", "-qv"}, description = "values for the corresponding keys in query parameters value1,value2,..,valuen")
	private String values;
	
	public MultivaluedMap<String, String> getQueryParam() {
		valueList = values.split(",");
		keyList = keys.split(",");
		if(valueList.length == keyList.length && valueList.length != 0 && keyList.length != 0) {
			queryParams = new MultivaluedHashMap<String, String>();
			for(int i = 0; i < valueList.length; i++) {
				queryParams.add(keyList[i], valueList[i]);
			}
			return queryParams;
		} else {
			return null;
		}
	}
	
	
	public JsonNode getJson() {
		JsonNode json;
		try {
			json = mapper.readTree(resource);
			System.out.println(json.toPrettyString());
			return json;
		} catch (JsonMappingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	public Integer call() throws Exception {
		RestClient rc = new RestClient();
        rc.setRootApi(rootApi);
        rc.setResourcePath(endPoint, this.getQueryParam());
        
        /*
         * Calling method as per given in command line
         * */
        if(method.equals("get")) {
        	JsonNode response = rc.getRequest();
          if(response != null) {
          	System.out.println(rc.getRequest().toPrettyString());
          } else {
          	System.out.println("404 NOT FOUND");
          }
        } else if(method.equals("post")) {
        	rc.postRequest(this.getJson().toString());
        } else if(method.equals("put")) {
        	rc.putRequest(this.getJson().toString());
        } else if(method.equals("delete")) {
        	rc.deleteRequest();
        } else {
        	System.out.println("Not a valid request method");
        	return 0;
        }
		return 1;
	}
	
	
}


public class App {
	
	private static final Logger logger 
    = (Logger) LoggerFactory.getLogger(App.class);

    public static void main(String[] args) {
    	logger.info("Example log from {}", App.class.getSimpleName());
  
        int exitCode = new CommandLine(new DemoRest()).execute(args);
        System.exit(exitCode);
    }
}
