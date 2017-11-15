package com.balancedbytes.mystuff.games.rest;

import java.util.SortedMap;
import java.util.TreeMap;

import com.balancedbytes.mystuff.MyStuffUtil;

public class RestDataPaging {
	
	private static int defaultPageSize = 10;
	
	public static void setDefaultPageSize(int defaultPageSize) {
		RestDataPaging.defaultPageSize = defaultPageSize;
	}

	private int page;
	private int pageSize;
	private int count;
	
	public RestDataPaging() {
		this.page = 1;
		this.count = 0;
		this.pageSize = defaultPageSize;
	}
	
	public int getPage() {
		return this.page;
	}
	
	public void setPage(int page) {
		this.page = page;
	}
	
	public int getPageSize() {
		return this.pageSize;
	}
	
	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}
	
	public int getCount() {
		return this.count;
	}
	
	public void setCount(int count) {
		this.count = count;
	}
	
	public void incCount() {
		this.count++;
	}
	
	public boolean isOnPage() {
		if ((this.page == 0) || (this.pageSize == 0)) {
			return true;
		}
		return (this.count > lowerLimit()) && (this.count <= upperLimit());
	}
	
	private int lowerLimit() {
		return Math.max((this.page - 1) * this.pageSize, 0);
	}
	
	private int upperLimit() {
		return this.page * this.pageSize;
	}
	
	public int pagesTotal() {
		int pages = 0;
		if ((this.pageSize > 0) && (this.count > 0)) {
			pages = this.count / this.pageSize;
			if (this.count % this.pageSize > 0) {
				pages++;
			}
		}
		return pages;
	}

	public void init(String page, String pageSize) {
		if (MyStuffUtil.isProvided(page)) {
			setPage(MyStuffUtil.parseInt(page));
		}
		if (MyStuffUtil.isProvided(pageSize)) {
			setPageSize(MyStuffUtil.parseInt(pageSize));
		}
	}
	
	public SortedMap<String, Object> toSortedMap() {
		SortedMap<String, Object> map = new TreeMap<String, Object>();
		if (this.page > 0) {
			map.put("page", this.page);
		}
		if (this.pageSize > 0) {
			map.put("pageSize", this.pageSize);
		}
		return map;
	}

	public RestDataPaging createStartPage() {
		if ((this.page == 0) || (this.pageSize == 0) || (pagesTotal() <= 1)) {
			return null;
		}
		RestDataPaging startPage = new RestDataPaging();
		startPage.setCount(this.count);
		startPage.setPageSize(this.pageSize);
		startPage.setPage(1);
		return startPage;
	}

	public RestDataPaging createPrevPage() {
		if ((this.page < 2) || (this.pageSize == 0)) {
			return null;
		}
		RestDataPaging prevPage = new RestDataPaging();
		prevPage.setCount(this.count);
		prevPage.setPageSize(this.pageSize);
		prevPage.setPage(this.page - 1);
		return prevPage;
	}

	public RestDataPaging createNextPage() {
		if ((this.page == 0) || (this.pageSize == 0) || (this.count <= upperLimit())) {
			return null;
		}
		RestDataPaging nextPage = new RestDataPaging();
		nextPage.setCount(this.count);
		nextPage.setPageSize(this.pageSize);
		nextPage.setPage(this.page + 1);
		return nextPage;
	}

	public RestDataPaging createEndPage() {
		if ((this.page == 0) || (this.pageSize == 0) || (pagesTotal() <= 1)) {
			return null;
		}
		RestDataPaging endPage = new RestDataPaging();
		endPage.setCount(this.count);
		endPage.setPageSize(this.pageSize);
		endPage.setPage(pagesTotal());
		return endPage;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + this.count;
		result = prime * result + this.page;
		result = prime * result + this.pageSize;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		RestDataPaging other = (RestDataPaging) obj;
		if (count != other.count)
			return false;
		if (page != other.page)
			return false;
		if (pageSize != other.pageSize)
			return false;
		return true;
	}

}
