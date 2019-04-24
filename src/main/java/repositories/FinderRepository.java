
package repositories;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.Finder;
import domain.Hacker;
import domain.Position;

@Repository
public interface FinderRepository extends JpaRepository<Finder, Integer> {

	@Query("select distinct(p) from Position p join p.requiredTecnologies rt join p.requiredSkills rs where (rs like ?1 or rt like ?1 or p.title like ?1 or p.description like ?1 or p.requiredProfile like ?1 or p.ticker like ?1) and p.isDraftMode = false")
	public List<Position> getPositionsByKeyWord(String keyWord);
	
	@Query("select distinct(h) from Hacker h join h.finder f where (f.keyWord like ?1 or f.keyWord like ?2  or  f.keyWord like ?3  or f.keyWord like ?4 or f.keyWord like ?5 or f.keyWord like ?6)")
	public List<Hacker> getHackersThatFinderKeyWordIsContaine(String rs, String rt, String title, String desription, String requiredProfile, String ticker);

	@Query("select p from Position p where p.deadline = ?1 and p.isDraftMode = false")
	public List<Position> getPositionsByDeadline(Date deadLine);

	@Query("select p from Position p where (p.deadline < ?1 or p.deadline = ?1) and p.isDraftMode = false")
	public List<Position> getPositionsByMaxDeadline(Date maxDeadLine);

	@Query("select p from Position p where (p.offeredSalary > ?1 or p.offeredSalary = ?1) and p.isDraftMode = false")
	public List<Position> getPositionsByMinSalary(Double minSalary);

	@Query("select f from Finder f where ?1 member of f.positions")
	public List<Finder> getFindersContainsPosition(Position position);

}
