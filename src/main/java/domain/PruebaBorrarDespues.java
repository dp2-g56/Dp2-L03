
package domain;

import java.util.Random;

public class PruebaBorrarDespues {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		System.out.println(PruebaBorrarDespues.generateTicker());
	}
	// TODO Auto-generated method stub
	public static String generateTicker() {
		String res = "";

		String companyName = "c";

		if (companyName.length() >= 4)
			res = companyName.substring(0, 4) + "-";
		else {
			int numberOfX = 4 - companyName.length();
			res = companyName;
			for (int i = 1; i <= numberOfX; i++)
				res = res + "X";
			res = res + "-";
		}

		String numbers = "0123456789";
		StringBuilder number = new StringBuilder();
		Random rnd = new Random();

		while (number.length() < 4) {	//Es posible que haya que cambiarlo a 5
			int index = (int) (rnd.nextFloat() * numbers.length());
			number.append(numbers.charAt(index));
		}
		String numberStr = number.toString();

		res = res + numberStr;

		return res;

	}
}
