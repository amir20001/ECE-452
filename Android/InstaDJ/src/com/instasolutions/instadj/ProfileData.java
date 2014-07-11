package com.instasolutions.instadj;


public class ProfileData {

    private String profilePic;
    private String userName;
    private String score;

    public ProfileData(String profilePic, String userName, String score){
        this.profilePic = profilePic;
        this.userName = userName;
        this.score = score;
    }

    public void setProfilePic(String profilePic) {
        this.profilePic = profilePic;
    }

    public String getProfilePic() {
        return profilePic;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }
}
