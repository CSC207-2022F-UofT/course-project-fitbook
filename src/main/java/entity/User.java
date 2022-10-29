package entity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class User {

    private int id;
    private int following;
    private int followers;
    private List<Post> postList;
    private String name;
    private String password;
    private Date joinDate;
    private int totalLikes;

    private User(Builder builder) {
        this.id = builder.id;
        this.following = builder.following;
        this.followers = builder.followers;
        this.postList = builder.postList;
        this.name = builder.name;
        this.password = builder.password;
        this.joinDate = builder.joinDate;
        this.totalLikes = builder.totalLikes;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getFollowing() {
        return following;
    }

    public void setFollowing(int following) {
        this.following = following;
    }

    public int getFollowers() {
        return followers;
    }

    public void setFollowers(int followers) {
        this.followers = followers;
    }

    public List<Post> getPostList() {
        return postList;
    }

    public void setPostList(List<Post> postList) {
        this.postList = postList;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Date getJoinDate() {
        return joinDate;
    }

    public void setJoinDate(Date joinDate) {
        this.joinDate = joinDate;
    }

    public int getTotalLikes() {
        return totalLikes;
    }

    public void setTotalLikes(int totalLikes) {
        this.totalLikes = totalLikes;
    }

    public void addPost(Post post) {
        if(this.postList.isEmpty()) {
            this.postList = new ArrayList<>();
            this.postList.add(post);
        } else {
            this.postList.add(post);
        }
    }

    public static class Builder {

        private int id;
        private int following;
        private int followers;
        private List<Post> postList;
        private String name;
        private String password;
        private Date joinDate;
        private int totalLikes;

        public Builder() {}

        public Builder withId(int val) {
            this.id = val;
            return this;
        }

        public Builder withFollowing(int val) {
            this.following = val;
            return this;
        }

        public Builder withFollowers(int val) {
            this.followers = val;
            return this;
        }

        public Builder withPostList(List<Post> val) {
            this.postList = val;
            return this;
        }

        public Builder withName(String val) {
            this.name = val;
            return this;
        }

        public Builder withPassword(String val) {
            this.password = val;
            return this;
        }

        public Builder withJoinDate(Date val) {
            this.joinDate = val;
            return this;
        }

        public Builder withTotalLikes(int val) {
            this.totalLikes = val;
            return this;
        }

        public User build() {
            return new User(this);
        }
    }
}
