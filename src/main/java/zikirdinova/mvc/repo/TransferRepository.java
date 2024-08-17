package zikirdinova.mvc.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import zikirdinova.mvc.entities.Transfer;


import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface TransferRepository extends JpaRepository<Transfer, Long> {
    Optional<Transfer> findByCode(String code);

    @Query("select t from Transfer t where t.createdAt between :fromDate and :toDate and (:comment is null or t.comment like %:comment%)")
    List<Transfer> findTransfersByCriteria(
            @Param("fromDate") LocalDateTime fromDate,
            @Param("toDate") LocalDateTime toDate,
            @Param("comment") String comment);}
