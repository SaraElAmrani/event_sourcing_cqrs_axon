package ma.elamrani.event_sourcing_cqrs_axon.commands.dtos;

public record AddNewAccountRequestDTO(double initialBalance, String currency) {
}
