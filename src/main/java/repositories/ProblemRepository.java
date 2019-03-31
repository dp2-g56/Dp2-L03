
package repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.Problem;

@Repository
public interface ProblemRepository extends JpaRepository<Problem, Integer> {

	@Query("select p from Company b join b.problems p where p.isDraftMode = false and b.id = ?1")
	public List<Problem> getFinalProblemsByCompany(int companyId);
}
