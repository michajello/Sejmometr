package agh.lab.zad2;

public class Arguments {
	public final String numberTerm;
	public final String firstname;
	public final String lastname;
	public final boolean readingFromFile;

	public Arguments(String[] args) {
		this.numberTerm = args[0];
		this.firstname = args[1];
		this.lastname = args[2];

		if (args.length == 4)
			this.readingFromFile = true;
		else
			this.readingFromFile = false;
	}

}
