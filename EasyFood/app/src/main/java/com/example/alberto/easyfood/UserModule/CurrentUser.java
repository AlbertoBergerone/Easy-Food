package com.example.alberto.easyfood.UserModule;

/**
 * Created by Alberto on 13/04/2016.
 */
public class CurrentUser {
    private static User _currentUserProfile;
	
	public static User get_currentUserProfile(){
		return _currentUserProfile;
	}

	public static void set_currentUserProfile(User user){
		_currentUserProfile = user;
	}
}

