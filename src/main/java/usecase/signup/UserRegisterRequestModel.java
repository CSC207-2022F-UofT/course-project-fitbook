package usecase.signup;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class UserRegisterRequestModel {

    private String name;
    private String password;
    private String repeatPassword;
    private String id;
    private Date joinDate;
    private int totalLikes;
    private List<String> followingIdList;
    private List<String> followerIdList;
    private List<String> postIdList;
    private List<String> likedPostIdList;

    public UserRegisterRequestModel(String name, String password, String repeatPassword) throws ParseException {
        this.name = name;
        this.password = password;
        this.repeatPassword = repeatPassword;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        joinDate = sdf.parse(sdf.format(new Date()));
        totalLikes = 0;
        followerIdList = new ArrayList<>();
        postIdList = new ArrayList<>();
        likedPostIdList = new ArrayList<>();
        followingIdList = new ArrayList<>();

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

    public String getRepeatPassword() {
        return repeatPassword;
    }

    public void setRepeatPassword(String repeatPassword) {
        this.repeatPassword = repeatPassword;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public List<String> getFollowingIdList() {
        return followingIdList;
    }

    public void setFollowingIdList(List<String> followingIdList) {
        this.followingIdList = followingIdList;
    }

    public List<String> getFollowerIdList() {
        return followerIdList;
    }

    public void setFollowerIdList(List<String> followerIdList) {
        this.followerIdList = followerIdList;
    }

    public List<String> getPostIdList() {
        return postIdList;
    }

    public void setPostIdList(List<String> postIdList) {
        this.postIdList = postIdList;
    }

    public List<String> getLikedPostIdList() {
        return likedPostIdList;
    }

    public void setLikedPostIdList(List<String> likedPostIdList) {
        this.likedPostIdList = likedPostIdList;
    }
}
