package ma.elamrani.event_sourcing_cqrs_axon.query.handlers;

import lombok.extern.slf4j.Slf4j;
import ma.elamrani.event_sourcing_cqrs_axon.query.dtos.AccountEvent;
import ma.elamrani.event_sourcing_cqrs_axon.query.dtos.AccountStatementResponseDTO;
import ma.elamrani.event_sourcing_cqrs_axon.query.entities.Account;
import ma.elamrani.event_sourcing_cqrs_axon.query.entities.Operation;
import ma.elamrani.event_sourcing_cqrs_axon.query.queries.GetAccountStatement;
import ma.elamrani.event_sourcing_cqrs_axon.query.queries.GetAllAccounts;
import ma.elamrani.event_sourcing_cqrs_axon.query.queries.WatchEventQuery;
import ma.elamrani.event_sourcing_cqrs_axon.query.repository.AccountRepository;
import ma.elamrani.event_sourcing_cqrs_axon.query.repository.OperationRepository;
import org.axonframework.queryhandling.QueryHandler;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class AccountQueryHandler {
    private AccountRepository accountRepository;
    private OperationRepository operationRepository;

    public AccountQueryHandler(AccountRepository accountRepository, OperationRepository operationRepository) {
        this.accountRepository = accountRepository;
        this.operationRepository = operationRepository;
    }
    @QueryHandler
    public List<Account> on(GetAllAccounts query){
        return accountRepository.findAll();
    }
    @QueryHandler
    public AccountStatementResponseDTO on(GetAccountStatement query){
        Account account = accountRepository.findById(query.getAccountId()).get();
        List<Operation> operations = operationRepository.findByAccountId(query.getAccountId());
        return new AccountStatementResponseDTO(account, operations);
    }

    @QueryHandler
    public AccountEvent on(WatchEventQuery query){
        return AccountEvent.builder().build();
    }


}
