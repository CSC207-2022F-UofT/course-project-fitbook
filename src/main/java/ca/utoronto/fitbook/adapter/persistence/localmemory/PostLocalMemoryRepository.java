package ca.utoronto.fitbook.adapter.persistence.localmemory;

import ca.utoronto.fitbook.adapter.persistence.GenericRepository;
import ca.utoronto.fitbook.application.exceptions.EntityNotFoundException;
import ca.utoronto.fitbook.application.port.in.LoadPaginatedPosts;
import ca.utoronto.fitbook.application.port.in.LoadPostListByExerciseListPort;
import ca.utoronto.fitbook.application.port.in.LoadPostListPort;
import ca.utoronto.fitbook.application.port.in.LoadPostPort;
import ca.utoronto.fitbook.application.port.out.SavePostPort;
import ca.utoronto.fitbook.entity.Post;
import java.util.*;
import java.util.stream.Collectors;

public class PostLocalMemoryRepository implements GenericRepository<Post>, LoadPostPort, SavePostPort, LoadPostListPort,LoadPaginatedPosts, LoadPostListByExerciseListPort
{
    private static final Map<String, Post> datastore = new HashMap<>();

    /**
     * @param id Id of the post
     * @return the post with the given Id
     */
    @Override
    public Post getById(String id) throws EntityNotFoundException {
        if (!datastore.containsKey(id))
            throw new EntityNotFoundException(id);
        return datastore.get(id);
    }

    /**
     * @param entity Post to be saved
     */
    @Override
    public void save(Post entity) {
        datastore.put(entity.getId(), entity);
    }

    /**
     * @param id if post to be deleted
     */
    @Override
    public void delete(String id) {
        datastore.remove(id);
    }

    /**
     * @param id Id of the post
     * @return the post with the given Id
     */
    @Override
    public Post loadPost(String id) throws EntityNotFoundException {
        return getById(id);
    }

    /**
     * @param post Post to be saved
     */
    @Override
    public void savePost(Post post) {
        save(post);
    }

    /**
     * @param postIds The post ids to be fetched
     * @return A list of posts
     * @throws EntityNotFoundException If a single post is not found
     */
    @Override
    public List<Post> loadPostList(List<String> postIds) throws EntityNotFoundException {
        List<Post> postList = new ArrayList<>();
        for (String id : postIds) {
            postList.add(getById(id));
        }
        return postList;
    }

    /**
<<<<<<< HEAD
     * Loads a maximum of `limit` number of posts after the paginationKey Id post
     *
     * @param paginationKey The post Id to begin the search at
     * @param limit         The maximum number of posts to return
     * @return A maximum of `limit` posts in a list
     */
    @Override
    public List<Post> loadPaginatedPosts(String paginationKey, int limit) {
        List<Post> posts = new ArrayList<>();

        List<Post> allPosts = new ArrayList<>(datastore.values());
        // Sort all posts by post date (newer first)
        allPosts.sort(Comparator.comparing(Post::getPostDate).reversed());

        int currentIndex = 0;
        // Move our index to the requested page if we have a key
        if (paginationKey != null) {
            while (currentIndex < allPosts.size()
                    && !allPosts.get(currentIndex).getId().equals(paginationKey))
                currentIndex++;
            // Don't include the paginationKey
            currentIndex++;
        }
        // Grab the number of pages we want
        for (int i = currentIndex; i < currentIndex + limit && i < allPosts.size(); i++)
            posts.add(allPosts.get(i));
        return posts;
    }
    /**
     * @param exerciseIdList list of exerciseIds to be fetched from database
     * @return list of post objects
     */
    @Override
    public List<Post> loadPostListByExerciseList(List<String> exerciseIdList) {
        return datastore.values().stream().filter(post -> exerciseIdList.stream()
                .anyMatch(id -> post.getExerciseIdList().stream()
                        .anyMatch(id::equals))).collect(Collectors.toList());
    }
}

