package testproj;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.Writer;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;


public class Solution {

	/**
	 * Storing all the objects (customers, site visits, images and orders in HashMaps.
	 * HashMap is the DS of preference considering the efficiency of retrieving the 
	 * objects by their keys.
	 */
	static HashMap<String,Customer> customerMap = new HashMap<String,Customer>();
	static HashMap<String,SiteVisit> siteVisitMap = new HashMap<String,SiteVisit>();
	static HashMap<String,Image> imageMap = new HashMap<String,Image>();
	static HashMap<String,Order> orderMap = new HashMap<String,Order>();
	@SuppressWarnings("rawtypes")
	static HashMap<String,Event> eventMap = new HashMap<String,Event>();
	
	public int getWeekNumber(String event_time){
		Calendar cl = Calendar.getInstance();
	    SimpleDateFormat formatter= new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSX");
	    Date date = null;
		try {
			date = formatter.parse(event_time);
		} catch (ParseException e) {
			e.printStackTrace();
		}  
	    cl.setTime(date);
	    return cl.get(Calendar.WEEK_OF_YEAR);
	}
	
	public void ingest(JsonElement e) throws ParseException{
		String type = e.getAsJsonObject().get("type").getAsString();
		String verb = e.getAsJsonObject().get("verb").getAsString();
		String key = e.getAsJsonObject().get("key").getAsString();
		String event_time = e.getAsJsonObject().get("event_time").getAsString();
		
		switch(type){
		case "CUSTOMER": {
			if(verb.equals("NEW")){
				String last_name = e.getAsJsonObject().get("last_name").getAsString();
			    String adr_city = e.getAsJsonObject().get("adr_city").getAsString();
			    String adr_state = e.getAsJsonObject().get("adr_state").getAsString();
			    
			    Customer cust = new Customer(key, last_name, adr_city, adr_state);
			    customerMap.put(key, cust);
			    
			    Event<Customer> event = new Event<Customer>(event_time, key, cust);
			    eventMap.put(event_time, event);
			}
			else if(verb.equals("UPDATE")){
				if(!customerMap.containsKey(key)){
					System.out.println("Customer to be updated does not exist");
					break;
				}
				
				Customer c = customerMap.get(key);
				if(e.getAsJsonObject().get("last_name") != null){
					c.setLast_name(e.getAsJsonObject().get("last_name").getAsString());
				}
				if (e.getAsJsonObject().get("adr_city") != null) {
					c.setAdr_city(e.getAsJsonObject().get("adr_city").getAsString());
				}
				if (e.getAsJsonObject().get("adr_state") != null) {
					c.setAdr_state(e.getAsJsonObject().get("adr_state").getAsString());
				}
			}
			else {
				System.out.println("Invalid operation on Customer");
			}
			break;
		}
		case "SITE_VISIT" : {
			if(verb.equals("NEW")){
				String customer_id = e.getAsJsonObject().get("customer_id").getAsString();
				HashMap<String,String> tagkv = new HashMap<String,String>();
				JsonObject o = (JsonObject) e.getAsJsonObject().get("tags");
				for (Map.Entry<String,JsonElement> entry : o.entrySet()){
					tagkv.put(entry.getKey(), entry.getValue().getAsString());
				}

				SiteVisit siteVisit = new SiteVisit(key, tagkv);
				siteVisitMap.put(key, siteVisit);

				Event<SiteVisit> event = new Event<SiteVisit>(event_time, customer_id, siteVisit);
				eventMap.put(event_time, event);

				int weekNum = getWeekNumber(event_time);
				Customer c = customerMap.get(customer_id);
				if (!c.custVal.containsKey(weekNum)) {
					CustValue custValueData = new CustValue();
					c.custVal.put(weekNum, custValueData);
				}
				c.custVal.get(weekNum).incrementSiteVisits();
			}
			else {
				System.out.println("Invalid operation on Site Visit");
			}
			break;
		}
		case "IMAGE" : {
			if(verb.equals("UPLOAD")){
				String customer_id = e.getAsJsonObject().get("customer_id").getAsString();
				String cam_make = e.getAsJsonObject().get("camera_make").getAsString();
				String cam_model = e.getAsJsonObject().get("camera_model").getAsString();

				Image img = new Image(key, cam_make, cam_model);
				imageMap.put(key, img);

				Event<Image> event = new Event<Image>(event_time, customer_id, img);
				eventMap.put(event_time, event);
			}
			else {
				System.out.println("Invalid operation on Image");
			}
			break;
		}
		case "ORDER" : {
			if(verb.equals("NEW")){
				String customer_id = e.getAsJsonObject().get("customer_id").getAsString();
				String totalAmtStr = e.getAsJsonObject().get("total_amount").getAsString();
				Double totalAmt = 0.0;
				Matcher m = Pattern.compile("(?!=\\d\\.\\d\\.)([\\d.]+)").matcher(totalAmtStr);
				if (m.find())
				{
					totalAmt = Double.parseDouble(m.group(1));
				}

				Order order = new Order(key, totalAmt);
				orderMap.put(key, order);

				Event<Order> event = new Event<Order>(event_time, customer_id, order);
				eventMap.put(event_time, event);

				int weekNum = getWeekNumber(event_time);
				Customer c = customerMap.get(customer_id);
				if (!c.custVal.containsKey(weekNum)) {
					CustValue custValueData = new CustValue();
					c.custVal.put(weekNum, custValueData);
				}
				c.custVal.get(weekNum).addExpense(totalAmt);
			}
			else if(verb.equals("UPDATE")){
				if(!orderMap.containsKey(key)){
					System.out.println("Order to be updated does not exist");
					break;
				}
				
				Order order = orderMap.get(key);
				if(e.getAsJsonObject().get("total_amount") != null){
					String customer_id = e.getAsJsonObject().get("customer_id").getAsString();
					String totalAmtStr = e.getAsJsonObject().get("total_amount").getAsString();
					Double totalAmt = 0.0;
					Matcher m = Pattern.compile("(?!=\\d\\.\\d\\.)([\\d.]+)").matcher(totalAmtStr);
					if (m.find())
					{
						totalAmt = Double.parseDouble(m.group(1));
					}
					
					Double oldAmt = order.getTotal_amt();
					order.setTotal_amt(totalAmt);
					orderMap.put(key, order);

					Event<Order> event = new Event<Order>(event_time, customer_id, order);
					eventMap.put(event_time, event);

					int weekNum = getWeekNumber(event_time);
					Customer c = customerMap.get(customer_id);
					if (!c.custVal.containsKey(weekNum)) {
						CustValue custValueData = new CustValue();
						c.custVal.put(weekNum, custValueData);
					}
					c.custVal.get(weekNum).updateExpense(oldAmt, totalAmt);
				}
			}
			else {
				System.out.println("Invalid operation on Order");
			}
			break;
		}
		default : {
			System.out.println("Invalid event type");
			break;
		}
		}
	}
	
	/**
	 * 
	 * This method uses a TreeMap to store the customers and their
	 * corresponding LTV values. The LTV double value is used as the key
	 * and the TreeMap ensures that the entries are always stored 
	 * in an ascending order. The assumption made here is that no 
	 * two customers have the exact same LTV value. If this were the case, we'd 
	 * have to have a list of customer entries instead of just one as the map value.
	 * 
	 * @param x Number of top LTV customers
	 * @return List of the corresponding customer objects
	 * @throws Exception
	 */
	public List<Customer> topXSimpleLTVCustomers(int x) throws Exception {
		if(x>customerMap.size()){
			System.out.println("Input greater than number of customers.");
			throw new Exception();
		}
        TreeMap<Double, String> map = new TreeMap<Double, String>();
        for(Entry<String,Customer> e: customerMap.entrySet()){
            map.put(e.getValue().getLTV(), e.getKey());
        }
        
        List<Customer> res = new ArrayList<Customer>();
        while(res.size()<x){
            Map.Entry<Double, String> entry = map.pollLastEntry();
            res.add(customerMap.get(entry.getValue()));
        }
        return res;
	}
	
	public void parseInput() {
		/**
		 * the Gson library has been used for parsing the JSON input
		 */
		JsonParser parser = new JsonParser();
		try {
			Object obj = parser.parse(new FileReader("input/sample.txt"));
			JsonArray eventList = (JsonArray) obj;
			Iterator<JsonElement> iterator = eventList.iterator();
			while(iterator.hasNext()) {
				ingest(iterator.next());
			}
		}
		catch(Exception e){
			e.printStackTrace();
		}
		
	}
	
	public static void main (String[] args){
		Solution s = new Solution();
		s.parseInput();
		
		try {
			List<Customer> res = s.topXSimpleLTVCustomers(2);
			int i=0;
			FileWriter file = new FileWriter("output/output.txt");
			BufferedWriter out = new BufferedWriter(file);
			for (Customer c : res)
			{
				i++;
				String output=i+" Customer Key:" + c.getKey() + 
						" Last Name:" + c.getLast_name() +
						" LTV Value:" +c.getLTV() + "\n";
				System.out.println(i+" Customer Key:" + c.getKey() + 
						" Last Name:" + c.getLast_name() +
						" LTV Value:" +c.getLTV());
				out.write(output);
			}
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
}
