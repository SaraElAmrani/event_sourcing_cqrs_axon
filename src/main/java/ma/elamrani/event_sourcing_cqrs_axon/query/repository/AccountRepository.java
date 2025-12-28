package ma.elamrani.event_sourcing_cqrs_axon.query.repository;


import ma.elamrani.event_sourcing_cqrs_axon.query.entities.Account;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountRepository extends JpaRepository<Account, String> {
}
