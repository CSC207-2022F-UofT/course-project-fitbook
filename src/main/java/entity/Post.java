package entity;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class Post {

    @NotEmpty
    private String id;
    @NotNull
    private String authorId;
    @NotNull
    @Min(value = 0, message = "A post should have at least 0 likes")
    private int likes;
    @NotNull
    private Date postDate;
    @NotEmpty
    private List<String> exerciseIdList;

    /**
     * Default constructor for use with datastore
     */
    public Post() {}

    /**
     * @param postBuilder to initialize Post object using builder attributes
     */
    private Post(PostBuilder postBuilder) {
        this.id = postBuilder.id;
        this.authorId = postBuilder.authorId;
        this.likes = postBuilder.likes;
        this.postDate = postBuilder.postDate;
        this.exerciseIdList = Objects.requireNonNullElseGet(postBuilder.exerciseIdList, ArrayList::new);
    }

    /**
     * @return id class variable
     */
    public String getId() {
        return id;
    }

    /**
     * @param id to set class variable id
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * @return authorId class variable
     */
    public String getAuthorId() {
        return authorId;
    }

    /**
     * @param authorId to set class variable author
     */
    public void setAuthorId(String authorId) {
        this.authorId = authorId;
    }

    /**
     * @return likes class variable
     */
    public int getLikes() {
        return likes;
    }

    /**
     * @param likes to set class variable likes
     */
    public void setLikes(int likes) {
        this.likes = likes;
    }

    /**
     * @return postDate class variable
     */
    public Date getPostDate() {
        return postDate;
    }

    /**
     * @param postDate to set class variable postDate
     */
    public void setPostDate(Date postDate) {
        this.postDate = postDate;
    }

    /**
     * @return exerciseList class variable
     */
    public List<String> getExerciseIdList() {
        return exerciseIdList;
    }

    /**
     * @param exerciseIdList to set class variable exerciseList
     */
    public void setExerciseIdList(List<String> exerciseIdList) {
        this.exerciseIdList = exerciseIdList;
    }

    /**
     * @param exercise to add Exercise object to class variable exerciseList
     */
    public void addExercise(String exercise) {
        this.exerciseIdList.add(exercise);
    }

    public static class PostBuilder {
        private String id;
        private String authorId;
        private int likes;
        private Date postDate;
        private List<String> exerciseIdList;

        public PostBuilder() { }

        /**
         * @param val to set author class variable
         * @return current object
         */
        public PostBuilder withAuthorId(String val) {
            this.authorId = val;
            return this;
        }

        /**
         * @param val to set likes class variable
         * @return current object
         */
        public PostBuilder withLikes(int val) {
            this.likes = val;
            return this;
        }

        /**
         * @param val to set postDate class variable
         * @return current object
         */
        public PostBuilder withPostDate(Date val) {
            this.postDate = val;
            return this;
        }

        /**
         * @param val to set exerciseList class variable
         * @return current object
         */
        public PostBuilder withExerciseList(List<String> val) {
            this.exerciseIdList = val;
            return this;
        }

        /**
         * @return Post object using current object attributes
         */
        public Post build() {
            this.id = UUID.randomUUID().toString();
            return new Post(this);
        }
    }

}
