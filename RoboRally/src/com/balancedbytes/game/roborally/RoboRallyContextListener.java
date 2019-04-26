package com.balancedbytes.game.roborally;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Enumeration;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.balancedbytes.game.roborally.db.RoboRallyDb;

@WebListener
public class RoboRallyContextListener implements ServletContextListener {
	
	private static final Logger LOG = LogManager.getLogger(RoboRallyContextListener.class);

    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
    	LOG.info("Servlet context initialized");
        try {
            RoboRallyDb.init();
		} catch (SQLException e) {
            LOG.error("Error initializing DB", e);
		}
    	/*
    	ServletContext servletContext = servletContextEvent.getServletContext();
    	String pageSize = servletContext.getInitParameter("mystuff.pageSize");
    	if (MyStuffUtil.isProvided(pageSize)) {
    		LOG.info("pageSize=" + pageSize);
    		RestDataPaging.setDefaultPageSize(MyStuffUtil.parseInt(pageSize));
    	}
    	*/
    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {
    	LOG.info("Servlet context destroyed");
    	// proper HSQLDB shutdown
        try (Connection c = RoboRallyDb.getConnection()){
            PreparedStatement ps = c.prepareStatement("SHUTDOWN");
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
