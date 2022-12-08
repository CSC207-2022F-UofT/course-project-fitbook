package ca.utoronto.fitbook.adapter.web.requestbody;

import lombok.*;

@Getter
@Setter
public class UpvoteRequestBody
{
    @NonNull
    String postId;
}
