package testproj;

public class Image extends Subject{

	String camera_make;
	String camera_model;
	public Image(String key, String camera_make, String camera_model) {
		super(key);
		this.camera_make = camera_make;
		this.camera_model = camera_model;
	}
	public String getCamera_make() {
		return camera_make;
	}
	public void setCamera_make(String camera_make) {
		this.camera_make = camera_make;
	}
	public String getCamera_model() {
		return camera_model;
	}
	public void setCamera_model(String camera_model) {
		this.camera_model = camera_model;
	}
	
}
