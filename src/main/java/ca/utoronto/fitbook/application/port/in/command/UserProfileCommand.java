package ca.utoronto.fitbook.application.port.in.command;

import lombok.NonNull;
import lombok.Value;

@Value
public class UserProfileCommand {
    @NonNull
    String profileId;
    @NonNull
    String userId;
}