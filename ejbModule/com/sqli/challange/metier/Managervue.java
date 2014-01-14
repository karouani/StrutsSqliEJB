package com.sqli.challange.metier;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import com.sqli.challange.entity.Administrateur;
import com.sqli.challange.entity.AmbassadeurRH;
import com.sqli.challange.entity.BusinessUnite;
import com.sqli.challange.entity.ColCompTechno;
import com.sqli.challange.entity.ColabDips;
import com.sqli.challange.entity.Collaborateur;
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
import com.sqli.challange.sessions.IManagerLocal;
import com.sqli.challange.sessions.IManagerRemote;


@Stateless(name="manager")
public class Managervue implements IManagerLocal,IManagerRemote {

	@PersistenceContext(unitName="emsqli")
	private EntityManager em;


	@Override
	public int editermonprofil(long id, Collaborateurs mng,
			List<String> ltechcomp, List<String> dips,
			String log, String pwd) {
		// TODO Auto-generated method stub
		int n=0;
		System.out.println("dkhelna hnaya");

		Collaborateurs colac=em.find(Collaborateurs.class, id);

		if(this.checkMatricuke(mng.getMatricule())){
			if(this.checkLogin(log)){

				List<Technologies> ltechno=new ArrayList<Technologies>();
				List<Competence> lcomp=new ArrayList<Competence>();
				List<String> llevls=new ArrayList<String>();
				List<Diplomes> ldips=new ArrayList<Diplomes>();

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
	public void ajouterManagerRH(Collaborateurs rh,List<String> ltechcomp,List<String> dips,long ibu,long ist,String log,String pwd,String prof) {
		// TODO Auto-generated method stub
		System.out.println("dkhelna hnaya");
		List<Technologies> ltechno=new ArrayList<Technologies>();
		List<Competence> lcomp=new ArrayList<Competence>();
		List<String> llevls=new ArrayList<String>();
		List<Diplomes> ldips=new ArrayList<Diplomes>();

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


		rh.setBu(em.find(BusinessUnite.class, ibu));
		rh.setSite(em.find(Site.class, ist));


		em.persist(rh);

		System.out.println("code RH 9bal "+rh.getCodecol());

		//Collaborateurs crh=em.find(Collaborateurs.class, rh.getCodecol());





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

	}

	@Override
	public void ajouterCollaborateur(Collaborateur col,List<String> ltechcomp,List<String> dips,long ibu,long ist,long idrh) {
		// TODO Auto-generated method stub
		List<Technologies> ltechno=new ArrayList<Technologies>();
		List<Competence> lcomp=new ArrayList<Competence>();
		List<String> llevls=new ArrayList<String>();
		List<Diplomes> ldips=new ArrayList<Diplomes>();

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

		Collaborateurs mrh=em.find(Collaborateurs.class, idrh);

		((Collaborateur)col).setManageractuel((ManagerRH)mrh);

		col.setBu(em.find(BusinessUnite.class, ibu));
		col.setSite(em.find(Site.class, ist));




		em.persist(col);

		long idcol=col.getCodecol();
		Collaborateurs crh=em.find(Collaborateurs.class, idcol);
		System.out.println("new id col "+crh.getCodecol());

		//a voire la date 
		System.out.println("date d'aumbauche "+crh.getDateembauche());
		this.addsalhisto(crh.getCodecol(), crh.getSalaireactuel(), crh.getDateembauche());


		for(Diplomes dps:ldips){
			//rh.getDiplome().add(dps);
			//dps.getCols().add(rh);
			this.colabDiplomes(crh.getCodecol(), dps.getCodedip());
		}

		for (int i = 0; i < llevls.size(); i++) {
			this.addColCompTechno(crh.getCodecol(), lcomp.get(i).getCodecompt(), ltechno.get(i).getCodetechno(), llevls.get(i));
		}



	}

	@Override
	public void editerCollaborateur(long idcol, String post, double sal,long idmanagerrh,String dt) {
		// TODO Auto-generated method stub
		Collaborateurs col=em.find(Collaborateurs.class, idcol);

		System.out.println("bdina bdina");
		col.setPosttravail(post);
		col.setSalaireactuel(sal);

		ManagerRH mngac=(ManagerRH)em.find(Collaborateurs.class, idmanagerrh);
		ManagerRH mnganc=((Collaborateur)col).getManageractuel();

		((Collaborateur)col).setManagerancien(mnganc);
		((Collaborateur)col).setManageractuel(mngac);


		this.addsalhisto(idcol, sal, "02/04/2013");

	}



	@Override
	public void desactiverMRHCols(long idmrh, List<Long> listcol) {
		// TODO Auto-generated method stub
		for(long col:listcol){
			this.deaffecterRhCol(col,idmrh);
		}
	}

	@Override
	public List<Collaborateur> consulterlistCollaborateur() {
		List<Collaborateur> cols=new ArrayList<Collaborateur>();
		Query sql=em.createQuery("select mg from Collaborateurs mg");

		for (int i = 0; i < sql.getResultList().size(); i++) {

			if(sql.getResultList().get(i) instanceof Collaborateur){
				cols.add((Collaborateur) sql.getResultList().get(i));
			}
		}
		return cols;

	}

	@Override
	public List<ManagerRH> consulterlistManagerRH() {
		List<ManagerRH> rh=new ArrayList<ManagerRH>();
		Query sql=em.createQuery("select mg from Collaborateurs mg");

		for (int i = 0; i < sql.getResultList().size(); i++) {

			if(sql.getResultList().get(i) instanceof ManagerRH){
				rh.add((ManagerRH) sql.getResultList().get(i));
			}
		}
		return rh;

	}

	@Override
	public Collaborateur consulterColab(long id) {
		// TODO Auto-generated method stub
		return em.find(Collaborateur.class, id);
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
		Collaborateurs cl=this.consulterColab(col);

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
	public List<Collaborateur> consulterlistCollabRH(long rh) {
		// TODO Auto-generated method stub
		Query sql=em.createQuery("select col from Collaborateur col where col.manageractuel.codecol = :x");
		sql.setParameter("x", rh);

		return sql.getResultList();
	}

	@Override
	public void affecterRhCol(long cl, long rh) {
		Collaborateur col=(Collaborateur) em.find(Collaborateurs.class, cl);
		ManagerRH mngac=(ManagerRH)em.find(Collaborateurs.class, rh);
		ManagerRH mnganc=((Collaborateur)col).getManageractuel();

		if(mnganc != null){
			if( mngac.getCodecol() != mnganc.getCodecol() ){
				((Collaborateur)col).setManagerancien(mnganc);
				((Collaborateur)col).setManageractuel(mngac);
			}
		}
		else {
			((Collaborateur)col).setManageractuel(mngac);
		}
		em.persist(col);
	}

	@Override
	public void deaffecterRhCol(long cl, long rh) {
		// TODO Auto-generated method stub
		Collaborateur col=(Collaborateur) em.find(Collaborateurs.class, cl);
		ManagerRH mnganc=(ManagerRH)em.find(Collaborateurs.class, rh);
		//ManagerRH mnganc=((Collaborateur)col).getManageractuel();


		((Collaborateur)col).setManagerancien(mnganc);
		((Collaborateur)col).setManageractuel(null);
		em.persist(col);
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
	public List<Collaborateur> consulterlistCollabNRH(long rh) {
		Query sql=em.createQuery("select col from Collaborateur col where col.manageractuel.codecol != :x");
		sql.setParameter("x", rh);

		return sql.getResultList();
	}


	@Override
	public String parseDate(Date d) {
		String a = d+"";
		String jour = a.split(" ")[2];
		String mois = a.split(" ")[1];
		String anne = a.split(" ")[5];


		String mm;
		switch (mois) {
		case "Jan": mm = "01";
		break;
		case "Feb":mm = "02";
		break;
		case "Mar":mm = "03";
		break;
		case "Apr":mm = "04";
		break;
		case "May":mm = "05";
		break;
		case "Jun":mm = "06";
		break;
		case "Jul":mm = "07";
		break;
		case "Aug":mm = "08";
		break;
		case "Sep":mm = "09";
		break;
		case "Oct":mm = "10";
		break;
		case "Nov":mm = "11";
		break;
		case "Dec":mm = "12";
		break;
		default: mm = "1";
		break;
		}

		return jour+"/"+mm+"/"+anne;
	}

	@Override
	public int editermyprofile(long idadm, String nom, String prenom,
			String email, String sexe, String username, String password) {
		// TODO Auto-generated method stub
		Collaborateurs col=em.find(Collaborateurs.class, idadm);
		int n=0;
		if(this.checkLogin(username) || username.equals(col.getProfil().getUsername())){
			col.setNom(nom);
			col.setPrenom(prenom);
			col.setEmail(email);
			col.setSexe(sexe);
			col.getProfil().setUsername(username);
			col.getProfil().setPassword(password);
			n=1;
		}
		return n;
	}

	@Override
	public boolean checkrhcol(long rh, long col) {
		Query sql=em.createQuery("select col from Collaborateur col where col.manageractuel.codecol = :x and col.codecol = :y ");
		sql.setParameter("x", rh);
		sql.setParameter("y", col);
		if(sql.getResultList().size() == 0)return true;
		return false;
	}


	@Override
	public void activerMRHCols(long idmrh, List<Long> listcol) {

		System.out.println("Hana jite");
		List<Long> colsact=new ArrayList<Long>();

		for(Collaborateur col:consulterlistCollabRH(idmrh)){
			colsact.add(col.getCodecol());
		}

		for(Long id:listcol){
			if(checkrhcol(idmrh, id)){
				this.affecterRhCol(id,idmrh);
			}
		}

		for(Long id:colsact){
			if(!listcol.contains(id)){
				this.deaffecterRhCol(id, idmrh);
			}
		}
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
