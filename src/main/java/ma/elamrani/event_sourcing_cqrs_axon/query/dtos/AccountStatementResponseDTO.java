package ma.elamrani.event_sourcing_cqrs_axon.query.dtos;

import ma.elamrani.event_sourcing_cqrs_axon.query.entities.Account;
import ma.elamrani.event_sourcing_cqrs_axon.query.entities.Operation;

import java.util.List;

public record AccountStatementResponseDTO(Account account, List<Operation> operations) {


}
