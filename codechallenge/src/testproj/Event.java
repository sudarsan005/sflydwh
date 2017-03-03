package testproj;

public class Event<T> {

	String event_time;
	String customer_id;
	T subject;
	
	public Event(String event_time, String customer_id, T subject) {
		super();
		this.event_time = event_time;
		this.customer_id = customer_id;
		this.subject = subject;
	}

	public String getEvent_time() {
		return event_time;
	}

	public void setEvent_time(String event_time) {
		this.event_time = event_time;
	}

	public String getCustomer_id() {
		return customer_id;
	}

	public void setCustomer_id(String customer_id) {
		this.customer_id = customer_id;
	}

	public T getSubject() {
		return subject;
	}

	public void setSubject(T subject) {
		this.subject = subject;
	}
}
