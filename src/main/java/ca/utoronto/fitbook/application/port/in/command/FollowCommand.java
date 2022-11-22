package ca.utoronto.fitbook.application.port.in.command;

import lombok.NonNull;
import lombok.Value;

@Value
public class FollowCommand {
    @NonNull
    String followerId;
    @NonNull
    String followeeId;
}
