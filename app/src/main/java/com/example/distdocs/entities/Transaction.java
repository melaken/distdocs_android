package com.example.distdocs.entities;

import java.sql.Timestamp;


public class Transaction {

	private String reference;
	private Timestamp dateAchat;
	private String telClient;
	private String moyenPaiement;
	private long clientId;
	private String etat;
	private float montant;
	
	public String toString() {
		return "ref = "+reference+" tel = "+telClient+" moyen = "+moyenPaiement+"\n client_id = "+clientId
				+"	etat = "+etat+" montant = "+montant;
	}
	public Timestamp getDateAchat() {
		return dateAchat;
	}
	public void setDateAchat(Timestamp dateAchat) {
		this.dateAchat = dateAchat;
	}
	public String getMoyenPaiement() {
		return moyenPaiement;
	}
	public void setMoyenPaiement(String moyenPaiement) {
		this.moyenPaiement = moyenPaiement;
	}
	public String getReference() {
		return reference;
	}
	public void setReference(String reference) {
		this.reference = reference;
	}
	public String getTelClient() {
		return telClient;
	}
	public void setTelClient(String telClient) {
		this.telClient = telClient;
	}
	public long getClientId() {
		return clientId;
	}
	public void setClientId(long clientId) {
		this.clientId = clientId;
	}
	public String getEtat() {
		return etat;
	}
	public void setEtat(String etat) {
		this.etat = etat;
	}
	public float getMontant() {
		return montant;
	}
	public void setMontant(float montant) {
		this.montant = montant;
	}
}
