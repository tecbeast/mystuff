package com.balancedbytes.mystuff;

import java.util.SortedMap;
import java.util.TreeMap;

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
	
	public int getCount() {
		return count;
	}
	
	public void setCount(int count) {
		this.count = count;
	}
	
	public void incCount() {
		this.count++;
	}
	
	public boolean isOnPage() {
		if ((page == 0) || (pageSize == 0)) {
			return true;
		}
		return (count > lowerLimit()) && (count <= upperLimit());
	}
	
	private int lowerLimit() {
		return Math.max((page - 1) * pageSize, 0);
	}
	
	private int upperLimit() {
		return page * pageSize;
	}
	
	public int pagesTotal() {
		int pages = 0;
		if ((pageSize > 0) && (count > 0)) {
			pages = count / pageSize;
			if (count % pageSize > 0) {
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
		if (page > 0) {
			map.put("page", page);
		}
		if (pageSize > 0) {
			map.put("pageSize", pageSize);
		}
		return map;
	}

	public RestDataPaging createStartPage() {
		if ((page == 0) || (pageSize == 0) || (pagesTotal() <= 1)) {
			return null;
		}
		RestDataPaging startPage = new RestDataPaging();
		startPage.setCount(count);
		startPage.setPageSize(pageSize);
		startPage.setPage(1);
		return startPage;
	}

	public RestDataPaging createPrevPage() {
		if ((page < 2) || (pageSize == 0)) {
			return null;
		}
		RestDataPaging prevPage = new RestDataPaging();
		prevPage.setCount(count);
		prevPage.setPageSize(pageSize);
		prevPage.setPage(page - 1);
		return prevPage;
	}

	public RestDataPaging createNextPage() {
		if ((page == 0) || (pageSize == 0) || (count <= upperLimit())) {
			return null;
		}
		RestDataPaging nextPage = new RestDataPaging();
		nextPage.setCount(count);
		nextPage.setPageSize(pageSize);
		nextPage.setPage(page + 1);
		return nextPage;
	}

	public RestDataPaging createEndPage() {
		if ((page == 0) || (pageSize == 0) || (pagesTotal() <= 1)) {
			return null;
		}
		RestDataPaging endPage = new RestDataPaging();
		endPage.setCount(count);
		endPage.setPageSize(pageSize);
		endPage.setPage(pagesTotal());
		return endPage;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + count;
		result = prime * result + page;
		result = prime * result + pageSize;
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
