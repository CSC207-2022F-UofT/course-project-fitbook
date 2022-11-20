package usecase.signup;

import java.util.Date;
import java.util.List;

public class UserRegisterDsRequestModel {
    private String name;
    private String password;
    private final  String id;
    private Date joinDate;
    private int totalLikes;
    private List<String> followingIdList;
    private List<String> followerIdList;
    private List<String> postIdList;
    private List<String> likedPostIdList;

    public UserRegisterDsRequestModel(String name, String password, String id, Date joinDate, int totalLikes, List<String> followingIdList, List<String> followerIdList, List<String> postIdList, List<String> likedPostIdList) {
        this.name = name;
        this.password = password;
        this.id = id;
        this.joinDate = joinDate;
        this.totalLikes = totalLikes;
        this.followingIdList = followingIdList;
        this.followerIdList = followerIdList;
        this.postIdList = postIdList;
        this.likedPostIdList = likedPostIdList;
    }

    public UserRegisterDsRequestModel(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public String getPassword() {
        return password;
    }

    public String getId() {
        return id;
    }

    public Date getJoinDate() {
        return joinDate;
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
