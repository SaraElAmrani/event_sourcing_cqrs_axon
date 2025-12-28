package ma.elamrani.event_sourcing_cqrs_axon.query.repository;


import ma.elamrani.event_sourcing_cqrs_axon.query.entities.Operation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OperationRepository extends JpaRepository<Operation,Long> {
    List<Operation> findByAccountId(String accountId);
}
