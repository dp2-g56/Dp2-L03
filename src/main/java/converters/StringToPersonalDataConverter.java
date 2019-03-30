
package converters;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import repositories.PersonalDataRepository;
import domain.PersonalData;

@Component
@Transactional
public class StringToPersonalDataConverter implements Converter<String, PersonalData> {

	@Autowired
	PersonalDataRepository	personalDataRepository;


	@Override
	public PersonalData convert(String text) {

		PersonalData result = new PersonalData();
		int id;

		try {
			if (StringUtils.isEmpty(text)) {
				result = null;
			} else {
				id = Integer.valueOf(text);
				result = this.personalDataRepository.findOne(id);
			}

		} catch (Throwable oops) {
			throw new IllegalArgumentException(oops);
		}
		return result;
	}
}
