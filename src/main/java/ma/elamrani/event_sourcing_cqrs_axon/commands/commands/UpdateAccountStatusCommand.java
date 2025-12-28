package ma.elamrani.event_sourcing_cqrs_axon.commands.commands;

import lombok.AllArgsConstructor;
import lombok.Getter;
import ma.elamrani.event_sourcing_cqrs_axon.enums.AccountStatus;
import org.axonframework.modelling.command.TargetAggregateIdentifier;

@Getter @AllArgsConstructor
public class UpdateAccountStatusCommand {
    @TargetAggregateIdentifier
    private String id;
    private AccountStatus accountStatus;

}
