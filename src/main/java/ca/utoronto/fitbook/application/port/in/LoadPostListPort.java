package ca.utoronto.fitbook.application.port.in;

import ca.utoronto.fitbook.application.exceptions.EntityNotFoundException;
import ca.utoronto.fitbook.entity.Post;

import java.util.List;

public interface LoadPostListPort
{
    List<Post> loadPostList(List<String> postIds) throws EntityNotFoundException;
}
