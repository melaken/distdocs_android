package com.example.distdocs.entities;

import java.io.InputStream;
import java.sql.Timestamp;

public class DocsAchetes {

	private int docId;
	private InputStream cover;
	private InputStream document;
	private String premiere_couverture;
	private Timestamp lastUpdate;


//	public DocsAchetes(long docId, String premiere_couverture, Timestamp lastUpdate) {
//		this.docId = docId;
//		this.cover = cover;
//		this.document = document;
//		this.premiere_couverture = premiere_couverture;
//		this.lastUpdate = lastUpdate;
//	}

	public String toString() {
		return " docId = "+docId;
	}

	public String getPremiere_couverture() {
		return premiere_couverture;
	}

	public void setPremiere_couverture(String premiere_couverture) {
		this.premiere_couverture = premiere_couverture;
	}

	public int getDocId() {
		return docId;
	}
	public void setDocId(int docId) {
		this.docId = docId;
	}
	public Timestamp getLastUpdate() {
		return lastUpdate;
	}

	public void setLastUpdate(Timestamp lastUpdate) {
		this.lastUpdate = lastUpdate;
	}

	public InputStream getCover() {
		return cover;
	}

	public void setCover(InputStream cover) {
		this.cover = cover;
	}

	public InputStream getDocument() {
		return document;
	}

	public void setDocument(InputStream document) {
		this.document = document;
	}
}
