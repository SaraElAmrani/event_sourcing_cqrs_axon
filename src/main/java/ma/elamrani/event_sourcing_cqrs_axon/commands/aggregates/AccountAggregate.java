package ma.elamrani.event_sourcing_cqrs_axon.commands.aggregates;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import ma.elamrani.event_sourcing_cqrs_axon.commands.commands.AddAccountCommand;
import ma.elamrani.event_sourcing_cqrs_axon.commands.commands.CreditAccountCommand;
import ma.elamrani.event_sourcing_cqrs_axon.commands.commands.DebitAccountCommand;
import ma.elamrani.event_sourcing_cqrs_axon.commands.commands.UpdateAccountStatusCommand;
import ma.elamrani.event_sourcing_cqrs_axon.enums.AccountStatus;
import ma.elamrani.event_sourcing_cqrs_axon.events.*;
import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.modelling.command.AggregateIdentifier;
import org.axonframework.modelling.command.AggregateLifecycle;
import org.axonframework.spring.stereotype.Aggregate;

@Aggregate
@Entity
@Slf4j
@Getter
@Setter
public class AccountAggregate {
    @AggregateIdentifier
    @Id
    private String accountId ;
    private double currentBalance;
    private String currency;
    private AccountStatus status;


    // pour Axon obligatoire ce constructeur par defaut
    public AccountAggregate() {
        log.info("Account Aggregate Created");
    }

    @CommandHandler
    public AccountAggregate(AddAccountCommand command) {
        log.info("CreateAccount Command Received");
        if (command.getInitialBalance()<=0) throw  new IllegalArgumentException("Balance negative exception");
        AggregateLifecycle.apply(new AccountCreatedEvent(
                command.getId(),
                command.getInitialBalance(),
                command.getCurrency(),
                AccountStatus.CREATED
        ));
        AggregateLifecycle.apply(new AccountActivatedEvent(
                command.getId(),
                AccountStatus.ACTIVATED
        ));

    }

    @CommandHandler
    public void handleCommand(DebitAccountCommand command){
        log.info("DebitAccountCommand Command Received");
        if (!status.equals(AccountStatus.ACTIVATED)) throw  new RuntimeException("This account" + command.getId()+ "can not be debited because of the account is not activated. The current status is ");
        if (command.getAmount()>currentBalance) throw  new RuntimeException("Balance not sufficient exception");
        if (command.getAmount()<=0) throw  new IllegalArgumentException("Amount negative exception, so must be positive");
        AggregateLifecycle.apply(new AccountDebitedEvent(
                command.getId(),
                command.getAmount(),
                command.getCurrency()
        ));
    }
    @CommandHandler
    public void handle(CreditAccountCommand command){
        log.info("CreditAccountCommand Command Received");
        if (!status.equals(AccountStatus.ACTIVATED)) throw  new RuntimeException("This account" + command.getId()+ "can not be debited because of the account is not activated. The current status is "+status);
        if (command.getAmount()<=0) throw  new IllegalArgumentException("Amount negative exception, so must be positive");
        AggregateLifecycle.apply(new AccountCreditedEvent(
                command.getId(),
                command.getAmount(),
                command.getCurrency()
        ));
    }

    @CommandHandler
    public void handleCommand(UpdateAccountStatusCommand command){
        log.info("UpdateAccountStatusCommand Command Received");
        if (command.getAccountStatus() == status) throw  new RuntimeException("This account" + command.getId()+"is already  "+command.getAccountStatus()+ " state");
        AggregateLifecycle.apply(new AccountStatusUpdatedEvent(
                command.getId(),
                command.getAccountStatus()
        ));
    }
    @EventSourcingHandler
    //@EventHandler
    public void on(AccountCreatedEvent event){
        log.info("AccountCreatedEvent occured");
        this.accountId =event.getAccountId();
        this.currentBalance = event.getInitialBalance();
        this.currency = event.getCurrency();
        this.status = event.getStatus();
    }
    @EventSourcingHandler
    //@EventHandler
    public void on(AccountActivatedEvent event){
        log.info("AccountActivatedEvent occured");
        this.accountId =event.getAccountId();
        this.status = event.getStatus();
    }
    @EventSourcingHandler
    //@EventHandler
    public void on(AccountDebitedEvent event){
        log.info("AccountDebitedEvent occured");
        this.accountId =event.getAccountId();
        this.currentBalance = this.currentBalance - event.getAmount();
    }
    @EventSourcingHandler
    //@EventHandler
    public void on(AccountCreditedEvent event){
        log.info("AccountCreditedEvent occured");
        this.accountId =event.getAccountId();
        this.currentBalance = this.currentBalance + event.getAmount();
    }

    @EventSourcingHandler
    //@EventHandler
    public void on(AccountStatusUpdatedEvent event){
        log.info("AccountStatusUpdatedEvent occured");
        this.accountId =event.getAccountId();
        this.status = event.getAccountStatus();
    }


}