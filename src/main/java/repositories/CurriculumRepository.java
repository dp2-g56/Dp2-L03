
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
}
