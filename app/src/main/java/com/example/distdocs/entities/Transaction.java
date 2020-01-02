package com.example.distdocs.entities;

import java.sql.Timestamp;


public class Transaction {

	private String reference;
	private Timestamp dateAchat;
	private Timestamp lastUpdate;
	private String etat;

	public String toString() {
		return "ref = "+reference +"	etat = "+etat;
	}
	public Timestamp getDateAchat() {
		return dateAchat;
	}
	public void setDateAchat(Timestamp dateAchat) {
		this.dateAchat = dateAchat;
	}

	public String getReference() {
		return reference;
	}
	public void setReference(String reference) {
		this.reference = reference;
	}

	public String getEtat() {
		return etat;
	}
	public void setEtat(String etat) {
		this.etat = etat;
	}

	public Timestamp getLastUpdate() {
		return lastUpdate;
	}

	public void setLastUpdate(Timestamp lastUpdate) {
		this.lastUpdate = lastUpdate;
	}
}
