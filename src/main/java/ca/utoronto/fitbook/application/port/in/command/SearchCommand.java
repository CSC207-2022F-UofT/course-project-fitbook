package ca.utoronto.fitbook.application.port.in.command;

import lombok.NonNull;
import lombok.Value;

@Value
public class SearchCommand {
    @NonNull
    String queryString;
    @NonNull
    String userId;
}
