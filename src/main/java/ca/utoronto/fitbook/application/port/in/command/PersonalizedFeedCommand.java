package ca.utoronto.fitbook.application.port.in.command;

import lombok.NonNull;
import lombok.Value;

@Value
public class PersonalizedFeedCommand
{
    @NonNull
    String userId;
    String paginationKey;
    int limit;
}
