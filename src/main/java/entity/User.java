package entity;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

public class User {

    @NotEmpty
    private String id;
    private List<@Valid User> followingList;
    private List<@Valid User> followerList;
    private List<@Valid Post> postList;
    private List<@Valid Post> likedPostList;
    @NotEmpty
    @Size(max = 40)
    private String name;
    @NotEmpty
    @Size(min = 8, max = 40)
    private String password;
    @NotNull
    private Date joinDate;
    @NotNull
    @Min(value = 0, message = "Number of likes should not be lower than 0")
    private int totalLikes;

    /**
     * @param userBuilder to initialize User object using builder attributes
     */
    private User(UserBuilder userBuilder) {
        this.id = userBuilder.id;
        this.name = userBuilder.name;
        this.password = userBuilder.password;
        this.joinDate = userBuilder.joinDate;
        this.totalLikes = userBuilder.totalLikes;
        this.followingList = Objects.requireNonNullElseGet(userBuilder.followingList, ArrayList::new);
        this.followerList = Objects.requireNonNullElseGet(userBuilder.followerList, ArrayList::new);
        this.postList = Objects.requireNonNullElseGet(userBuilder.postList, ArrayList::new);
        this.likedPostList = Objects.requireNonNullElseGet(userBuilder.likedPostList, ArrayList::new);
    }

    /**
     * @return id associated with the User
     */
    public String getId() {
        return id;
    }

    /**
     * @param id to set id class variable
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * @return followingList class variable
     */
    public List<User> getFollowingList() {
        return followingList;
    }

    /**
     * @param followingList to set followingList class variable
     */
    public void setFollowing(List<User> followingList) {
        this.followingList = followingList;
    }

    /**
     * @return followerList class variable
     */
    public List<User> getFollowerList() {
        return followerList;
    }

    /**
     * @param followerList to set followerList class variable
     */
    public void setFollowers(List<User> followerList) {
        this.followerList = followerList;
    }

    /**
     * @return likedPostList class variable
     */
    public List<Post> getLikedPostList() {
        return likedPostList;
    }

    /**
     * @param likedPostList to set likedPostlist class variable
     */
    public void setLikedPostList(List<Post> likedPostList) {
        this.likedPostList = likedPostList;
    }

    /**
     * @return postList class variable
     */
    public List<Post> getPostList() {
        return postList;
    }

    /**
     * @param postList to set class variable postList
     */
    public void setPostList(List<Post> postList) {
        this.postList = postList;
    }

    /**
     * @return name of class variable
     */
    public String getName() {
        return name;
    }

    /**
     * @param name to set class variable name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return password class variable
     */
    public String getPassword() {
        return password;
    }

    /**
     * @param password to set class variable password
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * @return joinDate class variable
     */
    public Date getJoinDate() {
        return joinDate;
    }

    /**
     * @param joinDate to set joinDate class variable
     */
    public void setJoinDate(Date joinDate) {
        this.joinDate = joinDate;
    }

    /**
     * @return totalLikes class variable
     */
    public int getTotalLikes() {
        return totalLikes;
    }

    /**
     * @param totalLikes to set class variable totalLikes
     */
    public void setTotalLikes(int totalLikes) {
        this.totalLikes = totalLikes;
    }

    /**
     * @param post to add Post object to class variable postList
     */
    public void addPost(@Valid Post post) {
        this.postList.add(post);
    }

    /**
     * @param followerUser to add User object to class variable followerList
     */
    public void addFollower(@Valid User followerUser) {
        this.followerList.add(followerUser);
    }

    /**
     * @param followingUser to add User object to class variable followingList
     */
    public void addFollowing(@Valid User followingUser) {
        this.followingList.add(followingUser);
    }

    public static class UserBuilder {

        private String id;
        private List<User> followingList;
        private List<User> followerList;
        private List<Post> postList;
        private List<Post> likedPostList;
        private String name;
        private String password;
        private Date joinDate;
        private int totalLikes;

        public UserBuilder() { }

        /**
         * @param val to set id class variable
         * @return current object
         */
        public UserBuilder withId(String val) {
            this.id = val;
            return this;
        }

        /**
         * @param val to set
         * @return current object
         */
        public UserBuilder withFollowingList(List<User> val) {
            this.followingList = val;
            return this;
        }

        /**
         * @param val to set followerList class variable
         * @return current object
         */
        public UserBuilder withFollowerList(List<User> val) {
            this.followerList = val;
            return this;
        }

        /**
         * @param val to set likedPostList class variable
         * @return current object
         */
        public UserBuilder withLikedPostList(List<Post> val) {
            this.likedPostList = val;
            return this;
        }

        /**
         * @param val to set postList class variable
         * @return current object
         */
        public UserBuilder withPostList(List<Post> val) {
            this.postList = val;
            return this;
        }

        /**
         * @param val to set name class variable
         * @return current object
         */
        public UserBuilder withName(String val) {
            this.name = val;
            return this;
        }

        /**
         * @param val to set password class variable
         * @return current object
         */
        public UserBuilder withPassword(String val) {
            this.password = val;
            return this;
        }

        /**
         * @param val to set joinDate class variable
         * @return current object
         */
        public UserBuilder withJoinDate(Date val) {
            this.joinDate = val;
            return this;
        }

        /**
         * @param val to set totalLikes class variable
         * @return current object
         */
        public UserBuilder withTotalLikes(int val) {
            this.totalLikes = val;
            return this;
        }

        /**
         * @return User object using current object attributes
         */
        public User build() {
            return new User(this);
        }
    }
}
