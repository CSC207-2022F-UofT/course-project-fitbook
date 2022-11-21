package ca.utoronto.fitbook.application.port.in.command;

import lombok.NonNull;
import lombok.Value;
import org.checkerframework.checker.units.qual.N;

import java.util.List;

@Value
public class PostCreationInputData {

    @NonNull
    String UserID;

    @NonNull
    List<String> exerciseIdList;

    @NonNull
    String description;
}
