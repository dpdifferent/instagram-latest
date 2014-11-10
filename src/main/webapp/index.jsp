<%@ page import="org.jinstagram.auth.oauth.InstagramService" %>
<%@ page import="org.jinstagram.auth.InstagramAuthService" %>
<%@ page import="com.sachinhandiekar.examples.InstagramUtils" %>
<%@ page import="com.dmt.utility.Constants" %>

<%@ page import="java.util.Properties" %>

<%
    // If we already have an instagram object, then redirect to profile.jsp
    Object objInstagram = session.getAttribute(Constants.INSTAGRAM_OBJECT);
    if (objInstagram != null) {
        response.sendRedirect(request.getContextPath() + "/profile.jsp");
    }

    Properties properties = InstagramUtils.getConfigProperties();

    String clientId = properties.getProperty(Constants.CLIENT_ID);
    String clientSecret = properties.getProperty(Constants.CLIENT_SECRET);
    String callbackUrl = properties.getProperty(Constants.REDIRECT_URI);


    InstagramService service = new InstagramAuthService()
            .apiKey(clientId)
            .apiSecret(clientSecret)
            .callback(callbackUrl)
            .build();

    String authorizationUrl = service.getAuthorizationUrl(null);

    session.setAttribute(Constants.INSTAGRAM_SERVICE, service);
%>

<!DOCTYPE html>
<html lang="en"><head>
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
							<a href="index.jsp"><img src="images/home/logo.png" alt="" /></a>
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
							<a class="btn btn-default btn2" href="<%= authorizationUrl%>" style="background-color:#FFFFFF;"><img src="images/y4aiho5e.png" height="40"></a>
						</form>
				</div><!--/login form-->
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