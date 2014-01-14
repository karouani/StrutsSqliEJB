package com.sqli.challange.sessions;

import javax.ejb.Remote;

@Remote
public interface IAuthentificationRemote {

	public Object sauthentifier(String login,String pwd);
	//public  Object getUtilisateur();
	public  String getMsg();
}
