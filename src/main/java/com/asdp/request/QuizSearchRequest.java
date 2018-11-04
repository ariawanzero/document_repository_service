package com.asdp.request;

public class QuizSearchRequest {
	private String name;
	private String divisi;
	private int page;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDivisi() {
		return divisi;
	}
	public void setDivisi(String divisi) {
		this.divisi = divisi;
	}
	public int getPage() {
		return page;
	}
	public void setPage(int page) {
		this.page = page;
	}
}
