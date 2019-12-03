package com.example.distdocs.entities;

import android.graphics.Bitmap;

import java.io.InputStream;
import java.sql.Timestamp;

public class DocsAchetes {

	private int docId;
	private InputStream cover;
	private InputStream document;
	private String premiere_couverture;
	private Timestamp lastUpdate;
	private Bitmap bitmapCover;
	private Bitmap bitmapDoc;


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

	public Bitmap getBitmapCover() {
		return bitmapCover;
	}

	public void setBitmapCover(Bitmap bitmapCover) {
		this.bitmapCover = bitmapCover;
	}

	public Bitmap getBitmapDoc() {
		return bitmapDoc;
	}

	public void setBitmapDoc(Bitmap bitmapDoc) {
		this.bitmapDoc = bitmapDoc;
	}
}
