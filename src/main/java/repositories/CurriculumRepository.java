
package repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.Curriculum;
import domain.Hacker;

@Repository
public interface CurriculumRepository extends JpaRepository<Curriculum, Integer> {

	@Query("select c from Hacker h join h.curriculums c where h.id = ?1")
	public List<Curriculum> getCurriculumsOfHacker(int hackerId);

	@Query("select c from Hacker h join h.curriculums c where h.id = ?1 and c.id = ?2")
	public Curriculum getCurriculumOfHacker(int hackerId, int curriculumId);

	@Query("select p.title from Hacker h join h.curriculums c join c.positionData p where h = ?1")
	List<String> getTitlesOfPositionDatas(Hacker hacker);

	@Query("select e.degree from Hacker h join h.curriculums c join c.educationData e where h = ?1")
	List<String> getDegreesOfEducationalData(Hacker hacker);

	@Query("select m.freeText from Hacker h join h.curriculums c join c.miscellaneousData m where h = ?1")
	List<String> getFreeTestOfMiscellaneousData(Hacker hacker);
}
