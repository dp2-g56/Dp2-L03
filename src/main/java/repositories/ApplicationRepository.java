
package repositories;

import java.util.Collection;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.Application;
import domain.Hacker;
import domain.Status;

@Repository
public interface ApplicationRepository extends JpaRepository<Application, Integer> {

	@Query("select p from Hacker b join b.applications p where b = ?1")
	public Collection<Application> getApplicationsByHacker(Hacker hacker);
	
	@Query("select p from Hacker b join b.applications p where b = ?1 and p.status = ?2")
	public Collection<Application> getApplicationsByHackerAndStatus(Hacker hacker, Status status);

}
