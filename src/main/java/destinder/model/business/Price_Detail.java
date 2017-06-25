package main.java.destinder.model.business;

public class Price_Detail {
	
	private final String currency;
	private final Double total;

	public Price_Detail(String currency,Double total) {
		this.currency=currency;
		this.total=total;
	}

	public String getCurrency() {
		return currency;
	}

	public Double getTotal() {
		return total;
	}

}
