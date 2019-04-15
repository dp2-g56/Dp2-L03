
package repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.Curriculum;

@Repository
public interface CurriculumRepository extends JpaRepository<Curriculum, Integer> {

	@Query("select c from Hacker h join h.curriculums c where h.id = ?1")
	public List<Curriculum> getCurriculumsOfHacker(int hackerId);
	
	@Query("select c from Hacker h join h.curriculums c where h.id = ?1 and c.id = ?2")
	public Curriculum getCurriculumOfHacker(int hackerId, int curriculumId);

	@Query("select c from Curriculum c join c.positionData p where p.id = ?1")
	public Curriculum getCurriculumOfPositionData(int positionDataId);

	@Query("select c from Curriculum c join c.educationData e where e.id = ?1")
	public Curriculum getCurriculumOfEducationData(int educationDataId);
}
