package com.balancedbytes.mystuff;

import java.sql.SQLException;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

import org.apache.juli.logging.Log;
import org.apache.juli.logging.LogFactory;

import com.balancedbytes.mystuff.games.CountryCache;

@WebListener
public class MyStuffContextListener implements ServletContextListener {
	
	private static final Log _LOG = LogFactory.getLog(MyStuffContextListener.class);

    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
    	_LOG.info("Servlet context initialized");
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
