package testproj;

import java.util.HashMap;

public class SiteVisit extends Subject{
HashMap<String,String> tags;

public SiteVisit(String key, HashMap<String, String> tags) {
	super(key);
	this.tags = tags;
}

public HashMap<String, String> getTags() {
	return tags;
}

public void setTags(HashMap<String, String> tags) {
	this.tags = tags;
}

}
