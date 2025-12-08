package ma.elamrani.event_sourcing_cqrs_axon.events;

import lombok.AllArgsConstructor;
import lombok.Getter;
import ma.elamrani.event_sourcing_cqrs_axon.enums.AccountStatus;


@Getter @AllArgsConstructor
public class AccountCreatedEvent {
    private String accountId;
    private double initialBalance;
    private String currency;
    private AccountStatus status;
}
