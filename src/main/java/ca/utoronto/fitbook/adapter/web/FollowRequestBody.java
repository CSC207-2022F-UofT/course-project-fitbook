package ca.utoronto.fitbook.adapter.web;

import lombok.*;

@Getter
@Setter
public class FollowRequestBody
{
    @NonNull
    String followeeId;
}
