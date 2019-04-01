
package repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.Finder;
import domain.Position;

@Repository
public interface FinderRepository extends JpaRepository<Finder, Integer> {

	@Query("select distinct(p) from Position p join p.requiredTecnologies rt join p.requiredSkills rs where (rs like ?1 or rt like ?1 or p.title like ?1 or p.description like ?1 or p.requiredProfile like ?1 or p.ticker like ?1) and p.isDraftMode = false")
	public List<Position> getPositionsByKeyWord(String keyWord);
	
} 
