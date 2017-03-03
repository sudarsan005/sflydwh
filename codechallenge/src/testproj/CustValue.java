package testproj;

import java.util.ArrayList;
import java.util.List;

/**
 * 
 * @author sjanakiraman
 * This class is used to store the individual expenses and site visits per week. 
 * It is used to calculate average customer value per week.
 *
 */
public class CustValue {

	List<Double> expenditure;
	Integer siteVisits;
	Double avg;
	
	public CustValue(){
		expenditure = new ArrayList<Double>();
		siteVisits = 0;
		avg = 0.0;
	}
	public CustValue(List<Double> expenditure, Integer siteVisits, Double avg) {
		super();
		this.expenditure = expenditure;
		this.siteVisits = siteVisits;
		this.avg = avg;
	}
	public List<Double> getExpenditure() {
		return expenditure;
	}
	public Integer getSiteVisits() {
		return siteVisits;
	}
	
	private void updateAvg() {
		  Double sum = 0.0;
		  Double t = 0.0;
		  if(!this.expenditure.isEmpty()) {
		    for (Double expense : this.expenditure) {
		        sum += expense;
		    }
		    t = sum / this.expenditure.size();
		  }
		  this.avg = t * this.siteVisits;
	}
	
	public void incrementSiteVisits() {
		this.siteVisits++;
		updateAvg();
	}
	
	public void addExpense(Double expense) {
		this.expenditure.add(expense);
		updateAvg();
	}
	
	public void updateExpense(Double oldExp, Double newExp) {
		for(int i=0; i<this.expenditure.size(); i++){
			if (this.expenditure.get(i)==oldExp){
				this.expenditure.set(i, newExp);
			}
		}
		updateAvg();
	}
	
	
}
