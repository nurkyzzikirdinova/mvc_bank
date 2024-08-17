package zikirdinova.mvc.repo;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import zikirdinova.mvc.entities.Cash;

import java.util.Optional;

@Repository
public interface CashRepository extends JpaRepository<Cash, Long> {
    boolean existsByCashName(String cashName);
    Optional<Cash> findByCashName(String name);

}

