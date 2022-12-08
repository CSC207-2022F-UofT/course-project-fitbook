package ca.utoronto.fitbook.application.port.in.command;

import lombok.NonNull;
import lombok.Value;

import java.util.List;

@Value
public class PostCreationCommand {
    @NonNull
    String userId;

    @NonNull
    List<String> exerciseIdList;

    @NonNull
    String description;
}
