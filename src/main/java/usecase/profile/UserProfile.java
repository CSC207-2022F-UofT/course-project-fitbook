package usecase.profile;

import entity.Post;
import entity.User;

import javax.swing.*;
import java.util.Date;
import java.util.List;

public class UserProfile {
    private final User user;

    /**
     * @param user to set user class variable
     */
    public UserProfile(User user){
        this.user = user;
    }

    /**
     * @return user's number of followers
     */
    public int getUserFollowerCount(){
        return this.user.getFollowerList().size();
    }

    /**
     * @return number user follows
     */
    public int getUserFollowingCount(){
        return this.user.getFollowingList().size();
    }

    /**
     * @return user object posts
     */
    public List<Post> getUserPosts(){
        return this.user.getPostList();
    }

    /**
     * @return user object liked posts
     */
    public List<Post> getLikedPosts(){
        return this.user.getLikedPostList();
    }

    /**
     * @return user object name
     */
    public String getUserName(){
        return this.user.getName();
    }

    /**
     * @return user object date joined
     */
    public Date getJoinDate() {
        return this.user.getJoinDate();
    }

    /**
     * @return string rendering of user information
     */
    public String render(){
        // Temporary Implementation of Render in User Profile
        return String.format("%s %n %d Followers %d Following %n Joined: %s", this.getUserName(), this.getUserFollowerCount(), this.getUserFollowingCount(), this.getJoinDate().toString());
    }
}
