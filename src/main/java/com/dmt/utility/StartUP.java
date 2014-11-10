package com.dmt.utility;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

/**
 * Servlet implementation class StartUP
 */
public class StartUP extends HttpServlet 
{
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public StartUP() 
    {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see Servlet#init(ServletConfig)
	 */
	public void init(ServletConfig config) throws ServletException 
	{
		System.out.println("*******************Initialization of Application wide data starts **************************");
		ServletContext application = config.getServletContext();
		
		application.setAttribute("status", Constants.STATUS_OFF);
		System.out.println("setting status = "+application.getAttribute("status"));
		
		ScheduledExecutorService scheduledThreadPool = Executors.newScheduledThreadPool(50);
		application.setAttribute("scheduledThreadPool", scheduledThreadPool);
		System.out.println("setting scheduledThreadPool = "+application.getAttribute("scheduledThreadPool"));
		
		System.out.println("*******************Initialization of Application wide data ends **************************");
	}

}
