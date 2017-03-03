package testproj;

/**
 * 
 * @author sjanakiraman
 * This abstract class is the parent class of all the
 * possible subjects including customers, images, orders 
 * and site visits.
 *
 */
public class Subject {
	String key;

	public Subject(String key) {
		this.key = key;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}
	
}
