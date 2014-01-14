package com.sqli.challange.sessions;

import java.util.List;

import javax.ejb.Remote;

import com.sqli.challange.entity.Administrateur;
import com.sqli.challange.entity.BusinessUnite;
import com.sqli.challange.entity.ColCompTechno;
import com.sqli.challange.entity.Collaborateurs;
import com.sqli.challange.entity.Competence;
import com.sqli.challange.entity.Compte;
import com.sqli.challange.entity.Diplomes;
import com.sqli.challange.entity.Site;
import com.sqli.challange.entity.Technologies;


@Remote
public interface IAdminRemote {

	public boolean editermonprofil(long id,Administrateur adm);
	public void ajouteradministrateur(Administrateur admin,String log,String pwd,String role);
	public void desactivercompteadmin(long id);
	
	public int ajoutermanager(Collaborateurs manager,List<String> ltechcomp,List<String> dips,long ibu,long ist,String log,String pwd,String prof);
	//new add
	public Compte ajoutercompteadmin(long id,String log,String pwd,String role);
	public Compte ajoutercomptemanager(long id,String log,String pwd,String role);
	public BusinessUnite ajouterBus(String desp);
	public Site ajouterSite(String st);
	
	
	public List<Collaborateurs> consulterAllCollaborateurrsManager();
	public List<Collaborateurs> consulterAllCollaborateurrsManagerRH();
	public Collaborateurs consulterCollaborateurs(long id);
	public List<Administrateur> consulterAllAdmin();
	public Administrateur consulterAdmin(long id);
	public List<Site> consulterAllSite();
	public List<BusinessUnite> consulterAllBu();
	
	public boolean checkLogin(String log);
	public boolean checkMatricuke(int mat);
	
	public Competence checkcomp(String comp);
	public Technologies checktechno(String tech);
	public ColCompTechno addColCompTechno(long col,long comp,long techno,String levl);
	public void colabDiplomes(long cl,long dp);
	public Diplomes addDiplomat(String titre, String promotion, String ecole,String typediplome, String typecole, String niveau);
	public Compte ajouterCompte(long col,String log,String pwd,String profil);
	public int supprimerCol(long id);
	public int editerManager(long id,Collaborateurs mng,List<String> ltechcomp,List<String> dips,long ibu,long ist,String log,String pwd,String prof);
	public void addsalhisto(long idcol,double nsal,String dt);
	
	public List<String> listDiplomes(long id);
	public List<String> listTechno(long id);
	
	
	}
