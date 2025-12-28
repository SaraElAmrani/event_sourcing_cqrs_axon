package ma.elamrani.event_sourcing_cqrs_axon.query.controllers;

import ma.elamrani.event_sourcing_cqrs_axon.query.dtos.AccountEvent;
import ma.elamrani.event_sourcing_cqrs_axon.query.dtos.AccountStatementResponseDTO;
import ma.elamrani.event_sourcing_cqrs_axon.query.entities.Account;
import ma.elamrani.event_sourcing_cqrs_axon.query.queries.GetAccountStatement;
import ma.elamrani.event_sourcing_cqrs_axon.query.queries.GetAllAccounts;
import ma.elamrani.event_sourcing_cqrs_axon.query.queries.WatchEventQuery;
import org.axonframework.messaging.responsetypes.ResponseTypes;
import org.axonframework.queryhandling.QueryGateway;
import org.axonframework.queryhandling.SubscriptionQueryResult;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/query/accounts")
@CrossOrigin("*")
public class AccountQueryController {
    private QueryGateway queryGateway;

    public AccountQueryController(QueryGateway queryGateway) {
        this.queryGateway = queryGateway;
    }

    @GetMapping("/all")
    public CompletableFuture<List<Account>> getAllAccounts(){
        CompletableFuture<List<Account>> result = queryGateway.query(new GetAllAccounts(), ResponseTypes.multipleInstancesOf(Account.class));
        return result;
    }
    @GetMapping("/accountStatement/{accountId}")
    public CompletableFuture<AccountStatementResponseDTO> getAccountStatement(@PathVariable String accountId){
        return queryGateway.query(new GetAccountStatement(accountId), ResponseTypes.instanceOf(AccountStatementResponseDTO.class));
    }

    @GetMapping(value = "/watch/{accountId}", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<AccountEvent> watch(@PathVariable String accountId){
        SubscriptionQueryResult<AccountEvent, AccountEvent> result = queryGateway.subscriptionQuery(new WatchEventQuery(accountId),
                ResponseTypes.instanceOf(AccountEvent.class),
                ResponseTypes.instanceOf(AccountEvent.class)
        );
        return result.initialResult().concatWith(result.updates());
    }


}
