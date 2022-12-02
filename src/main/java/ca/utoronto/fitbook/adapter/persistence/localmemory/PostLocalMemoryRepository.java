package ca.utoronto.fitbook.adapter.persistence.localmemory;

import ca.utoronto.fitbook.adapter.persistence.GenericRepository;
import ca.utoronto.fitbook.application.exceptions.EntityNotFoundException;
import ca.utoronto.fitbook.application.port.in.LoadPostListPort;
import ca.utoronto.fitbook.application.port.in.LoadPostPort;
import ca.utoronto.fitbook.application.port.out.SavePostPort;
import ca.utoronto.fitbook.entity.Post;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PostLocalMemoryRepository implements GenericRepository<Post>, LoadPostPort, SavePostPort, LoadPostListPort
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
}

