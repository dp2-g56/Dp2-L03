
package converters;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import domain.PositionData;

@Component
@Transactional
public class PositionDataToStringConverter implements Converter<PositionData, String> {

	@Override
	public String convert(PositionData positionData) {
		String result;

		if (positionData == null) {
			result = null;
		} else {
			result = String.valueOf(positionData.getId());
		}
		return result;
	}

}
