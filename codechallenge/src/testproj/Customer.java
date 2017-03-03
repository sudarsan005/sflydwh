package testproj;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;

public class Customer extends Subject{

	String last_name;
	String adr_city;
	String adr_state;
	public HashMap<Integer, CustValue> custVal;
	
	public Customer(String key, String last_name, String adr_city, String adr_state) {
		super(key);
		this.last_name = last_name;
		this.adr_city = adr_city;
		this.adr_state = adr_state;
		custVal = new HashMap<Integer,CustValue>();
	}
	
	/**
	 * This method keeps track of the LTV value for the customer object.
	 * @return LTV value of customer
	 */
	public Double getLTV(){
		Collection<CustValue> list1 = custVal.values();
		Iterator<CustValue> i = list1.iterator();
		Double sum = 0.0;
		int n = 0;
		while(i.hasNext()){
			sum += i.next().avg;
			n++;
		}
		sum = sum/n;
		return (520 * sum);
	}
	
	public String getLast_name() {
		return last_name;
	}
	public void setLast_name(String last_name) {
		this.last_name = last_name;
	}
	public String getAdr_city() {
		return adr_city;
	}
	public void setAdr_city(String adr_city) {
		this.adr_city = adr_city;
	}
	public String getAdr_state() {
		return adr_state;
	}
	public void setAdr_state(String adr_state) {
		this.adr_state = adr_state;
	}
	
}
