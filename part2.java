import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestFactory;
import com.google.api.client.http.HttpResponse;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.jayway.jsonpath.JsonPath;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.util.*;
import java.util.Map.Entry;
import org.apache.commons.lang3.StringUtils;

public class part2 {
	public static void main(String[] args) {
		try {
			
			//come from parameters
			String accountKey = "AIzaSyCIQ8gDGEMgxJpSsGK6BwkfLZXtN4MTf4E";
			String search = "Lord of the Rings";
					
			HttpTransport httpTransport = new NetHttpTransport();
			HttpRequestFactory requestFactory = httpTransport.createRequestFactory();
			JSONParser parser = new JSONParser();

			//query for books
			String query = "[{\"/book/author/works_written\":[{\"a:name\":null,\"name~=\":\""+search+"\"}],\"id\":null,\"name\":null,\"type\":\"/book/author\"}]";
			
			GenericUrl url = new GenericUrl("https://www.googleapis.com/freebase/v1/mqlread");
			url.put("query", query);
			url.put("key", accountKey);
			HttpRequest request = requestFactory.buildGetRequest(url);
			HttpResponse httpResponse = request.execute();
			JSONObject response = (JSONObject)parser.parse(httpResponse.parseAsString());
			JSONArray results = (JSONArray)response.get("result");
			
			Map<String, String> resultList = new TreeMap<String, String>(); 
			for (Object result : results) {
				String name = JsonPath.read(result,"$.name").toString();
				ArrayList<String> creations = new ArrayList<String>();
				String type = null;
				//for an author
				if(JsonPath.read(result,"$.type").toString().contains("/book/author")){
					type = "Author";
					int count = StringUtils.countMatches(JsonPath.read(result,"$./book/author/works_written").toString(), "a:name");
					for(int i=0; i<count; i++){
						creations.add(JsonPath.read(result,"$./book/author/works_written.a:name["+i+"]").toString());
					}
				}else{
					type = "?";
				}
				//compressing their creations
				String creationStr = "";
				for(int i=0; i<creations.size(); i++){
					if(i==0){
						creationStr = creationStr +"<"+ creations.get(i) + ">";
					}else if(creations.size()>2 && i==creations.size()-1){
						creationStr = creationStr + ", and <" + creations.get(i) +">";
					}else if(creations.size()==2 && i==creations.size()-1){
						creationStr = creationStr + " and <" + creations.get(i) +">";
					}else{
						creationStr = creationStr + ", <" + creations.get(i) + ">";
					}
				}
				resultList.put(name, name+" (as "+type+") created "+creationStr);
			}

			//query for organization
			String query2 = "[{\"/organization/organization_founder/organizations_founded\":[{\"a:name\":null,\"name~=\":\""+search+"\"}],\"id\":null,\"name\":null,\"type\":\"/organization/organization_founder\"}]";
			
			url = new GenericUrl("https://www.googleapis.com/freebase/v1/mqlread");
			url.put("query", query2);
			url.put("key", accountKey);
			request = requestFactory.buildGetRequest(url);
			httpResponse = request.execute();
			response = (JSONObject)parser.parse(httpResponse.parseAsString());
			results = (JSONArray)response.get("result");
			
			for (Object result : results) {
				String name = JsonPath.read(result,"$.name").toString();
				ArrayList<String> creations = new ArrayList<String>();
				String type = null;
				//for an author
				if(JsonPath.read(result,"$.type").toString().contains("/organization/organization_founder")){
					type = "Businessperson";
					int count = StringUtils.countMatches(JsonPath.read(result,"$./organization/organization_founder/organizations_founded").toString(), "a:name");
					for(int i=0; i<count; i++){
						creations.add(JsonPath.read(result,"$./organization/organization_founder/organizations_founded.a:name["+i+"]").toString());
					}
				}else{
					type = "?";
				}
				//compressing their creations
				String creationStr = "";
				for(int i=0; i<creations.size(); i++){
					if(i==0){
						creationStr = creationStr +"<"+ creations.get(i) + ">";
					}else if(creations.size()>2 && i==creations.size()-1){
						creationStr = creationStr + ", and <" + creations.get(i) +">";
					}else if(creations.size()==2 && i==creations.size()-1){
						creationStr = creationStr + " and <" + creations.get(i) +">";
					}else{
						creationStr = creationStr + ", <" + creations.get(i) + ">";
					}
				}
				resultList.put(name, name+" (as "+type+") created "+creationStr);
			}
			
			//print out results
			int i = 0;
			for(Entry<String, String> entry : resultList.entrySet()){
				i = i +1;
				System.out.println(i+". "+entry.getValue());
			}
		
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
}
