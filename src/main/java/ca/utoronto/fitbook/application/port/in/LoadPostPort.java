package ca.utoronto.fitbook.application.port.in;

import ca.utoronto.fitbook.application.exceptions.EntityNotFoundException;
import ca.utoronto.fitbook.entity.Post;

public interface LoadPostPort
{
    Post loadPost(String id) throws EntityNotFoundException;
}
