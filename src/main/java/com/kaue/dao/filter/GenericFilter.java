package com.kaue.dao.filter;

public abstract class GenericFilter {
			
	private String q;
	
	private Long page;
	
	private Long pageSize;
	
	private boolean paginated;
	
	private String termo;
	
	private boolean avancada = false;

	public String getQ() {
		return q;
	}

	public void setQ(String q) {
		this.q = q;
	}

	public Long getPage() {
		return page;
	}

	public void setPage(Long page) {
		this.page = page;
	}

	public Long getPageSize() {
		return pageSize;
	}

	public void setPageSize(Long pageSize) {
		this.pageSize = pageSize;
	}

	public boolean isPaginated() {
		return paginated;
	}

	public void setPaginated(boolean paginated) {
		this.paginated = paginated;
	}

	public String getTermo() {
		return termo;
	}

	public void setTermo(String termo) {
		this.termo = termo;
	}

	public boolean isAvancada() {
		return avancada;
	}

	public void setAvancada(boolean avancada) {
		this.avancada = avancada;
	}
}
