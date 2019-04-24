
package repositories;

import java.util.Collection;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.Actor;
import domain.Application;
import domain.Curriculum;
import domain.Hacker;
import domain.Status;

@Repository
public interface ApplicationRepository extends JpaRepository<Application, Integer> {

	@Query("select p from Hacker b join b.applications p where b = ?1")
	public Collection<Application> getApplicationsByHacker(Hacker hacker);

	@Query("select p from Hacker b join b.applications p where b = ?1 and p.status = ?2")
	public Collection<Application> getApplicationsByHackerAndStatus(Hacker hacker, Status status);

	@Query("select a from Position p join p.applications a where a.status != 'PENDING' and p.id=?1")
	public List<Application> getApplicationsCompany(int positionId);

	@Query("select a from Position p join p.applications a where a.status = 'SUBMITTED' or a.status = 'PENDING' and p.id=?1")
	public List<Application> getSubmittedApplicationsCompany(int positionId);

	@Query("select c from Hacker h join h.applications a join a.curriculum c where h = ?1")
	public List<Curriculum> getCopyCurriculumsOfApplications(Hacker hacker);

	@Query("select c from Company c join c.positions cp join cp.applications ap where ap.id = ?1")
	public Actor getCompanyByApplicationId(int applicationId);
}
