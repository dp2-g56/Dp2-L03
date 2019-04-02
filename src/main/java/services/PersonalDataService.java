
package services;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;

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

	public PersonalData reconstruct(FormObjectCurriculumPersonalData formObject, BindingResult binding) {
		PersonalData personalData = new PersonalData();
		
		if(formObject.getId() > 0) {
			Curriculum curriculumFounded = this.curriculumService.findOne(formObject.getId());
			PersonalData personalDataFounded = curriculumFounded.getPersonalData();
			
			personalData.setId(personalDataFounded.getId());
			personalData.setVersion(personalDataFounded.getVersion());
		} 
		personalData.setFullName(formObject.getFullName());
		personalData.setPhoneNumber(formObject.getPhoneNumber());
		personalData.setStatement(formObject.getStatement());
		personalData.setLinkedinProfile(formObject.getLinkedInProfile());
		personalData.setGitHubProfile(formObject.getGitHubProfile());
		
		return personalData;
	}

	
}
