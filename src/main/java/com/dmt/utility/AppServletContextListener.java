package com.dmt.utility;

import java.util.concurrent.ScheduledExecutorService;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

public class AppServletContextListener implements ServletContextListener{
	 
	public void contextDestroyed(ServletContextEvent servletContextEvent) 
	{
		System.out.println("Exiting system... ...");
		ServletContext application = servletContextEvent.getServletContext();
		System.out.println("Shutting down all threads");
		ScheduledExecutorService scheduledThreadPool = (ScheduledExecutorService)application.getAttribute("scheduledThreadPool");
		scheduledThreadPool.shutdownNow();
		System.out.println("Shutting down all threads finished");
	}
 
        //Run this before web application is started
	public void contextInitialized(ServletContextEvent arg0) {
		System.out.println("ServletContextListener started");	
	}


}
