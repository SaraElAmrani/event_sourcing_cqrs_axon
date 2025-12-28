package ma.elamrani.event_sourcing_cqrs_axon.query.dtos;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter @AllArgsConstructor @Builder
public class AccountEvent {
    private String type;
    private String accountId;
    private double amount;
    private double balance;
    private String status;
}
