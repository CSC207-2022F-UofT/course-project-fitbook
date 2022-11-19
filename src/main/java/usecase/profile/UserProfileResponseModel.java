package usecase.profile;

import entity.Post;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.Date;
import java.util.List;

public class UserProfileResponseModel {
    @NotEmpty
    private final String name;
    @NotEmpty
    private final int followingSize;
    @NotEmpty
    private final int followerSize;
    @NotNull
    private String joinDate;
    @NotNull
    private final List<@Valid Post> postList;
    @NotNull
    private final List<@Valid Post> likedPostList;

    public UserProfileResponseModel(String name, int followingSize, int followerSize, String joinDate, List<Post> postList, List<Post> likedPostList) {
        this.name = name;
        this.followingSize = followingSize;
        this.followerSize = followerSize;
        this.joinDate = joinDate;
        this.postList = postList;
        this.likedPostList = likedPostList;
    }

    public String getName(){
        return name;
    }

    public int getFollowingSize(){
        return followingSize;
    }

    public int getFollowerSize(){
        return followerSize;
    }

    public String getJoinDate(){
        return joinDate;
    }

    public void setJoinDate(String joinDate) {this.joinDate = joinDate;}

    public List<Post> getPostList(){
        return postList;
    }

    public List<Post> getLikedPostList(){
        return likedPostList;
    }
}
