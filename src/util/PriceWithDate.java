package util;

public class PriceWithDate {
	private Date date;
	private double price;
	
	public PriceWithDate(double price, Date date){
		super();
		this.price = price;
		this.date = date;
	}
	
	public void setDate(Date d){ date = d; }
	public Date getDate(){ return date; }
	public void setPrice(double p){ price = p; }
	public double getPrice(){ return price; }
}