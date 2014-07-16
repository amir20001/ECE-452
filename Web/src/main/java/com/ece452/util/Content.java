package com.ece452.util;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import com.ece452.domain.Sync;

public class Content {

	private List<String> registration_ids = new ArrayList<String>();
	private Sync data;

	public void addRegId(String regId) {
		registration_ids.add(regId);
	}

	public void setSync(Sync sync) {
		this.setData(sync);
	}

	public List<String> getRegistration_ids() {
		return registration_ids;
	}

	public void setRegistration_ids(List<String> registration_ids) {
		this.registration_ids = registration_ids;
	}

	public Sync getData() {
		return data;
	}

	public void setData(Sync data) {
		this.data = data;
	}
}
