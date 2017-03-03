package testproj;

public class Order extends Subject{

	double total_amt;

	public Order(String key, double total_amt) {
		super(key);
		this.total_amt = total_amt;
	}

	public double getTotal_amt() {
		return total_amt;
	}

	public void setTotal_amt(double total_amt) {
		this.total_amt = total_amt;
	}	
}
