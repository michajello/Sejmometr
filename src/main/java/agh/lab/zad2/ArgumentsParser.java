package agh.lab.zad2;

import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ArgumentsParser {

	private static final Pattern wordPattern = Pattern.compile("[a-zA-Z ĄĘÓŁĆŻŹŃŚĆ ąęółćżźńść -]+");
	private static Matcher matcher;

	public Arguments parse(String[] args) {
		if (args.length != 3 && args.length != 4)
			throw new IllegalArgumentException("Nieprawidlowa liczba argumentów");
		StringBuilder builder = new StringBuilder();

		for (String string : args) {
			builder.append(string).append(" ");
		}

		Scanner scaner = new Scanner(builder.toString());
		Arguments arguments = null;

		try {
			if (!scaner.hasNextInt())
				throw new IllegalArgumentException("Pierwszy parametr musi być liczba 7 lub 8");
			else {

				int x = scaner.nextInt();
				if (x != 7 && x != 8)
					throw new IllegalArgumentException("Nieprawidłowa wartość pierwszego parametru");

				String firtname = scaner.next();
				matcher = wordPattern.matcher(firtname);
				if (!matcher.matches())
					throw new IllegalArgumentException("Imie zawiera niedozwolone znaki");

				String lastname = scaner.next();
				matcher = wordPattern.matcher(lastname);
				if (!matcher.matches())
					throw new IllegalArgumentException("Nazwisko zawiera niedozwolone znaki");
				
				if (args.length == 4) {
					String readingFromFileOption = scaner.next();
					if (!readingFromFileOption.equals("r")) {
					throw new IllegalArgumentException("Czwarty argument jest opcjonalny i moze przyjmowac tylko wartosc 'r'");
					}
				}
			}
		} finally {
			scaner.close();
		}
		arguments = new Arguments(args);
		return arguments;

	}

}
