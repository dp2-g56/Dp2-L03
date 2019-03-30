
package converters;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import repositories.CompanyRepository;
import domain.Company;

@Component
@Transactional
public class StringToCompanyConverter implements Converter<String, Company> {

	@Autowired
	CompanyRepository	companyRepository;


	@Override
	public Company convert(String text) {

		Company result = new Company();
		int id;

		try {
			if (StringUtils.isEmpty(text)) {
				result = null;
			} else {
				id = Integer.valueOf(text);
				result = this.companyRepository.findOne(id);
			}

		} catch (Throwable oops) {
			throw new IllegalArgumentException(oops);
		}
		return result;
	}
}
