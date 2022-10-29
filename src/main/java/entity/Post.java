package entity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Post {

    private int id;
    private User user;
    private int likes;
    private Date postDate;
    private List<Exercise> exerciseList;

    private Post(Builder builder) {
        this.id = builder.id;
        this.user = builder.user;
        this.likes = builder.likes;
        this.postDate = builder.postDate;
        this.exerciseList = builder.exerciseList;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public int getLikes() {
        return likes;
    }

    public void setLikes(int likes) {
        this.likes = likes;
    }

    public Date getPostDate() {
        return postDate;
    }

    public void setPostDate(Date postDate) {
        this.postDate = postDate;
    }

    public List<Exercise> getExerciseList() {
        return exerciseList;
    }

    public void setExerciseList(List<Exercise> exerciseList) {
        this.exerciseList = exerciseList;
    }

    public void addPost(Exercise exercise) {
        if(this.exerciseList.isEmpty()) {
            this.exerciseList = new ArrayList<>();
            this.exerciseList.add(exercise);
        } else {
            this.exerciseList.add(exercise);
        }
    }

    public static class Builder {
        private int id;
        private User user;
        private int likes;
        private Date postDate;
        private List<Exercise> exerciseList;

        public Builder() {}

        public Builder withId(int val) {
            this.id = val;
            return this;
        }

        public Builder withUser(User val) {
            this.user = val;
            return this;
        }

        public Builder withLikes(int val) {
            this.likes = val;
            return this;
        }

        public Builder withPostDate(Date val) {
            this.postDate = val;
            return this;
        }

        public Builder withExerciseList(List<Exercise> val) {
            this.exerciseList = val;
            return this;
        }

        public Post build() {
            return new Post(this);
        }
    }

}
