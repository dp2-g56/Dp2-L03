
package repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.Actor;

@Repository
public interface ActorRepository extends JpaRepository<Actor, Integer> {

	@Query("select a from Actor a join a.userAccount b where b.username = ?1")
	public Actor getActorByUserName(String a);

	@Query("select a from Actor a")
	public List<Actor> getActors();

	@Query("select u.username from Actor a join a.userAccount u")
	public List<String> usernamesOfActors();

	@Query("select a from Actor a join a.userAccount b where b.username != ?1")
	public List<Actor> getActorsExceptOne(String username);
}
