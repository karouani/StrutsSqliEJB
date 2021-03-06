package com.sqli.challange.metier;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import com.sqli.challange.entity.Compte;
import com.sqli.challange.sessions.IAuthentificationLocal;
import com.sqli.challange.sessions.IAuthentificationRemote;

@Stateless(name="login")
public class Authentification implements IAuthentificationLocal,
		IAuthentificationRemote {

	@PersistenceContext(unitName="emsqli")
	private EntityManager em;
	
	private Compte obj;
	private Object user;
	private String message;
	
	public Authentification() {
		super();
		// TODO Auto-generated constructor stub
	}

	@Override
	public Object sauthentifier(String login, String pwd) {
		// TODO Auto-generated method stub
		//System.out.println("***********************");
		Query sql=em.createQuery("select cp from Compte cp where cp.username = :x and cp.password = :y ");
		sql.setParameter("x", login.toLowerCase());
		sql.setParameter("y", pwd.toLowerCase());
		//sql.setParameter("z", "out");
		
		
			
			if(sql.getResultList().size() > 0){
				message=null;
				obj=(Compte) sql.getSingleResult();	
				//System.out.println("in here not null");
				if(!"out".equals(obj.getStatus())){
					
					this.setMessage("1");
					return getUtilisateur(obj);
				}else{
					this.setMessage("2");
					System.out.println("Par getResult() = "+this.getMessage());
				}
			}
			else{
				//System.out.println("in here  null");
				this.setMessage("0");
			}
			 
		
		//System.out.println("size "+);
		//System.out.println("Retour "+sql.getSingleResult());
		return null;
	}

	
	private Object getUtilisateur(Compte cp) {
		if(obj != null){
			if("Administrateur".equals(cp.getRole())){
				//System.out.println("yes es");
				Query sql=em.createQuery("select user from Administrateur user where user.cmp.codecompte = :x");
				sql.setParameter("x", cp.getCodecompte());
				user=sql.getSingleResult();
			}else if("ManagerAgence".equals(cp.getRole())){
				Query sql=em.createQuery("select user from Collaborateurs user where user.cmp.codecompte = :x");
				sql.setParameter("x", cp.getCodecompte());
				user=sql.getSingleResult();
			}else if("AmbassadeurRH".equals(cp.getRole())){
				Query sql=em.createQuery("select user from Collaborateurs user where user.cmp.codecompte = :x");
				sql.setParameter("x", cp.getCodecompte());
				user=sql.getSingleResult();
			}else if("ResponsableProduction".equals(cp.getRole())){
				Query sql=em.createQuery("select user from Collaborateurs user where user.cmp.codecompte = :x");
				sql.setParameter("x", cp.getCodecompte());
				user=sql.getSingleResult();
			}else if("ManagerRH".equals(cp.getRole())){
				Query sql=em.createQuery("select user from Collaborateurs user where user.cmp.codecompte = :x");
				sql.setParameter("x", cp.getCodecompte());
				user=sql.getSingleResult();
			}
			return user;
		}else{
			return null;
		}
		
		
	}

	public Compte getObj() {
		return obj;
	}

	public void setObj(Compte obj) {
		this.obj = obj;
	}

	public Object getUser() {
		return user;
	}

	public void setUser(Object user) {
		this.user = user;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String msg) {
		//System.out.println(">>> setM");
		this.message = msg;
	}

	@Override
	public String getMsg() {
		return getMessage();
	}

}
