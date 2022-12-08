package ca.utoronto.fitbook.adapter.web.requestbody;

import lombok.*;

@Getter
@Setter
public class FollowRequestBody
{
    @NonNull
    String followeeId;
}
