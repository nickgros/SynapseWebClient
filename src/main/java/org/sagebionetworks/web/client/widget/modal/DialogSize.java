package org.sagebionetworks.web.client.widget.modal;import java.io.Serializable;import com.google.gwt.user.client.rpc.IsSerializable;public enum DialogSize implements Serializable, IsSerializable {	SMALL("300px"), MEDIUM("600px"), LARGE("900px");	private String size;	DialogSize() {}	DialogSize(String size) {		this.size = size;	}	public String getSize() {		return size;	}	public void setSize(String size) {		this.size = size;	}}