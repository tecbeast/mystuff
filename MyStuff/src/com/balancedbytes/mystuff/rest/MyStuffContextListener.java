package com.balancedbytes.mystuff.rest;

import java.sql.SQLException;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

import org.apache.juli.logging.Log;
import org.apache.juli.logging.LogFactory;

import com.balancedbytes.mystuff.MyStuffException;
import com.balancedbytes.mystuff.MyStuffUtil;
import com.balancedbytes.mystuff.RestDataPaging;
import com.balancedbytes.mystuff.games.data.CountryCache;

@WebListener
public class MyStuffContextListener implements ServletContextListener {
	
	private static final Log _LOG = LogFactory.getLog(MyStuffContextListener.class);

    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
    	_LOG.info("Servlet context initialized");
    	ServletContext servletContext = servletContextEvent.getServletContext();
    	String defaultPageSize = servletContext.getInitParameter("mystuff.defaultPageSize");
    	if (MyStuffUtil.isProvided(defaultPageSize)) {
    		_LOG.info("defaultPageSize=" + defaultPageSize);
    		RestDataPaging.setDefaultPageSize(MyStuffUtil.parseInt(defaultPageSize));
    	}
    	try {
    		CountryCache.init();
    	} catch (SQLException sqlE) {
    		throw new MyStuffException("Error while initializing CountryCache", sqlE);
    	}
    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {
    	_LOG.info("Servlet context destroyed");
    }
    
}
