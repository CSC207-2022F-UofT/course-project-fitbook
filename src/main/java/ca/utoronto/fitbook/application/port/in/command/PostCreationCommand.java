package ca.utoronto.fitbook.application.port.in.command;

import lombok.NonNull;
import lombok.Value;

import java.util.List;

@Value
public class PostCreationCommand {
    @NonNull
    String UserID;

    @NonNull
    List<String> exerciseIdList;

    @NonNull
    String description;
}
