package usecase.signup;

import java.util.Date;
import java.util.List;

public class UserRegisterResponseModel {
    private String id;
    private String name;
    private Date joinDate;
    private int totalLikes;
    private List<String> followingIdList;
    private List<String> followerIdList;
    private List<String> postIdList;
    private List<String> likedPostIdList;

    public UserRegisterResponseModel(String id,String name, Date joinDate, int totalLikes, List<String> followingIdList, List<String> followerIdList, List<String> postIdList, List<String> likedPostIdList) {
        this.id = id;
        this.name = name;
        this.joinDate = joinDate;
        this.totalLikes = totalLikes;
        this.followingIdList = followingIdList;
        this.followerIdList = followerIdList;
        this.postIdList = postIdList;
        this.likedPostIdList = likedPostIdList;
    }

    public String getName() {
        return name;
    }


    public String getId() {
        return id;
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


    public List<String> getFollowingIdList() {
        return followingIdList;
    }

    public List<String> getFollowerIdList() {
        return followerIdList;
    }

    public List<String> getPostIdList() {
        return postIdList;
    }

    public List<String> getLikedPostIdList() {
        return likedPostIdList;
    }

}
