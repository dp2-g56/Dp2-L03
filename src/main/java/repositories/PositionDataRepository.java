
package repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.PositionData;

@Repository
public interface PositionDataRepository extends JpaRepository<PositionData, Integer> {

	@Query("select p from Hacker h join h.curriculums c join c.positionData p where h.id = ?1 and p.id = ?2")
	PositionData getPositionDataOfHacker(int hackerId, int positionDataId);

}
