package ca.utoronto.fitbook.application.port.in;

import ca.utoronto.fitbook.entity.Post;

import java.util.List;

public interface LoadPaginatedPosts
{
    /**
     * Loads a maximum of `limit` number of posts after the paginationKey Id post
     *
     * @param paginationKey The post Id to begin the search at
     * @param limit         The maximum number of posts to return
     * @return A maximum of `limit` posts in a list
     */
    List<Post> loadPaginatedPosts(String paginationKey, int limit);
}
