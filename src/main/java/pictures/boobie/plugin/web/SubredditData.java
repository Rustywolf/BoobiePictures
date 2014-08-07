package pictures.boobie.plugin.web;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.regex.Pattern;
import net.minecraft.util.org.apache.commons.lang3.StringEscapeUtils;
import org.bukkit.Bukkit;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import pictures.boobie.plugin.exceptions.NoResultsException;

public class SubredditData {
    
    private static String searchUrlFormat = "http://reddit.com/r/%s.json";
    private static String pageUrlFormat = "http://www.reddit.com/r/%s/.json?after=%s";
    private static Pattern matchPattern = Pattern.compile("(.+)(.jpg|.png|.jpeg)");
    
    private String source;
    private String searchString;
    private List<String> imgUrls;
    
    private String after = "";
    
    public SubredditData(String searchString, String source) {
        this.source = source;
        this.searchString = searchString;
        this.imgUrls = new ArrayList<>();
        
        this.findImages();
    }

    public String getSource() {
        return source;
    }

    public String getSearchString() {
        return searchString;
    }
    
    public void findImages() {
        
        JSONObject obj = (JSONObject)JSONValue.parse(source);
        JSONObject data = (JSONObject)obj.get("data");
        JSONArray children = (JSONArray)data.get("children");
        Iterator childIterator = children.iterator();
        
        after = (String)data.get("after");
        
        while (childIterator.hasNext()) {
            JSONObject child = (JSONObject)childIterator.next();
            JSONObject childData = (JSONObject)child.get("data");
            String url = (String)childData.get("url");
            if (matchPattern.matcher(url).matches()) {
                imgUrls.add(url);
            }
        }
        
        if (imgUrls.isEmpty()) {
            throw new NoResultsException("No results found for: " + searchString);
        }
    }
    
    public void nextPage() {
        try {
            URL url = new URL(String.format(pageUrlFormat, StringEscapeUtils.escapeHtml4(searchString), after));
            BufferedReader br = new BufferedReader(new InputStreamReader(url.openConnection().getInputStream()));
            
            StringBuilder builder = new StringBuilder();
            
            String input;
            while ((input = br.readLine()) != null) {
                builder.append(input);
            }
            
            this.source = builder.toString();
            imgUrls.clear();
            findImages();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public List<String> getUrls() {
        return new ArrayList<>(this.imgUrls);
    }
    
    public static SubredditData browse(String queryData) {
        try {
            URL url = new URL(String.format(searchUrlFormat, StringEscapeUtils.escapeHtml4(queryData)));
            BufferedReader br = new BufferedReader(new InputStreamReader(url.openConnection().getInputStream()));
            
            StringBuilder builder = new StringBuilder();
            
            String input;
            while ((input = br.readLine()) != null) {
                builder.append(input);
            }
            
            return new SubredditData(queryData, builder.toString());
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
    
}
