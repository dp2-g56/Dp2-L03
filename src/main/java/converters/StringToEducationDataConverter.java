
package converters;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import repositories.EducationDataRepository;
import domain.EducationData;

@Component
@Transactional
public class StringToEducationDataConverter implements Converter<String, EducationData> {

	@Autowired
	EducationDataRepository	educationDataRepository;


	@Override
	public EducationData convert(String text) {

		EducationData result = new EducationData();
		int id;

		try {
			if (StringUtils.isEmpty(text)) {
				result = null;
			} else {
				id = Integer.valueOf(text);
				result = this.educationDataRepository.findOne(id);
			}

		} catch (Throwable oops) {
			throw new IllegalArgumentException(oops);
		}
		return result;
	}
}
