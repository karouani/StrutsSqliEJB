package com.sqli.challange.sessions;

import javax.ejb.Local;

@Local
public interface IAuthentificationLocal {

	public Object sauthentifier(String login,String pwd);
	public  String getMsg();
}
