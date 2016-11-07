package com.balancedbytes.mystuff;

import java.util.SortedMap;
import java.util.TreeMap;

public class RestDataPaging {

	private int page = 1;       // default
	private int pageSize = 25;  // default
	private int count = 0;
	
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
	
	public RestDataPaging createNext() {
		if ((page == 0) || (pageSize == 0) || (count <= upperLimit())) {
			return null;
		}
		RestDataPaging next = new RestDataPaging();
		next.setCount(count);
		next.setPageSize(pageSize);
		next.setPage(page + 1);
		return next;
	}

	public RestDataPaging createPrev() {
		if ((page == 0) || (pageSize == 0) || (count > lowerLimit())) {
			return null;
		}
		RestDataPaging prev = new RestDataPaging();
		prev.setCount(count);
		prev.setPageSize(pageSize);
		prev.setPage(page - 1);
		return prev;
	}

}
