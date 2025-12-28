package ma.elamrani.event_sourcing_cqrs_axon.query.queries;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor @Getter
public class WatchEventQuery {
    private String accountId;
}
