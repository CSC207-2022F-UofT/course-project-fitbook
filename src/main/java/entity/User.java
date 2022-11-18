package entity;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.*;

public class User {

    @NotEmpty
    private String id;
    private List<String> followingIdList;
    private List<String> followersIdList;
    private List<String> postIdList;
    private List<String> likedPostIdList;
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
     * Default constructor for use with datastore
     */
    public User() {}

    /**
     * @param userBuilder to initialize User object using builder attributes
     */
    private User(UserBuilder userBuilder) {
        this.id = userBuilder.id;
        this.name = userBuilder.name;
        this.password = userBuilder.password;
        this.joinDate = userBuilder.joinDate;
        this.totalLikes = userBuilder.totalLikes;
        this.followingIdList = Objects.requireNonNullElseGet(userBuilder.followingIdList, ArrayList::new);
        this.followersIdList = Objects.requireNonNullElseGet(userBuilder.followersIdList, ArrayList::new);
        this.postIdList = Objects.requireNonNullElseGet(userBuilder.postIdList, ArrayList::new);
        this.likedPostIdList = Objects.requireNonNullElseGet(userBuilder.likedPostIdList, ArrayList::new);
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
     * @return followingIdList class variable
     */
    public List<String> getFollowingIdList() {
        return followingIdList;
    }

    /**
     * @param followingIdList to set followingIdList class variable
     */
    public void setFollowingIdList(List<String> followingIdList) {
        this.followingIdList = followingIdList;
    }

    /**
     * @return followersIdList class variable
     */
    public List<String> getFollowersIdList() {
        return followersIdList;
    }

    /**
     * @param followersIdList to set followersIdList class variable
     */
    public void setFollowersIdList(List<String> followersIdList) {
        this.followersIdList = followersIdList;
    }

    /**
     * @return likedPostList class variable
     */
    public List<String> getLikedPostIdList() {
        return likedPostIdList;
    }

    /**
     * @param likedPostIdList to set likedPostlist class variable
     */
    public void setLikedPostIdList(List<String> likedPostIdList) {
        this.likedPostIdList = likedPostIdList;
    }

    /**
     * @return postList class variable
     */
    public List<String> getPostIdList() {
        return postIdList;
    }

    /**
     * @param postIdList to set class variable postList
     */
    public void setPostIdList(List<String> postIdList) {
        this.postIdList = postIdList;
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
    public void addPost(String post) {
        this.postIdList.add(post);
    }

    /**
     * @param followerUser to add User object to class variable followerList
     */
    public void addFollower(String followerUser) {
        this.followersIdList.add(followerUser);
    }

    /**
     * @param followingUser to add User object to class variable followingList
     */
    public void addFollowing(String followingUser) {
        this.followingIdList.add(followingUser);
    }

    public static class UserBuilder {

        private String id;
        private List<String> followingIdList;
        private List<String> followersIdList;
        private List<String> postIdList;
        private List<String> likedPostIdList;
        private String name;
        private String password;
        private Date joinDate;
        private int totalLikes;

        public UserBuilder() { }

        /**
         * @param val to set
         * @return current object
         */
        public UserBuilder withFollowingList(List<String> val) {
            this.followingIdList = val;
            return this;
        }

        /**
         * @param val to set followerList class variable
         * @return current object
         */
        public UserBuilder withFollowersList(List<String> val) {
            this.followersIdList = val;
            return this;
        }

        /**
         * @param val to set likedPostList class variable
         * @return current object
         */
        public UserBuilder withLikedPostList(List<String> val) {
            this.likedPostIdList = val;
            return this;
        }

        /**
         * @param val to set postList class variable
         * @return current object
         */
        public UserBuilder withPostList(List<String> val) {
            this.postIdList = val;
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
            this.id = UUID.randomUUID().toString();
            return new User(this);
        }
    }
}
