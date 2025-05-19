package model;

public class LoginLogic {

	public boolean execute(User user) {
		return user.getPass().equals("1234");
	}
}
