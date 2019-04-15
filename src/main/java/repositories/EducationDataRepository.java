
package repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.EducationData;

@Repository
public interface EducationDataRepository extends JpaRepository<EducationData, Integer> {

	@Query("select e from Hacker h join h.curriculums c join c.educationData e where h.id = ?1 and e.id = ?2")
	EducationData getEducationDataOfHacker(int hackerId, int educationDataId);

}
