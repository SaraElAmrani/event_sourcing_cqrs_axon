package ma.elamrani.event_sourcing_cqrs_axon.commands.controllers;


import ma.elamrani.event_sourcing_cqrs_axon.commands.aggregates.AccountAggregate;
import ma.elamrani.event_sourcing_cqrs_axon.commands.commands.AddAccountCommand;
import ma.elamrani.event_sourcing_cqrs_axon.commands.commands.CreditAccountCommand;
import ma.elamrani.event_sourcing_cqrs_axon.commands.commands.DebitAccountCommand;
import ma.elamrani.event_sourcing_cqrs_axon.commands.commands.UpdateAccountStatusCommand;
import ma.elamrani.event_sourcing_cqrs_axon.commands.dtos.AddNewAccountRequestDTO;
import ma.elamrani.event_sourcing_cqrs_axon.commands.dtos.CreditAccountRequestDTO;
import ma.elamrani.event_sourcing_cqrs_axon.commands.dtos.DebitAccountRequestDTO;
import ma.elamrani.event_sourcing_cqrs_axon.commands.dtos.UpdateAccountStatusRequestDTO;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.axonframework.eventsourcing.eventstore.EventStore;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Stream;

@RestController
@RequestMapping("commands/accounts")
public class AccountCommandController {

    private CommandGateway commandGateway;
    private EventStore eventStore;

    public AccountCommandController(CommandGateway commandGateway, EventStore eventStore) {
        this.commandGateway = commandGateway;
        this.eventStore = eventStore;
    }

    @PostMapping("/add")
    public CompletableFuture<String> addNewAccount(@RequestBody AddNewAccountRequestDTO request){
        CompletableFuture<String> response = commandGateway.send(new AddAccountCommand(
                UUID.randomUUID().toString(),
                request.initialBalance(),
                request.currency()
        ));
        return response;
    }

    @PostMapping("/credit")
    public CompletableFuture<String> creditAccount(@RequestBody CreditAccountRequestDTO request){
        CompletableFuture<String> response = commandGateway.send(new CreditAccountCommand(
                request.accountId(),
                request.amount(),
                request.currency()
        ));
        return response;
    }

    @PostMapping("/debit")
    public CompletableFuture<String> debitAccount(@RequestBody DebitAccountRequestDTO request){
        CompletableFuture<String> response = commandGateway.send(new DebitAccountCommand(
                request.accountId(),
                request.amount(),
                request.currency()
        ));
        return response;
    }

    @PutMapping("/updateStatus")
    public CompletableFuture<String> updateStatus(@RequestBody UpdateAccountStatusRequestDTO request){
        CompletableFuture<String> response = this.commandGateway.send(new UpdateAccountStatusCommand(
                request.accountId(),
                request.accountStatus()
        ));
        return response;
    }

    @ExceptionHandler(Exception.class)
    public String exceptionHandler(Exception  exception){
        return exception.getMessage();
    }

    @GetMapping("/events/{accountId}")
    public Stream eventStore(@PathVariable String accountId){
        return eventStore.readEvents(accountId).asStream();
    }

    /*@GetMapping("/accountAggregate")
    public AccountAggregate accountAggregate(){
        return accountAggregate;
    } */

}
