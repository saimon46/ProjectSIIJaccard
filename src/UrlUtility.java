import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.Proxy;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import twitter4j.URLEntity;
 
/**
 * @author Abhijit Ghosh
 * @version 1.0
 */
public class UrlUtility {
	
	final static int LENGTH_ID = 22;
	
    public static String expandUrl(String shortenedUrl) throws IOException {
        URL url = new URL(shortenedUrl);
        // open connection
        HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection(Proxy.NO_PROXY); 
        
        // stop following browser redirect
        httpURLConnection.setInstanceFollowRedirects(false);
         
        // extract location header containing the actual destination URL
        String expandedURL = httpURLConnection.getHeaderField("Location");
        httpURLConnection.disconnect();
         
        return expandedURL;
    }
    
    public static boolean containsUrlSpotify(URLEntity[] urls) throws IOException {
    	boolean ctr = false;
    	
    	for(URLEntity url: urls){
    		String urlString = url.getExpandedURL();
    		
    		if(urlString.contains(".spotify.com/track")){
    			String singleId = getSingleIdFromUrl(urlString);
				if(singleId.length() == LENGTH_ID){
					ctr = true;
					break;
				}
    		}
    			
    		if(urlString.contains("spoti.fi/")) {
    			String expandUrl = expandUrl(urlString);
				if(expandUrl.contains(".spotify.com/track")){
					String singleId = getSingleIdFromUrl(expandUrl);
					if(singleId.length() == LENGTH_ID){
						ctr = true;
						break;
					}
				}
			}
    	}
    	return ctr;
    }
    
    public static List<String> getIdsFromUrls(URLEntity[] urls) throws IOException{
    	List<String> ids = new ArrayList<String>();
    	
    	for(URLEntity url: urls){
    		String urlString = url.getExpandedURL();
    		
    		if(urlString.contains(".spotify.com/track")){
    			String singleId = getSingleIdFromUrl(urlString);
    			if(singleId.length() == LENGTH_ID){
    				ids.add(singleId);
    			}
    		}
    			
    		if(urlString.contains("spoti.fi/")){
    			String expandUrl = expandUrl(urlString);
	    		if(expandUrl != null)
	    			if(expandUrl.contains(".spotify.com/track")){
		    			String singleId = getSingleIdFromUrl(expandUrl);
		    			if(singleId.length() == LENGTH_ID)
		    				ids.add(singleId);
	    			}
   			}
    	}
    	if(ids.isEmpty())
    		return null;
    	return ids;
    }
    
    private static String getSingleIdFromUrl(String url){
    	StringTokenizer urlStringToken = new StringTokenizer(url,"/");
		
		String token = "";
		
		while(urlStringToken.hasMoreTokens())
			token = urlStringToken.nextToken();
		return token;
    }
}