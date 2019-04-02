
package repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.Actor;
import domain.Message;

@Repository
public interface MessageRepository extends JpaRepository<Message, Integer> {

	@Query("select a.messages from Actor a where a = ?1")
	public List<Message> messagesOfLoggedActor(Actor a);

}
