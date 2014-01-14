package com.sqli.challange.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table
public class Rituls implements Serializable{

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private long idrt;
	private String themert;
	private String descrt;
	private Date datert;
	
	
	@ManyToOne
	@JoinColumn(name="codecol")
	private Collaborateurs col;
	
	 
	
	

	public Rituls(String themert, String descrt, Date datert) {
		super();
		this.themert = themert;
		this.descrt = descrt;
		this.datert = datert;
	}

	public Rituls() {
		super();
		// TODO Auto-generated constructor stub
	}

	public long getIdrt() {
		return idrt;
	}

	public void setIdrt(long idrt) {
		this.idrt = idrt;
	}

	public String getThemert() {
		return themert;
	}

	public void setThemert(String themert) {
		this.themert = themert;
	}

	public String getDescrt() {
		return descrt;
	}

	public void setDescrt(String descrt) {
		this.descrt = descrt;
	}

	public Date getDatert() {
		return datert;
	}

	public void setDatert(Date datert) {
		this.datert = datert;
	}

	public Collaborateurs getCol() {
		return col;
	}

	public void setCol(Collaborateurs col) {
		this.col = col;
	}

	 
	
}
