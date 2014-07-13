package com.instasolutions.instadj;

public class UserData {
	
	private String FirstName;
	private String LastName;
	private String UserID;
    private String score;
	
	public UserData(String FirstName, String LastName, String UserID, String score){
		this.FirstName = FirstName;
		this.LastName = LastName;
		this.UserID = UserID;
        this.score = score;
	}

    public String getFirstName() {
        return FirstName;
    }

    public void setFirstName(String firstName) {
        FirstName = firstName;
    }

    public String getLastName() {
        return LastName;
    }

    public void setLastName(String lastName) {
        LastName = lastName;
    }

    public String getUserID() {
        return UserID;
    }

    public void setUserID(String userID) {
        UserID = userID;
    }

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }
}
