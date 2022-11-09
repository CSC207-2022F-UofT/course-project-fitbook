package entity;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

public class Post {

    @NotEmpty
    private String id;
    @NotNull
    private User author;
    @NotNull
    @Min(value = 0, message = "A post should have at least 0 likes")
    private int likes;
    @NotNull
    private Date postDate;
    @NotEmpty
    private List<@Valid Exercise> exerciseList;

    /**
     * @param postBuilder to initialize Post object using builder attributes
     */
    private Post(PostBuilder postBuilder) {
        this.id = postBuilder.id;
        this.author = postBuilder.author;
        this.likes = postBuilder.likes;
        this.postDate = postBuilder.postDate;
        this.exerciseList = Objects.requireNonNullElseGet(postBuilder.exerciseList, ArrayList::new);
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
     * @return author class variable
     */
    public User getUser() {
        return author;
    }

    /**
     * @param author to set class variable author
     */
    public void setUser(@Valid User author) {
        this.author = author;
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
    public List<Exercise> getExerciseList() {
        return exerciseList;
    }

    /**
     * @param exerciseList to set class variable exerciseList
     */
    public void setExerciseList(List<Exercise> exerciseList) {
        this.exerciseList = exerciseList;
    }

    /**
     * @param exercise to add Exercise object to class variable exerciseList
     */
    public void addExercise(@Valid Exercise exercise) {
        this.exerciseList.add(exercise);
    }

    public static class PostBuilder {
        private String id;
        private User author;
        private int likes;
        private Date postDate;
        private List<Exercise> exerciseList;

        public PostBuilder() { }

        /**
         * @param val to set id class variable
         * @return current object
         */
        public PostBuilder withId(String val) {
            this.id = val;
            return this;
        }

        /**
         * @param val to set author class variable
         * @return current object
         */
        public PostBuilder withAuthor(@Valid User val) {
            this.author = val;
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
        public PostBuilder withExerciseList(List<Exercise> val) {
            this.exerciseList = val;
            return this;
        }

        /**
         * @return Post object using current object attributes
         */
        public Post build() {
            return new Post(this);
        }
    }

}
