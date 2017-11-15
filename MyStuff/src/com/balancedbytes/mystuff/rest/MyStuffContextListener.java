package com.balancedbytes.mystuff.rest;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Enumeration;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

import org.apache.juli.logging.Log;
import org.apache.juli.logging.LogFactory;

import com.balancedbytes.mystuff.ConnectionHelper;
import com.balancedbytes.mystuff.MyStuffException;
import com.balancedbytes.mystuff.MyStuffUtil;
import com.balancedbytes.mystuff.games.data.CountryCache;
import com.balancedbytes.mystuff.games.rest.RestDataPaging;

@WebListener
public class MyStuffContextListener implements ServletContextListener {
	
	private static final Log LOG = LogFactory.getLog(MyStuffContextListener.class);

	private static final String SQL_SHUTDOWN = "SHUTDOWN";

    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
    	LOG.info("Servlet context initialized");
    	ServletContext servletContext = servletContextEvent.getServletContext();
    	String pageSize = servletContext.getInitParameter("mystuff.pageSize");
    	if (MyStuffUtil.isProvided(pageSize)) {
    		LOG.info("pageSize=" + pageSize);
    		RestDataPaging.setDefaultPageSize(MyStuffUtil.parseInt(pageSize));
    	}
    	try {
    		CountryCache.init();
    	} catch (SQLException sqlE) {
    		throw new MyStuffException("Error while initializing CountryCache", sqlE);
    	}
    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {
    	LOG.info("Servlet context destroyed");
    	// proper HSQLDB shutdown
        try (Connection c = ConnectionHelper.getConnection()){
            PreparedStatement ps = c.prepareStatement(SQL_SHUTDOWN);
            ps.execute();
		} catch (SQLException e) {
            LOG.error("Error executing DB shutdown", e);
		}
    	// This manually deregisters JDBC driver, which prevents Tomcat from complaining about memory leaks
        Enumeration<Driver> drivers = DriverManager.getDrivers();
        while (drivers.hasMoreElements()) {
            Driver driver = drivers.nextElement();
            try {
                DriverManager.deregisterDriver(driver);
                LOG.info(String.format("deregistering jdbc driver: %s", driver));
            } catch (SQLException e) {
                LOG.error(String.format("Error deregistering driver %s", driver), e);
            }

        }
    }
    
}
