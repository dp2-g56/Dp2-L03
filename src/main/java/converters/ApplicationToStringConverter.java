
package converters;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import domain.Application;

@Component
@Transactional
public class ApplicationToStringConverter implements Converter<Application, String> {

	@Override
	public String convert(Application application) {
		String result;

		if (application == null) {
			result = null;
		} else {
			result = String.valueOf(application.getId());
		}
		return result;
	}

}
