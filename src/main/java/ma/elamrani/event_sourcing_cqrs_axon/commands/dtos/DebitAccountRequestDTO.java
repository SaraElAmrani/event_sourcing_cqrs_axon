package ma.elamrani.event_sourcing_cqrs_axon.commands.dtos;

public record DebitAccountRequestDTO(String accountId, double amount, String currency) {
}
