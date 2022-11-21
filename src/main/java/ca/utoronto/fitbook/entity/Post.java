package ca.utoronto.fitbook.entity;

import lombok.*;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Post {

    @Builder.Default
    @NonNull
    private String id = UUID.randomUUID().toString();
    @NonNull
    private String authorId;
    private int likes;
    @NonNull
    private Date postDate;
    @NonNull
    private List<String> exerciseIdList;
    @NonNull
    private String description;

}
