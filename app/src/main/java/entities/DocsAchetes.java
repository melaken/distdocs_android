package entities;

import java.io.InputStream;
import java.sql.Timestamp;

public class DocsAchetes {

	private long docId;
	private byte[] cover;
	private byte[] document;
	private String premiere_couverture;
	private Timestamp lastUpdate;

	public String toString() {
		return " docId = "+docId;
	}

	public String getPremiere_couverture() {
		return premiere_couverture;
	}

	public void setPremiere_couverture(String premiere_couverture) {
		this.premiere_couverture = premiere_couverture;
	}

	public long getDocId() {
		return docId;
	}
	public void setDocId(long docId) {
		this.docId = docId;
	}
	public Timestamp getLastUpdate() {
		return lastUpdate;
	}

	public void setLastUpdate(Timestamp lastUpdate) {
		this.lastUpdate = lastUpdate;
	}

	public byte[] getCover() {
		return cover;
	}

	public void setCover(byte[] cover) {
		this.cover = cover;
	}

	public byte[] getDocument() {
		return document;
	}

	public void setDocument(byte[] document) {
		this.document = document;
	}
}
