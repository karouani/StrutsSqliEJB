package com.sqli.challange.metier;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import com.sqli.challange.entity.BusinessUnite;
import com.sqli.challange.entity.ColCompTechno;
import com.sqli.challange.entity.ColabDips;
import com.sqli.challange.entity.Collaborateur;
import com.sqli.challange.entity.Collaborateurs;
import com.sqli.challange.entity.Competence;
import com.sqli.challange.entity.Diplomes;
import com.sqli.challange.entity.ManagerRH;
import com.sqli.challange.entity.Rituls;
import com.sqli.challange.entity.Site;
import com.sqli.challange.entity.Technologies;
import com.sqli.challange.sessions.IManagerRHLocal;
import com.sqli.challange.sessions.IManagerRHRemote;



@Stateless(name="managerrh")
public class ManagerRHvue implements IManagerRHLocal,IManagerRHRemote{

	@PersistenceContext(unitName="emsqli")
	private EntityManager em;
	
	

	@Override
	public void organiserRitules(String sujet,String descp,String dt,long col) {
		Collaborateur cl=(Collaborateur) em.find(Collaborateurs.class, col);
		try {
			SimpleDateFormat sd=new SimpleDateFormat("yyyy-MM-dd");
			Rituls rt=new Rituls(sujet, descp, sd.parse(dt));
			
			rt.setCol(cl);
			cl.getRituls().add(rt);
			
			em.persist(rt);
		} catch (Exception e) {
		}
	}

	@Override
	public List<Collaborateur> consultermescollaborateurs(long id) {
		Query sql=em.createQuery("select col from Collaborateur col where col.manageractuel.codecol = :x");
		sql.setParameter("x", id);
		return sql.getResultList();
	}

	@Override
	public ManagerRH consultermrh(long id) {
		return (ManagerRH)em.find(Collaborateurs.class, id);
	}

	@Override
	public int editermonprofil(long id, Collaborateurs mng,
			List<String> ltechcomp, List<String> dips, String log, String pwd) {
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
	public int editerprofilcollaborateur(long col, Collaborateur mng,
			List<String> ltechcomp, List<String> dips) {
		int n=0;
		System.out.println("dkhelna hnaya");
		
		Collaborateur colac=(Collaborateur)em.find(Collaborateur.class, col);
		
		
				
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

				em.persist(colac);				
		return n;
	}

	@Override
	public Competence checkcomp(String comp) {
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
	public ColCompTechno addColCompTechno(long col, long comp, long techno,
			String levl) {
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
	public boolean checkLogin(String log) {
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
	public Collaborateur consultercolab(long id) {
		return em.find(Collaborateur.class, id);
	}

	@Override
	public void UpdateCollaborateurManagerRhAction(Long id, String nom,
			String prenom, String email, String abreviation, String sexe,
			Long site, Long bu, String participeseminaire,
			String dateparticipeseminaire, List<String> diplome,
			List<String> techno) {
		
		
		List<Technologies> ltechno=new ArrayList<Technologies>();
		List<Competence> lcomp=new ArrayList<Competence>();
		List<String> llevls=new ArrayList<String>();
		List<Diplomes> ldips=new ArrayList<Diplomes>();

		Collaborateur col=this.consultercolab(id);
		System.out.println("Colaborateur desirer a modifier"+col.getNom());
		System.out.println("List Diplome "+diplome.size());
		/*
		for(String val:techno){
			ltechno.add(this.checktechno(val.split(",")[0]));
			lcomp.add(this.checkcomp(val.split(",")[1]));
			llevls.add(val.split(",")[2]);
		}

		for(String dp:diplome){

			ldips.add(this.addDiplomat(dp.split(",")[0], dp.split(",")[4], 
					dp.split(",")[0], dp.split(",")[2], 
					dp.split(",")[3], dp.split(",")[1]));

		}
		
		for(Diplomes dps:ldips){
			this.colabDiplomes(col.getCodecol(), dps.getCodedip());
		}

		for (int i = 0; i < llevls.size(); i++) {
			System.out.println("code techno comp "+lcomp.get(i).getdicrpt()+ ltechno.get(i).getDesctechno());
			this.addColCompTechno(col.getCodecol(), lcomp.get(i).getCodecompt(), ltechno.get(i).getCodetechno(), llevls.get(i));
		}
		
			
			col.setNom(nom);
			col.setPrenom(prenom);
			col.setEmail(email);
			col.setAbreviation(abreviation);
			col.setSexe(sexe);
			col.setDateparticipeseminaire(dateparticipeseminaire);
			col.setParticipeseminaire(participeseminaire);
			
			BusinessUnite bus=em.find(BusinessUnite.class, bu);
			Site sts=em.find(Site.class, site);
			
			col.setBu(bus);
			col.setSite(sts);
			
			em.persist(col);
	*/
	}

}
