package ca.utoronto.fitbook.application.port.out;

import ca.utoronto.fitbook.entity.Post;

public interface SavePostPort
{
    void savePost(Post post);
}
