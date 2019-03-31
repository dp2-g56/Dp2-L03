
package repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.Company;


import domain.Position;

import domain.Problem;

@Repository
public interface ProblemRepository extends JpaRepository<Problem, Integer> {



	@Query("select p from Company b join b.problems p where p.isDraftMode = false and b.id = ?1")
	public List<Problem> getFinalProblemsByCompany(int companyId);

	@Query("select c.problems from Company c where c = ?1")
	public List<Problem> getProblemsByCompany(Company company);

	@Query("select p from Position p join p.problems c where (select k from Problem k where k.id = ?1) in c")
	public List<Position> getPositionsByProblem(int problemId);


}
