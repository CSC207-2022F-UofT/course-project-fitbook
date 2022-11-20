package usecase.profile;

import entity.Post;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.Date;
import java.util.List;

public class UserProfileResponseModel {
    @NotEmpty
    private final String id;
    @NotEmpty
    private final String name;
    @NotEmpty
    private final int followingSize;
    @NotEmpty
    private final int followerSize;
    @NotNull
    private String joinDate;
    @NotNull
    private final List<String> postList;
    @NotNull
    private final List<String> likedPostList;

    public UserProfileResponseModel(String id, String name, int followingSize, int followerSize, String joinDate, List<String> postList, List<String> likedPostList) {
        this.id = id;
        this.name = name;
        this.followingSize = followingSize;
        this.followerSize = followerSize;
        this.joinDate = joinDate;
        this.postList = postList;
        this.likedPostList = likedPostList;
    }
    public String getId() { return id; }
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

    public List<String> getPostList(){
        return postList;
    }

    public List<String> getLikedPostList(){
        return likedPostList;
    }
}
