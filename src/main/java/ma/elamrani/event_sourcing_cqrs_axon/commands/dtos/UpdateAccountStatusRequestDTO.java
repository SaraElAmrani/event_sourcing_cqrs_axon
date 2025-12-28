package ma.elamrani.event_sourcing_cqrs_axon.commands.dtos;

import ma.elamrani.event_sourcing_cqrs_axon.enums.AccountStatus;

public record UpdateAccountStatusRequestDTO(String accountId, AccountStatus accountStatus) {
}
