package agh.lab.zad2;

public class PolitcianWithAbroadDays extends Politician {
	private int daysSpendedAbroad;

	public PolitcianWithAbroadDays(String name, int daysSpendedAbroad) {
		super(name);
		this.daysSpendedAbroad = daysSpendedAbroad;
	}

	@Override
	public String toString() {
		return  super.toString()+ ", Liczba dni spędzonych za granicą = " + daysSpendedAbroad ;
	}
	
	

}
