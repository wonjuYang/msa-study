package com.msa.controller.dto;

public class PostDto {
	private String title;
	private String content;
	
	public PostDto() {
		super();
	}
	
	public PostDto(String title, String content) {
		super();
		this.title = title;
		this.content = content;
	}
	
	public String getTitle() {
		return title;
	}
	
	public void setTitle(String title) {
		this.title = title;
	}
	
	public String getContent() {
		return content;
	}
	
	public void setContent(String content) {
		this.content = content;
	}
	
}
