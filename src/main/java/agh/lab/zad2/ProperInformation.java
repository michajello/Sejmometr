package agh.lab.zad2;

import java.util.ArrayList;

public class ProperInformation {
	
	
	private boolean isSelectedPolitician =true;
	private Double sumExpensesBySelectedPolitician;
	private Double repairExpensesBySelectedPolitician;

	private Double averageExpensesAllPoliticians;
	private PolitcianWithAbroadDays politicianMaxTimeAbroad;
	private PoliticianWithCostTrips politicianMaxCostOfTrips;
	private PoliticianWithAmountTrips politicianWithMaxAmountTrips;
	private ArrayList<String> politicianVisitedItalia;
	
	public ProperInformation(SelectedPolitician selectedPolitician, double sumExpenses, double repairExpenses,
			double average, PolitcianWithAbroadDays politicianMaxTimeAbroad, PoliticianWithCostTrips maxCostOfTrips,  PoliticianWithAmountTrips politicianWithMaxAmountTrips,
			ArrayList<String> politicianVisitedItalia) {
		
		if(selectedPolitician == null) isSelectedPolitician=false;
		else{
			this.sumExpensesBySelectedPolitician=sumExpenses;
			this.repairExpensesBySelectedPolitician=repairExpenses;
		}
		this.politicianWithMaxAmountTrips=politicianWithMaxAmountTrips;
		this.averageExpensesAllPoliticians = average;
		this.politicianMaxTimeAbroad=politicianMaxTimeAbroad;
		this.politicianMaxCostOfTrips=maxCostOfTrips;
		this.politicianVisitedItalia=politicianVisitedItalia;

	}
	public void print(){
		System.out.println();
		if(!isSelectedPolitician){
			System.out.println("Nie istnieje wybrany polityk w bazie, wiec brak o nim danych");
		}
		else{
			System.out.println("Suma wydatków wybranego polityka: " + sumExpensesBySelectedPolitician);
			System.out.println("Koszty drobnych napraw wybranego polityka: " + repairExpensesBySelectedPolitician);
		}
		System.out.println("Polityk który odbył najwięcej podróży: " + politicianWithMaxAmountTrips);
		System.out.println("Średnia wydatków  dla wszystkich polityków wynosi: " + averageExpensesAllPoliticians);
		System.out.println("Polityk który najdłużej przebywał zagranicą to: "+ politicianMaxTimeAbroad);
		System.out.println("Polityk który odbył najdroższą podróż zgraniczną to: "+ politicianMaxCostOfTrips);
		
		System.out.println("Politycy którzy zwiedzili Włochy:");
		for (String politician : politicianVisitedItalia) {
			System.out.print(politician + ", ");
		}
		System.out.println();
	}

	

}
