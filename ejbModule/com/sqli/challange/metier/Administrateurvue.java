package com.sqli.challange.metier;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import com.sqli.challange.entity.Administrateur;
import com.sqli.challange.entity.AmbassadeurRH;
import com.sqli.challange.entity.BusinessUnite;
import com.sqli.challange.entity.ColCompTechno;
import com.sqli.challange.entity.ColabDips;
import com.sqli.challange.entity.Collaborateurs;
import com.sqli.challange.entity.Competence;
import com.sqli.challange.entity.Compte;
import com.sqli.challange.entity.Diplomes;
import com.sqli.challange.entity.HistoriqueSalPostTravail;
import com.sqli.challange.entity.ManagerAgence;
import com.sqli.challange.entity.ManagerRH;
import com.sqli.challange.entity.ResponsableProduction;
import com.sqli.challange.entity.Site;
import com.sqli.challange.entity.Technologies;
import com.sqli.challange.sessions.IAdminLocal;
import com.sqli.challange.sessions.IAdminRemote;


@Stateless(name="admin")
public class Administrateurvue implements IAdminLocal,IAdminRemote {

	@PersistenceContext(unitName="emsqli")
	private EntityManager em;

	@Override
	public boolean editermonprofil(long id,Administrateur administrateur) {
		Administrateur admin=em.find(Administrateur.class, id);

		String status = "in";
		if(administrateur.getCmp().getStatus() == null){
			status = "out";
		}
		if(administrateur.getDescription() != null){
			admin.setDescription(administrateur.getDescription());
		}
		if(administrateur.getEmail() != null){
			admin.setEmail(administrateur.getEmail());
		}

		if(administrateur.getNom() != null){
			admin.setNom(administrateur.getNom());
		}

		if(administrateur.getPhoto() != null){
			admin.setPhoto(administrateur.getPhoto());
		}

		if(administrateur.getPrenom() != null){
			admin.setPrenom(administrateur.getPrenom());
		}

		if(administrateur.getSexe() != null){
			admin.setSexe(administrateur.getSexe());
		}

		if(administrateur.getCmp().getPassword()  != null){
			admin.getCmp().setPassword(administrateur.getCmp().getPassword());
		}

		if(administrateur.getCmp().getUsername()  != null){
			admin.getCmp().setUsername(administrateur.getCmp().getUsername());
		}

		admin.getCmp().setStatus(status);

		em.persist(admin);
		return true;
	}



	@Override
	public void ajouteradministrateur(Administrateur admin,String log,String pwd,String role) {
		// TODO Auto-generated method stub
		//System.out.println("in add administrateur");
		em.persist(admin);
		Administrateur ad=this.consulterAdmin(admin.getIdadm());
		this.ajoutercompteadmin(ad.getIdadm(), log, pwd, role);
	}

	@Override
	public void desactivercompteadmin(long id) {
		// TODO Auto-generated method stub
		Administrateur admin=this.consulterAdmin(id);

		Compte cp=admin.getCmp();
		cp.setStatus("out");

		em.persist(cp);

	}

	@Override
	public int ajoutermanager(
				Collaborateurs rh,
				List<String> ltechcomp,
				List<String> dips,
				long ibu,
				long ist,
				String log,
				String pwd,String prof){
		int n=0;
		System.out.println("dkhelna hnaya");
		
		if(this.checkMatricuke(rh.getMatricule())){
			if(this.checkLogin(log)){
				
				List<Technologies> ltechno=new ArrayList<Technologies>();
				List<Competence> lcomp=new ArrayList<Competence>();
				List<String> llevls=new ArrayList<String>();
				List<Diplomes> ldips=new ArrayList<Diplomes>();

				for(String val:ltechcomp){
					System.out.println("valeur "+val);
					ltechno.add(this.checktechno(val.split(",")[0]));
					lcomp.add(this.checkcomp(val.split(",")[1]));
					llevls.add(val.split(",")[2]);
				}

				for(String dp:dips){

					ldips.add(this.addDiplomat(dp.split(",")[0], dp.split(",")[4], 
							dp.split(",")[0], dp.split(",")[2], 
							dp.split(",")[3], dp.split(",")[1]));

				}


				rh.setBu(em.find(BusinessUnite.class, ibu));
				rh.setSite(em.find(Site.class, ist));


				em.persist(rh);

				System.out.println("code RH 9bal "+rh.getCodecol());

				for(Diplomes dps:ldips){
					//rh.getDiplome().add(dps);
					//dps.getCols().add(rh);
					System.out.println("code dip "+dps.getTitre()+ "code rh "+rh.getCodecol());
					this.colabDiplomes(rh.getCodecol(), dps.getCodedip());
				}

				for (int i = 0; i < llevls.size(); i++) {
					System.out.println("code techno comp "+lcomp.get(i).getdicrpt()+ ltechno.get(i).getDesctechno());
					this.addColCompTechno(rh.getCodecol(), lcomp.get(i).getCodecompt(), ltechno.get(i).getCodetechno(), llevls.get(i));
				}

				this.ajouterCompte(rh.getCodecol(), log, pwd, prof);
				
			}else{
				n=2;
			}
		}
		else{
			n=1;
		}
		return n;
	}

	
	@Override
	public List<Collaborateurs> consulterAllCollaborateurrsManager() {
		// TODO Auto-generated method stub
		List<Collaborateurs> cols=new ArrayList<Collaborateurs>();
		Query sql=em.createQuery("select mg from Collaborateurs mg");

		for (int i = 0; i < sql.getResultList().size(); i++) {

			if(sql.getResultList().get(i) instanceof AmbassadeurRH){
				cols.add((AmbassadeurRH) sql.getResultList().get(i));
			}else if(sql.getResultList().get(i) instanceof ResponsableProduction){
				cols.add((ResponsableProduction) sql.getResultList().get(i));
			}else if(sql.getResultList().get(i) instanceof ManagerAgence){
				cols.add((ManagerAgence) sql.getResultList().get(i));
			}
		}
		return cols;
	}

	@Override
	public List<Collaborateurs> consulterAllCollaborateurrsManagerRH() {
		// TODO Auto-generated method stub
		List<Collaborateurs> cols=new ArrayList<Collaborateurs>();
		Query sql=em.createQuery("select mg from Collaborateurs mg");

		for (int i = 0; i < sql.getResultList().size(); i++) {
			if(sql.getResultList().get(i) instanceof ManagerRH){
				cols.add((ManagerRH) sql.getResultList().get(i));
			}
		}
		return cols;
	}

	@Override
	public List<Administrateur> consulterAllAdmin() {
		List<Administrateur> cols=new ArrayList<>();
		Query sql=em.createQuery("select a from Administrateur a");

		for (int i = 0; i < sql.getResultList().size(); i++) {
			if(sql.getResultList().get(i) instanceof Administrateur){
				cols.add((Administrateur) sql.getResultList().get(i));
				System.out.println(sql.getResultList().get(i));
			}
		}
		return cols;
	}


	@Override
	public Administrateur consulterAdmin(long id) {
		Administrateur admin=em.find(Administrateur.class, id);
		return admin;
	}

	@Override
	public Compte ajoutercompteadmin(long id, String log, String pwd,
			String role) {
		// TODO Auto-generated method stub
		Administrateur adm=this.consulterAdmin(id);
		Compte cp=new Compte(log, pwd, role);
		cp.setStatus("in");

		adm.setCmp(cp);

		em.persist(cp);

		return cp;
	}

	@Override
	public Compte ajoutercomptemanager(long id, String log, String pwd,
			String role) {
		Collaborateurs col=em.find(Collaborateurs.class, id);
		Compte cp=new Compte(log, pwd, role);
		cp.setStatus("in");

		col.setProfil(cp);

		em.persist(cp);

		return cp;
	}

	@Override
	public BusinessUnite ajouterBus(String desp) {
		// TODO Auto-generated method stub
		BusinessUnite bu=new BusinessUnite(desp);
		em.persist(bu);

		return bu;
	}

	@Override
	public Site ajouterSite(String s) {
		// TODO Auto-generated method stub
		Site st=new Site(s);
		em.persist(st);

		return st;
	}



	@Override
	public List<Site> consulterAllSite() {
		// TODO Auto-generated method stub
		Query sql=em.createQuery("select a from Site a");
		return sql.getResultList();
	}



	@Override
	public List<BusinessUnite> consulterAllBu() {
		Query sql=em.createQuery("select a from BusinessUnite a");
		return sql.getResultList();
	}



	@Override
	public boolean checkLogin(String log) {
		// TODO Auto-generated method stub
		Query sql=em.createQuery("select col from Collaborateurs col where col.cmp.username like :x");
		sql.setParameter("x", log);
		
		if(sql.getResultList().size() == 0){
			return true;
		}
		
		return false;
	}



	@Override
	public boolean checkMatricuke(int mat) {
		Query sql=em.createQuery("select col from Collaborateurs col where col.matricule = :x");
		sql.setParameter("x", mat);
		
		if(sql.getResultList().size() == 0){
			return true;
		}
		
		return false;
	}
	
	@Override
	public Competence checkcomp(String comp) {
		// TODO Auto-generated method stub
		Query sql=em.createQuery("select cp from Competence cp where cp.dicrpt LIKE :x");
		sql.setParameter("x", comp);
		Competence cp=null;
		
		for (int i = 0; i < sql.getResultList().size(); i++) {
			cp=(Competence) sql.getResultList().get(i);
		}
		if(cp != null){
			return cp;
		}
		else{
			cp=new Competence(comp);
			em.persist(cp);
			return cp;
		}		 
	}

	@Override
	public Technologies checktechno(String tech) {
		Query sql=em.createQuery("select tec from Technologies tec where tec.desctechno LIKE :x");
		sql.setParameter("x", tech);
		Technologies tec=null;
		for (int i = 0; i < sql.getResultList().size(); i++) {
			tec=(Technologies) sql.getResultList().get(i);
		}
		
		if(tec != null){
			return tec;
		}
		else{
			tec=new Technologies(tech);
			em.persist(tec);
			return tec;
		}	
	}

	@Override
	public ColCompTechno addColCompTechno(long col, long comp, long techno,String levl) {
		// TODO Auto-generated method stub
		Competence cp=em.find(Competence.class, comp);
		Technologies tec=em.find(Technologies.class, techno);
		Collaborateurs cl=em.find(Collaborateurs.class, col);
		
		ColCompTechno cct=new ColCompTechno(levl, cl, cp, tec);
		
		cp.getCct().add(cct);
		cl.getComptechno().add(cct);
		tec.getCct().add(cct);
		
		cct.setColab(cl);
		cct.setTechno(tec);
		cct.setComp(cp);
		
		em.persist(cct);
		
		return cct;
	}

	@Override
	public void colabDiplomes(long cl, long dp) {
		// TODO Auto-generated method stub
		Collaborateurs col=em.find(Collaborateurs.class, cl);
		Diplomes dip=em.find(Diplomes.class, dp);
		//System.out.println("collaborateur "+col.getNom());
		/*cola
		col.getDiplome().add(dip);
		dip.getCols().add(col);*/
		ColabDips cldp=new ColabDips();
		if(col == null || dip == null || cldp == null){
			System.out.println("walo wlooo");
			//System.exit(0);
		}
		else {
			cldp.setCol(col);
			cldp.setDip(dip);
			
			dip.getCols().add(cldp);
			col.getCldp().add(cldp);
			
			em.persist(cldp);

		}

	}

	@Override
	public Diplomes addDiplomat(String titre, String promotion, String ecole,
			String typediplome, String typecole, String niveau) {
		Diplomes dip=new Diplomes(titre, promotion, ecole, typediplome, typecole, niveau);
		
		em.persist(dip);
		
		return dip;
	}
	
	@Override
	public Compte ajouterCompte(long col, String log, String pwd, String profil) {
		// TODO Auto-generated method stub
		Collaborateurs cl=em.find(Collaborateurs.class, col);
		
		Compte cpt=new Compte(log, pwd, profil);
		cpt.setStatus("in");
		
		System.out.println("notre compte "+cpt.getUsername());
		cl.setProfil(cpt);
		
		//em.persist(cl);
		em.persist(cpt);
 
		
		return cpt;
	}



	@Override
	public int supprimerCol(long id) {
		// TODO Auto-generated method stub
		Collaborateurs col=em.find(Collaborateurs.class, id);
		em.remove(col);
		return 0;
	}



	@Override
	public int editerManager(long id,Collaborateurs mng,List<String> ltechcomp,List<String> dips,long ibu,long ist,String log,String pwd,String prof){
		 
		int n=0;
		System.out.println("Mng Recuperer "+mng);
		
		Collaborateurs colac=em.find(Collaborateurs.class, id);
		
		int mat = colac.getMatricule();
		String login = colac.getProfil().getUsername();

		if(this.checkMatricuke(mng.getMatricule()) || mat == mng.getMatricule()){
			if(this.checkLogin(log) || login.equals(log)){
				
				List<Technologies> ltechno=new ArrayList<Technologies>();
				List<Competence> lcomp=new ArrayList<Competence>();
				
				List<String> llevls=new ArrayList<String>();
				List<Diplomes> ldips=new ArrayList<Diplomes>();
				
				System.out.println("taille techno "+ltechno.size());
				System.out.println("taille comp "+lcomp.size());
				
				for(String val:ltechcomp){
					ltechno.add(this.checktechno(val.split(",")[0]));
					lcomp.add(this.checkcomp(val.split(",")[1]));
					llevls.add(val.split(",")[2]);
				}

				for(String dp:dips){
					ldips.add(this.addDiplomat(dp.split(",")[0], dp.split(",")[4], 
							dp.split(",")[0], dp.split(",")[2], 
							dp.split(",")[3], dp.split(",")[1]));

				}
				
				System.out.println("************** Debut set colac **************");
				colac.setMatricule(mng.getMatricule());
				colac.setNom(mng.getNom());
				colac.setPrenom(mng.getPrenom());
				colac.setAbreviation(mng.getAbreviation());
				colac.setSexe(mng.getSexe());
				colac.setDateembauche(mng.getDateembauche());
				colac.setMoisBAP(mng.getMoisBAP());
				colac.setParticipeseminaire(mng.getParticipeseminaire());
				colac.setDateparticipeseminaire(mng.getDateparticipeseminaire());
				colac.setSalaireactuel(mng.getSalaireactuel());
				colac.setPosttravail(mng.getPosttravail());
				colac.setEmail(mng.getEmail());

				colac.setBu(em.find(BusinessUnite.class, ibu));
				colac.setSite(em.find(Site.class, ist));
				
				System.out.println("************** Colac rempli **************"+colac);
				System.out.println("************** Fin set colac **************");
				

				for(Diplomes dps:ldips){
					//rh.getDiplome().add(dps);
					//dps.getCols().add(rh);
					System.out.println("code dip "+dps.getTitre()+ "code rh "+colac.getCodecol());
					this.colabDiplomes(colac.getCodecol(), dps.getCodedip());
				}

				for (int i = 0; i < llevls.size(); i++) {
					System.out.println("code techno comp "+lcomp.get(i).getdicrpt()+ ltechno.get(i).getDesctechno());
					this.addColCompTechno(colac.getCodecol(), lcomp.get(i).getCodecompt(), ltechno.get(i).getCodetechno(), llevls.get(i));
				}

				
				colac.getProfil().setUsername(log);
				colac.getProfil().setPassword(pwd);
				this.addsalhisto(colac.getCodecol(), colac.getSalaireactuel(), "");
				
				em.persist(colac);

				System.out.println("code RH 9bal "+colac.getCodecol());
				
			}else{
				n=2;
			}
		}
		else{
			n=1;
		}
		return n;
	}
	

	@Override
	public void addsalhisto(long idcol, double nsal ,String dt) {
		// TODO Auto-generated method stub
		Collaborateurs col=em.find(Collaborateurs.class, idcol);
		System.out.println("voila notre collaborateur "+col.getNom());
	 
			System.out.println("bedua bedua");
			
			HistoriqueSalPostTravail histo=new HistoriqueSalPostTravail(nsal,dt, "");
			
			col.getHistoriquesal().add(histo);
			histo.setColab(col);
			
			System.out.println("voila historique "+histo.getHsalaire());
			
			em.persist(histo);
			System.out.println("fin de persistance");
		 
		
	}


	@Override
	public Collaborateurs consulterCollaborateurs(long id) {
		// TODO Auto-generated method stub
		return em.find(Collaborateurs.class, id);
	}



	@Override
	public List<String> listDiplomes(long id) {
		// TODO Auto-generated method stub
		Query sql=em.createQuery("select cdp from ColabDips cdp where cdp.col.codecol = :x");
		sql.setParameter("x", id);
		List<String> diplo=new ArrayList<>();
		ColabDips cdp=null;
		for (int i = 0; i < sql.getResultList().size(); i++) {
			cdp=(ColabDips) sql.getResultList().get(i);
			diplo.add(cdp.getDip().getTitre()+","+cdp.getDip().getNiveau()+","+cdp.getDip().getTypediplome()+","+cdp.getDip().getTypecole()+","+cdp.getDip().getPromotion());
		}
		
		return diplo;
	}



	@Override
	public List<String> listTechno(long id) {
		Query sql=em.createQuery("select cdp from ColCompTechno cdp where cdp.colab.codecol = :x");
		sql.setParameter("x", id);
		List<String> techno=new ArrayList<>();
		ColCompTechno cdp=null;
		for (int i = 0; i < sql.getResultList().size(); i++) {
			cdp=(ColCompTechno) sql.getResultList().get(i);
			techno.add(cdp.getTechno().getDesctechno()+","+cdp.getComp().getdicrpt()+","+cdp.getLevel());
		}
		
		return techno;
	}
	

}
