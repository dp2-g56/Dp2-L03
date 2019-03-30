
package repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import domain.Hacker;

@Repository
public interface HackerRepository extends JpaRepository<Hacker, Integer> {

}
