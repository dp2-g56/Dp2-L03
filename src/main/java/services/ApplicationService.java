
package services;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import repositories.ApplicationRepository;
import domain.Application;
import domain.Company;
import domain.Position;
import domain.Status;

@Service
@Transactional
public class ApplicationService {

	@Autowired
	private ApplicationRepository	applicationRepository;
	@Autowired
	private CompanyService			companyService;
	@Autowired
	private PositionService			positionService;


	//----------------------------------------CRUD METHODS--------------------------
	//------------------------------------------------------------------------------
	public List<Application> getApplicationsCompany(int positionId) {
		return this.applicationRepository.getApplicationsCompany(positionId);
	}

	public Application findOne(int applicationId) {
		return this.applicationRepository.findOne(applicationId);
	}

	public Application save(Application application) {
		return this.applicationRepository.save(application);
	}

	//---------------------------------EDIT AS COMPANY-------------------------------
	//-------------------------------------------------------------------------------
	public void editApplicationCompany(Application application, boolean accept) {
		//Security
		Company loggedCompany = this.companyService.loggedCompany();
		List<Position> positions = loggedCompany.getPositions();
		Position position = application.getPosition();

		Assert.isTrue(positions.contains(position));
		Assert.isTrue(application.getStatus() == Status.SUBMITTED);

		//position.getApplications().remove(application);
		//		positions.remove(position);
		//
		//		position.setIsCancelled(true);
		//		Position saved = this.save(position);
		//		positions.add(saved);
		//		loggedCompany.setPositions(positions);
		if (accept)
			application.setStatus(Status.ACCEPTED);
		else
			application.setStatus(Status.REJECTED);

		Application saved = this.applicationRepository.save(application);
		position.getApplications().remove(application);
		position.getApplications().add(saved);
		this.positionService.save(position);
		this.companyService.save(loggedCompany);

	}
	
	public List<Application> getSubmittedApplicationCompany(Integer positionId){
		return this.applicationRepository.getSubmittedApplicationsCompany(positionId);
	}
}
