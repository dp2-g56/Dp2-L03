
package repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import domain.Actor;
import domain.Position;
import domain.Problem;

@Repository
public interface PositionRepository extends JpaRepository<Position, Integer> {

	@Query("select p from Position p where p.isDraftMode = false")
	List<Position> getFinalPositions();

	@Query("select p.problems from Position p where p.id = ?1")
	List<Problem> getProblemsOfPosition(int positionId);

	@Query("select distinct p from Company c join c.positions p join p.requiredTecnologies rt join p.requiredSkills rs where (p.title LIKE :keyWord OR p.description LIKE :keyWord OR p.requiredProfile LIKE :keyWord OR rt LIKE :keyWord OR rs LIKE :keyWord OR c.name LIKE :keyWord) AND p.isDraftMode = false AND p.isCancelled = false")
	List<Position> positionsFiltered(@Param("keyWord") String word);

	@Query("select a from Actor a join a.positions p where ?1 IN p")
	public Actor getActorWithPosition(int positionId);

}
