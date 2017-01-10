package agh.lab.zad2;

public class PoliticianWithAmountTrips extends Politician {
	private int amountOfTrips;
	
	public PoliticianWithAmountTrips(String name, int amountOfTrips) {
		super(name);
		this.amountOfTrips = amountOfTrips;
	}

	@Override
	public String toString() {
		return super.toString()  + ", ilosc wyjazdów = " + amountOfTrips ;
	}
	
	
	
	
	
	
}
