
package repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.Admin;

@Repository
public interface AdminRepository extends JpaRepository<Admin, Integer> {

	@Query("select m from Admin m join m.userAccount u where u.username = ?1")
	public Admin getAdminByUsername(String username);

	//The minimum, the maximum, the average, and the standard deviation of the
	//number of curricula per hacker.

	@Query("select max(h.curriculums.size) from Hacker h")
	public Float maxCurriculumPerHacker();

	@Query("select min(h.curriculums.size) from Hacker h")
	public Float minCurriculumPerHacker();

	@Query("select avg(h.curriculums.size) from Hacker h")
	public Float avgCurriculumPerHacker();

	@Query("select stddev(h.curriculums.size) from Hacker h")
	public Float stddevCurriculumPerHacker();

	/*
	 * The minimum, the maximum, the average, and the standard deviation of the
	 * number of results in the finders.
	 */

	@Query("select min(a.positions.size) from Finder a")
	public Float minResultFinders();

	@Query("select max(a.positions.size) from Finder a")
	public Float maxResultFinders();

	@Query("select avg(a.positions.size) from Finder a")
	public Float avgResultFinders();

	@Query("select stddev(a.positions.size) from Finder a")
	public Float stddevResultFinders();

	@Query("select (cast((select count(a) from Finder a where a.positions.size = 0) as float)/(select count(c) from Finder c where c.positions.size > 0)) from Configuration b")
	public Float ratioEmptyFinder();

	@Query("select count(c) from Finder c where c.positions.size > 0")
	public Integer numberNonEmptyFinders();

}
