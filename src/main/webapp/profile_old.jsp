<jsp:include page="common/header.jsp"/>


<%@ page import="com.dmt.utility.Constants" %>

<%@ page import="java.util.List" %>

<%@ page import="org.jinstagram.Instagram" %>
<%@ page import="org.jinstagram.entity.users.feed.MediaFeedData" %>
<%@ page import="org.jinstagram.entity.users.feed.UserFeed" %>
<%@ page import="org.jinstagram.entity.users.feed.UserFeedData" %>
<%@ page import="org.jinstagram.entity.users.basicinfo.UserInfoData" %>

<%@ page import="java.util.Date"%>
<%@ page import="java.util.LinkedHashMap"%>
<%@ page import="java.util.ArrayList"%>

<%@ page import="java.text.DateFormat"%>
<%@ page import="java.text.SimpleDateFormat"%>

<%@ page import="com.sachinhandiekar.examples.InstagramHandler" %>

<%@ page import="java.util.concurrent.Executors"%>
<%@ page import="java.util.concurrent.ScheduledExecutorService"%>
<%@ page import="java.util.concurrent.TimeUnit"%>

<%@ page import="com.dmt.spreadsheet.SpreadSheetHandler" %>
<%@ page import="com.dmt.executor.TransporterWorker" %>


<%

ScheduledExecutorService scheduledThreadPool = (ScheduledExecutorService)application.getAttribute("scheduledThreadPool");//Executors.newScheduledThreadPool(50);

if(request.getParameter("Start") != null && request.getParameter("Start").equalsIgnoreCase("Start"))
{	
	System.out.println("posting data ...");
	Object objInstagram = session.getAttribute(Constants.INSTAGRAM_OBJECT);
	
	if(scheduledThreadPool.isShutdown())
	{
		scheduledThreadPool = Executors.newScheduledThreadPool(50);
		application.setAttribute("scheduledThreadPool", scheduledThreadPool);
	}	

    application.setAttribute("status", Constants.STATUS_ON);
	Instagram instagram = null;
    

    if (objInstagram != null) 
    {
        instagram = (Instagram) objInstagram;
        System.out.println("instagram = "+instagram);
    }
    else
    {
        response.sendRedirect(request.getContextPath() + "/index.jsp");
        return;
    }
    
    //DataTransporter dataTransporter = new DataTransporter(instagram);
    //scheduledThreadPool.scheduleWithFixedDelay(dataTransporter, 0, 1,TimeUnit.MINUTES);
    InstagramHandler instagramHandler = new InstagramHandler();
	
	SpreadSheetHandler spreadSheethandler = new SpreadSheetHandler();
	
	ArrayList<String> listWorkSheetName = (ArrayList<String>)spreadSheethandler.getWorkSheetNameList(Constants.SPREADSHEET_NAME);
	if(listWorkSheetName != null && listWorkSheetName.size()>0)
	{
		for(String workSheetName:listWorkSheetName)
		{
			TransporterWorker transporterWorker = new TransporterWorker(instagram, spreadSheethandler, workSheetName);
            scheduledThreadPool.scheduleWithFixedDelay(transporterWorker, 0, 2,TimeUnit.MINUTES);
		}
	}
	
}
else if(request.getParameter("Stop") != null && request.getParameter("Stop").equalsIgnoreCase("Stop"))
{
	scheduledThreadPool.shutdown();
}

%>


<!-- Navigation -->
<nav class="navbar navbar-inverse navbar-fixed-top" role="navigation">
    <div class="container">
        <!-- Brand and toggle get grouped for better mobile display -->
       
    </div>
    <!-- /.container -->
</nav>

<!-- Page Content -->
<div class="container">

    <div class="row">

        <div class="col-lg-12">
            <h1 class="page-header">User Profile</h1>
        </div>
         <div class="col-lg-12">
            <form name="postDataForm" method="post" action="#">
            	<input type="submit" name="Start" value="Start">
            	<input type="submit" name="Stop" value="Stop">
            </form>
        </div>
    </div>
    <hr>
</div>
<jsp:include page="common/footer.jsp"/>