package agh.lab.zad2;

public class PoliticianWithCostTrips extends Politician{
	private double costOfAllTrips;

	public PoliticianWithCostTrips(String name, double costOfAllTrips) {
		super(name);
		this.costOfAllTrips = costOfAllTrips;
	}


	@Override
	public String toString() {
		return super.toString() + ", Koszty podróży = " + costOfAllTrips ;
	}
	
	
	
}
