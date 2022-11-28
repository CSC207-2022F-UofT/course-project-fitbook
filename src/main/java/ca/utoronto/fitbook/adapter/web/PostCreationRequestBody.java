package ca.utoronto.fitbook.adapter.web;

import lombok.NonNull;
import lombok.Value;

import java.util.List;

@Value
public class PostCreationRequestBody {
    @NonNull
    List<String> exerciseIdList;

    @NonNull
    String description;
}
