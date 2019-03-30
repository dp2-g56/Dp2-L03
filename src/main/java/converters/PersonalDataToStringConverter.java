
package converters;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import domain.PersonalData;

@Component
@Transactional
public class PersonalDataToStringConverter implements Converter<PersonalData, String> {

	@Override
	public String convert(PersonalData personalData) {
		String result;

		if (personalData == null) {
			result = null;
		} else {
			result = String.valueOf(personalData.getId());
		}
		return result;
	}

}
