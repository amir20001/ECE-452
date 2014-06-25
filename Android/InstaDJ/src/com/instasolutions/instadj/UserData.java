package com.instasolutions.instadj;

import android.app.Activity;

public class UserData {
	
	public String UserName;
	public String Picture_URL;
<<<<<<< Updated upstream

	public UserData(Activity activity, String UserName, String Picture_URL){
=======
	
	public UserData(String UserName, String Picture_URL){
>>>>>>> Stashed changes
		this.UserName = UserName;
		this.Picture_URL = Picture_URL;
		
	}
	
	public UserData(String UserName, String Picture_URL){
		this.UserName = UserName;
		this.Picture_URL = Picture_URL;
	}
}
