package com.sqli.challange.sessions;

import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.ejb.Local;

import com.sqli.challange.entity.BusinessUnite;
import com.sqli.challange.entity.ColCompTechno;
import com.sqli.challange.entity.Collaborateur;
import com.sqli.challange.entity.Collaborateurs;
import com.sqli.challange.entity.Competence;
import com.sqli.challange.entity.Compte;
import com.sqli.challange.entity.Diplomes;
import com.sqli.challange.entity.ManagerRH;
import com.sqli.challange.entity.Site;
import com.sqli.challange.entity.Technologies;


@Local
public interface IManagerLocal {
	public boolean checkrhcol(long rh,long col);
	
	public List<Site> consulterAllSite();
	public List<BusinessUnite> consulterAllBu();
	
	public int editermonprofil(long id,Collaborateurs mng,List<String> ltechcomp,List<String> dips,String log,String pwd);
	public int editermyprofile(long idadm, String nom, String prenom,String email, String sexe,String username,String password);
	public void ajouterManagerRH(Collaborateurs rh,List<String> ltechcomp,List<String> dips,long ibu,long ist,String log,String pwd,String prof);
	public void ajouterCollaborateur(Collaborateur col,List<String> ltechcomp,List<String> dips,long ibu,long ist,long idrh);
	public void editerCollaborateur(long idcol,String post,double sal,long idmanagerrh,String dt);
	public void activerMRHCols(long idmrh,List<Long> listcol);
	public void desactiverMRHCols(long idmrh,List<Long> listcol);
	
	public List<Collaborateur> consulterlistCollaborateur();
	public List<ManagerRH> consulterlistManagerRH();
	public Collaborateur consulterColab(long id);
	public List<Collaborateur> consulterlistCollabRH(long rh);
	public List<Collaborateur> consulterlistCollabNRH(long rh);
	
	
	public Competence checkcomp(String comp);
	public Technologies checktechno(String tech);
	public ColCompTechno addColCompTechno(long col,long comp,long techno,String levl);
	public void colabDiplomes(long cl,long dp);
	public Diplomes addDiplomat(String titre, String promotion, String ecole,String typediplome, String typecole, String niveau);
	public boolean checkLogin(String log);
	public boolean checkMatricuke(int mat);
	
	public Compte ajouterCompte(long col,String log,String pwd,String profil);
	public void addsalhisto(long idcol,double nsal,String dt);
	public void affecterRhCol(long cl,long rh);
	public void deaffecterRhCol(long cl,long rh);
	
	public String parseDate(Date d);
	
	public List<String> listDiplomes(long id);
	public List<String> listTechno(long id);
}
