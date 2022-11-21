package ca.utoronto.fitbook.adapter.persistence.localmemory;

import ca.utoronto.fitbook.adapter.persistence.GenericRepository;
import ca.utoronto.fitbook.application.port.in.LoadPostPort;
import ca.utoronto.fitbook.application.port.out.SavePostPort;
import ca.utoronto.fitbook.entity.Post;

import java.util.HashMap;
import java.util.Map;

public class PostLocalMemoryRepository implements GenericRepository<Post>, LoadPostPort, SavePostPort
{
    private static final Map<String, Post> datastore = new HashMap<>();

    /**
     * @param id Id of the post
     * @return the post with the given Id
     */
    @Override
    public Post getById(String id) {
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
     * @param id Id of the post
     * @return the post with the given Id
     */
    @Override
    public Post loadPost(String id) {
        return getById(id);
    }

    /**
     * @param post Post to be saved
     */
    @Override
    public void savePost(Post post) {
        save(post);
    }
}
