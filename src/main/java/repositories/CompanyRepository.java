
package repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.Company;

@Repository
public interface CompanyRepository extends JpaRepository<Company, Integer> {


	@Query("select m from Company m join m.userAccount u where u.username = ?1")
	public Company getCompanyByUsername(String username);


}
