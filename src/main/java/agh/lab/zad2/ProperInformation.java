package agh.lab.zad2;

import java.math.BigDecimal;
import java.math.BigInteger;

public class ProperInformation {

	private double allAverageCost;
	private double costRepair;
	
	public double getCostRepair() {
		return costRepair;
	}

	public void setCostRepair(double costRepair) {
		this.costRepair = costRepair;
	}

	public double getAllAverageCost() {
		return allAverageCost;
	}

	public void setAllAverageCost(double allAverageCost) {
		this.allAverageCost = allAverageCost;
	}

	public void printInformation() {
		System.out.println(allAverageCost);
		System.out.println(costRepair);
		
	}

}
