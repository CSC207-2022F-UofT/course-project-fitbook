package ca.utoronto.fitbook.entity;


import lombok.*;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class User {

    @Builder.Default
    @NonNull
    private String id = UUID.randomUUID().toString();
    @NonNull
    private List<String> followingIdList;
    @NonNull
    private List<String> followersIdList;
    @NonNull
    private List<String> postIdList;
    @NonNull
    private List<String> likedPostIdList;
    @NonNull
    private String name;
    @NonNull
    private String password;
    @NonNull
    private Date joinDate;
    private int totalLikes;

}
