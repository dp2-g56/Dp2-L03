package services;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;

import domain.Configuration;
import domain.Curriculum;
import domain.PersonalData;
import forms.FormObjectCurriculumPersonalData;
import repositories.PersonalDataRepository;

@Service
@Transactional
public class PersonalDataService {
	
	@Autowired
	private PersonalDataRepository personalDataRepository;
	@Autowired
	private CurriculumService curriculumService;
	@Autowired
	private ConfigurationService configurationService;

	public PersonalData save(PersonalData p) {
		return this.personalDataRepository.save(p);
	}

	public PersonalData reconstruct(FormObjectCurriculumPersonalData formObject, BindingResult binding) {
		PersonalData personalData = new PersonalData();
		
		if(formObject.getId() > 0) {
			Curriculum curriculumFounded = this.curriculumService.findOne(formObject.getId());
			PersonalData personalDataFounded = curriculumFounded.getPersonalData();
			
			personalData.setId(personalDataFounded.getId());
			personalData.setVersion(personalDataFounded.getVersion());
		} 
		
		Configuration configuration = this.configurationService.getConfiguration();
		String prefix = configuration.getSpainTelephoneCode();
		
		if(formObject.getPhoneNumber().matches("([0-9]{4,})$")) {
			formObject.setPhoneNumber(prefix + formObject.getPhoneNumber());
		}
		
		personalData.setFullName(formObject.getFullName());
		personalData.setPhoneNumber(formObject.getPhoneNumber());
		personalData.setStatement(formObject.getStatement());
		personalData.setLinkedinProfile(formObject.getLinkedInProfile());
		personalData.setGitHubProfile(formObject.getGitHubProfile());
		
		return personalData;
	}
	
	public void delete(PersonalData personalData) {
		this.personalDataRepository.delete(personalData);
	}
	
}
