package ca.utoronto.fitbook.application.port.out.response;

import lombok.NonNull;
import lombok.Value;

@Value
public class FollowResponse {
    @NonNull
    String followeeId;
}
