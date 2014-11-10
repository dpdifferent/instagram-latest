<%@ page import="com.dmt.utility.Constants" %>
<%@ page import="com.dmt.utility.Utils" %>

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

<%@ page import="java.util.concurrent.ExecutorService"%>
<%@ page import="java.util.concurrent.Executors"%>
<%@ page import="java.util.concurrent.ScheduledExecutorService"%>
<%@ page import="java.util.concurrent.TimeUnit"%>

<%@ page import="com.dmt.spreadsheet.SpreadSheetHandler" %>
<%@ page import="com.dmt.executor.TransporterWorker" %>
<%@ page import="com.dmt.executor.AllPostTransporterWorker" %>

<%@ page import="com.sachinhandiekar.examples.InstagramUtils" %>
<%@ page import="java.util.Properties" %>


<%

ScheduledExecutorService scheduledThreadPool = (ScheduledExecutorService)application.getAttribute("scheduledThreadPool");//Executors.newScheduledThreadPool(50);


if(request.getParameter("Start") != null && request.getParameter("Start").equalsIgnoreCase("Start"))
{	
	System.out.println("posting data ...");
	Object objInstagram = session.getAttribute(Constants.INSTAGRAM_OBJECT);
	
	Properties properties = InstagramUtils.getConfigProperties();
	int delay = Utils.parseInt(properties.getProperty(Constants.DELAY));
	
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
    
    InstagramHandler instagramHandler = new InstagramHandler();
	
	SpreadSheetHandler spreadSheethandler = new SpreadSheetHandler();
	
	ArrayList<String> listWorkSheetName = (ArrayList<String>)spreadSheethandler.getWorkSheetNameList(Constants.SPREADSHEET_NAME);
	if(listWorkSheetName != null && listWorkSheetName.size()>0)
	{
		for(String workSheetName:listWorkSheetName)
		{
			if(workSheetName.indexOf("*")<=0)
			{
				int noOfRows = spreadSheethandler.getNoOfRows(Constants.SPREADSHEET_NAME, workSheetName);
				if(noOfRows<=0)
				{
					AllPostTransporterWorker allPostTransporterWorker = new AllPostTransporterWorker(instagram, spreadSheethandler, workSheetName);
					Thread tobj =new Thread(allPostTransporterWorker);  
				    tobj.start();
				}
				TransporterWorker transporterWorker = new TransporterWorker(instagram, spreadSheethandler, workSheetName);
	            scheduledThreadPool.scheduleWithFixedDelay(transporterWorker, 0, delay,TimeUnit.MINUTES);
			}
		}
	}
	
}
else if(request.getParameter("Stop") != null && request.getParameter("Stop").equalsIgnoreCase("Stop"))
{
	application.setAttribute("status", Constants.STATUS_OFF);
	scheduledThreadPool.shutdown();
}

String status = (String)application.getAttribute("status");
String disabled = "";
String statusText = "";

%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta name="description" content="">
    <meta name="author" content="">
    <title>Instragram History Saver</title>
    <link href="css/bootstrap.min.css" rel="stylesheet">
    <link href="css/font-awesome.min.css" rel="stylesheet">
    <link href="css/prettyPhoto.css" rel="stylesheet">
    <link href="css/price-range.css" rel="stylesheet">
    <link href="css/animate.css" rel="stylesheet">
	<link href="css/main.css" rel="stylesheet">
	<link href="css/responsive.css" rel="stylesheet">
    <!--[if lt IE 9]>
    <script src="js/html5shiv.js"></script>
    <script src="js/respond.min.js"></script>
    <![endif]-->       
    <link rel="shortcut icon" href="images/ico/favicon.ico">
    <link rel="apple-touch-icon-precomposed" sizes="144x144" href="images/ico/apple-touch-icon-144-precomposed.png">
    <link rel="apple-touch-icon-precomposed" sizes="114x114" href="images/ico/apple-touch-icon-114-precomposed.png">
    <link rel="apple-touch-icon-precomposed" sizes="72x72" href="images/ico/apple-touch-icon-72-precomposed.png">
    <link rel="apple-touch-icon-precomposed" href="images/ico/apple-touch-icon-57-precomposed.png">
</head><!--/head-->
<body>
	<header id="header"><!--header-->
		<div class="header_top"><!--header_top-->
			<div class="container">
				<div class="row">
					<div class="col-sm-6">
						<div class="contactinfo">
							<ul class="nav nav-pills">
								<li><a href=""> </a></li>
								<li><a href=""></a></li>
							</ul>
						</div>
					</div>
					<div class="col-sm-6">
						<div class="social-icons pull-right">
							<!--<ul class="nav navbar-nav">
								<li><a href=""><i class="fa fa-facebook"></i></a></li>
								<li><a href=""><i class="fa fa-twitter"></i></a></li>
								<li><a href=""><i class="fa fa-linkedin"></i></a></li>
								<li><a href=""><i class="fa fa-dribbble"></i></a></li>
								<li><a href=""><i class="fa fa-google-plus"></i></a></li>
							</ul> -->
						</div>
					</div>
				</div>
			</div>
		</div><!--/header_top-->
		<div class="header-middle"><!--header-middle-->
			<div class="container">
				<div class="row">
					<div class="col-sm-4">
						<div class="logo pull-left">
							<a href="index.html"><img src="images/home/logo.png" alt="" /></a>
						</div>
					</div>
				</div>
			</div>
		</div><!--/header-middle-->
	</header><!--/header-->
	<section id="form"><!--form-->
		<div class="container">
			<div class="row" align="center">
				<div class="col-sm-4 col-sm-offset-1">
					<div class="login-form"><!--login form-->
							<form action="#">
							<%if(status.equalsIgnoreCase(Constants.STATUS_ON)) disabled="disabled"; else disabled=""; %>
							<button type="submit" name="Start" value="Start" class="btn btn-default" <%= disabled %>>Start</button>
						</form>
					</div><!--/login form-->
				</div>
				<div class="col-sm-1">
					<%if(status.equalsIgnoreCase(Constants.STATUS_ON)) statusText="Running........."; else statusText="Stopped"; %>
					<p class="pere" id="statusText"><%= statusText%></p>
				</div>
				<div class="col-sm-4">
					<div class="signup-form"><!--sign up form-->
							<form action="#">
							<%if(status.equalsIgnoreCase(Constants.STATUS_OFF)) disabled="disabled"; else disabled=""; %>
							<button type="submit" name="Stop" value="Stop" class="btn btn-default" <%= disabled %> style="margin-top:23px;">Stop</button>
						</form>
					</div><!--/sign up form-->
				</div>
			</div>
		</div>
	</section><!--/form-->
		<div class="footer-bottom">
			<div class="container">
				<div class="row">
					<p class="pull-left"></p>
					<p class="pull-right"><span><a target="_blank" href="#"></a></span></p>
				</div>
			</div>
		</div>
	</footer><!--/Footer-->
</body>
</html>