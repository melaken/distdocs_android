package entities;

import java.sql.Timestamp;
import java.util.Date;

public class Document {

	private long id;
	private String nom;
	private Timestamp	dateAjout;
	private java.sql.Date	dateParution;
	private String docType;
	private float prix;
	private String numeroEdition;
	private Long editeur;
	private String resume;
	private String premiereCouverture;
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getNom() {
		return nom;
	}
	public void setNom(String nom) {
		this.nom = nom;
	}
	public Timestamp getDateAjout() {
		return dateAjout;
	}
	public void setDateAjout(Timestamp dateAjout) {
		this.dateAjout = dateAjout;
	}
	public Date getDateParution() {
		return dateParution;
	}
	public void setDateParution(java.sql.Date dateParution) {
		this.dateParution = dateParution;
	}
	public String getDocType() {
		return docType;
	}
	public void setDocType(String docType) {
		this.docType = docType;
	}
	public float getPrix() {
		return prix;
	}
	public void setPrix(float prix) {
		this.prix = prix;
	}
	public String getNumeroEdition() {
		return numeroEdition;
	}
	public void setNumeroEdition(String numeroEdition) {
		this.numeroEdition = numeroEdition;
	}
	public Long getEditeur() {
		return editeur;
	}
	public void setEditeur(Long editeur) {
		this.editeur = editeur;
	}
	public String getResume() {
		return resume;
	}
	public void setResume(String resume) {
		this.resume = resume;
	}
	public String getPremiereCouverture() {
		return premiereCouverture;
	}
	public void setPremiereCouverture(String premiereCouverture) {
		this.premiereCouverture = premiereCouverture;
	}
	@Override
	public boolean equals(Object d) {
		if(this.id == ((Document)d).getId())
				return true;
		else return false;
	}
	
}
