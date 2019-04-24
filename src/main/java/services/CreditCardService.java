
package services;

import java.util.Calendar;

import javax.transaction.Transactional;

import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import domain.CreditCard;

@Service
@Transactional
public class CreditCardService {

	public boolean validateNumberCreditCard(CreditCard creditCard) {
		Assert.notNull(creditCard);

		String str = creditCard.getNumber().toString();

		if (str.length() != 16) {
			return false;
		} else {
			int[] ints = new int[str.length()];
			for (int i = 0; i < str.length(); i++) {
				ints[i] = Integer.parseInt(str.substring(i, i + 1));
			}
			for (int i = ints.length - 2; i >= 0; i = i - 2) {
				int j = ints[i];
				j = j * 2;
				if (j > 9) {
					j = j % 10 + 1;
				}
				ints[i] = j;
			}
			int sum = 0;
			for (int i = 0; i < ints.length; i++) {
				sum += ints[i];
			}
			if (sum % 10 == 0) {
				return true;
			} else {
				return false;
			}
		}
	}

	public boolean validateDateCreditCard(CreditCard creditCard) {
		boolean res = true;

		Calendar c = Calendar.getInstance();
		Integer month = c.get(Calendar.MONTH) + 1;
		Integer year = c.get(Calendar.YEAR);

		if (creditCard.getExpirationYear() + 2000 < year) {
			res = false;
		} else if (creditCard.getExpirationYear() + 2000 == year && creditCard.getExpirationMonth() < month) {
			res = false;
		}

		return res;
	}

	public boolean validateCvvCreditCard(CreditCard creditCard) {
		return creditCard.getCvvCode() >= 100 && creditCard.getCvvCode() <= 999;
	}

}
