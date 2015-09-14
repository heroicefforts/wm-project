package com.acme.reservations.web.json;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;


public class PagedResponse {
	private int status;
	
	private int page;
	private int pageSize;
	
	@JsonInclude(Include.NON_NULL)
	private String nextPage;
	
	@JsonInclude(Include.NON_NULL)
	private String prevPage;
	
	private List<? extends Payload> data;

	
	public PagedResponse(int page, int pageSize, List<? extends Payload> data) {
		this.page = page;
		this.pageSize = pageSize;
		this.data = data;
	}
	
	public int getPage() {
		return page;
	}
	
	public void setPage(int page) {
		this.page = page;
	}
	
	public int getPageSize() {
		return pageSize;
	}
	
	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}
	
	public String getNextPage() {
		return nextPage;
	}

	public void setNextPage(String nextPage) {
		this.nextPage = nextPage;
	}

	public String getPrevPage() {
		return prevPage;
	}

	public void setPrevPage(String prevPage) {
		this.prevPage = prevPage;
	}

	public List<? extends Payload> getData() {
		return data;
	}

	public void setData(List<? extends Payload> data) {
		this.data = data;
	}
	
	public int getStatus() {
		return status;
	}

}
