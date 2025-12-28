package ma.elamrani.event_sourcing_cqrs_axon.query.handlers;


import lombok.extern.slf4j.Slf4j;
import ma.elamrani.event_sourcing_cqrs_axon.events.*;
import ma.elamrani.event_sourcing_cqrs_axon.query.dtos.AccountEvent;
import ma.elamrani.event_sourcing_cqrs_axon.query.entities.Account;
import ma.elamrani.event_sourcing_cqrs_axon.query.entities.Operation;
import ma.elamrani.event_sourcing_cqrs_axon.query.entities.OperationType;
import ma.elamrani.event_sourcing_cqrs_axon.query.repository.AccountRepository;
import ma.elamrani.event_sourcing_cqrs_axon.query.repository.OperationRepository;
import org.axonframework.eventhandling.EventHandler;
import org.axonframework.eventhandling.EventMessage;
import org.axonframework.queryhandling.QueryUpdateEmitter;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class AccountEventHandler {
    private AccountRepository accountRepository;
    private OperationRepository operationRepository;
    private QueryUpdateEmitter queryUpdateEmitter;

    public AccountEventHandler(AccountRepository accountRepository, OperationRepository operationRepository, QueryUpdateEmitter queryUpdateEmitter) {
        this.accountRepository = accountRepository;
        this.operationRepository = operationRepository;
        this.queryUpdateEmitter = queryUpdateEmitter;
    }



    @EventHandler
    public void on(AccountCreatedEvent event, EventMessage eventMessage){
        log.info("################# AccountCreatedEvent ################");
        Account account = Account.builder()
                .id(event.getAccountId())
                .balance(event.getInitialBalance())
                .status(event.getStatus())
                .currency(event.getCurrency())
                .createdAt(eventMessage.getTimestamp())
                .build();
        accountRepository.save(account);
        AccountEvent accountEvent = AccountEvent.builder()
                .type(event.getClass().getSimpleName())
                .accountId(account.getId())
                .balance(account.getBalance())
                .amount(event.getInitialBalance())
                .status(account.getStatus().toString())
                .build();
        queryUpdateEmitter.emit(e->true, accountEvent);

    }

    @EventHandler
    public void on(AccountActivatedEvent event){
        log.info("################# AccountActivatedEvent ################");
        Account account = accountRepository.findById(event.getAccountId()).get();
        account.setStatus(event.getStatus());
        accountRepository.save(account);
    }



    @EventHandler
    public void on(AccountStatusUpdatedEvent event){
        log.info("################# AccountStatusUpdatedEvent ################");
        Account account = accountRepository.findById(event.getAccountId()).get();
        account.setStatus(event.getAccountStatus());
        accountRepository.save(account);

        AccountEvent accountEvent = AccountEvent.builder()
                .type(event.getClass().getSimpleName())
                .accountId(account.getId())
                .balance(account.getBalance())
                .amount(0)
                .status(account.getStatus().toString())
                .build();
        queryUpdateEmitter.emit(e->true, accountEvent);

    }
    @EventHandler
    public void on(AccountDebitedEvent event, EventMessage eventMessage){
        log.info("################# AccountDebitedEvent ################");
        Account account = accountRepository.findById(event.getAccountId()).get();
        Operation operation = Operation.builder()
                .amount(event.getAmount())
                .date(eventMessage.getTimestamp())
                .type(OperationType.DEBIT)
                .currency(event.getCurrency())
                .account(account)
                .build();
        operationRepository.save(operation);
        account.setBalance(account.getBalance()-operation.getAmount());
        accountRepository.save(account);
        AccountEvent accountEvent = AccountEvent.builder()
                .type(event.getClass().getSimpleName())
                .accountId(account.getId())
                .balance(account.getBalance())
                .amount(event.getAmount())
                .status(account.getStatus().toString())
                .build();
        queryUpdateEmitter.emit(e->true, accountEvent);

    }
    @EventHandler
    public void on(AccountCreditedEvent event, EventMessage eventMessage){
        log.info("################# AccountCreditedEvent ################");
        Account account = accountRepository.findById(event.getAccountId()).get();
        Operation operation = Operation.builder()
                .amount(event.getAmount())
                .date(eventMessage.getTimestamp())
                .type(OperationType.DEBIT)
                .currency(event.getCurrency())
                .account(account)
                .build();
        operationRepository.save(operation);
        account.setBalance(account.getBalance() + operation.getAmount());
        accountRepository.save(account);
        AccountEvent accountEvent = AccountEvent.builder()
                .type(event.getClass().getSimpleName())
                .accountId(account.getId())
                .balance(account.getBalance())
                .amount(event.getAmount())
                .status(account.getStatus().toString())
                .build();
        queryUpdateEmitter.emit(e->true, accountEvent);

    }

}
