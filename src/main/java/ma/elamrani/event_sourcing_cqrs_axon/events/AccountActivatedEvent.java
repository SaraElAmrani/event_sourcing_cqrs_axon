package ma.elamrani.event_sourcing_cqrs_axon.events;

import lombok.AllArgsConstructor;
import lombok.Getter;
import ma.elamrani.event_sourcing_cqrs_axon.enums.AccountStatus;


@Getter @AllArgsConstructor
public class AccountActivatedEvent {
    private String accountId;
    private AccountStatus status;
}
