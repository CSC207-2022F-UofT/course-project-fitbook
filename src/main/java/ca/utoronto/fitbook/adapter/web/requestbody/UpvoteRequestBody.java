package ca.utoronto.fitbook.adapter.web.requestbody;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

@Getter
@Setter
public class UpvoteRequestBody
{
    @NonNull
    String postId;
}
