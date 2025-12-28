package ma.elamrani.event_sourcing_cqrs_axon.commands.dtos;

public record CreditAccountRequestDTO(String accountId, double amount, String currency) {
}
