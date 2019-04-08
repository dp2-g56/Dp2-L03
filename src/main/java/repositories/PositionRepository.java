
package repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.Position;
import domain.Problem;

@Repository
public interface PositionRepository extends JpaRepository<Position, Integer> {

	@Query("select p from Position p where p.isDraftMode = false")
	List<Position> getFinalPositions();

	@Query("select p.problems from Position p where p.id = ?1")
	List<Problem> getProblemsOfPosition(int positionId);
}
