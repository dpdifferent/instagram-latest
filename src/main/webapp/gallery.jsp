<jsp:include page="common/header.jsp"/>


<%@ page import="com.sachinhandiekar.examples.Constants" %>

<%@ page import="org.jinstagram.Instagram" %>
<%@ page import="org.jinstagram.entity.users.feed.MediaFeedData" %>
<%@ page import="java.util.List" %>
<%@ page import="com.sachinhandiekar.examples.AppHandler" %>
<%@ page import="com.sachinhandiekar.model.AppUser" %>
<%@ page import="org.jinstagram.entity.users.feed.UserFeed" %>
<%@ page import="org.jinstagram.entity.users.feed.UserFeedData" %>



<%

    Object objInstagram = session.getAttribute(Constants.INSTAGRAM_OBJECT);

    Instagram instagram = null;

    if (objInstagram != null) {
        instagram = (Instagram) objInstagram;
    } else {
        response.sendRedirect(request.getContextPath() + "/index.jsp");
        return;
    }
	String query = "healing_belle";
   	UserFeed userFeed = instagram.searchUser(query);
   	List<UserFeedData> userList = userFeed.getUserList();
	long userId2 = userList.get(0).getId();


%>
<!-- Navigation -->
<nav class="navbar navbar-inverse navbar-fixed-top" role="navigation">
    <div class="container">
        <!-- Brand and toggle get grouped for better mobile display -->
        <div class="navbar-header">
            <button type="button" class="navbar-toggle" data-toggle="collapse"
                    data-target="#bs-example-navbar-collapse-1">
                <span class="sr-only">Toggle navigation</span>
                <span class="icon-bar"></span>
                <span class="icon-bar"></span>
                <span class="icon-bar"></span>
            </button>
            <a class="navbar-brand" href="#">jInstagram</a>
        </div>
        <div class="collapse navbar-collapse">
            <ul class="nav navbar-nav">
                <li><a href="profile.jsp">Profile</a></li>
                <li class="active"><a href="gallery.jsp">Gallery</a></li>
                <li><a href="popular.jsp">Popular</a></li>
                <li><a href="search.jsp">Search</a></li>
                <li><a href="logout.jsp">Logout</a></li>

            </ul>
        </div>
    </div>
    <!-- /.container -->
</nav>

<!-- Page Content -->
<div class="container">

    <div class="row">

        <div class="col-lg-12">
            <h1 class="page-header">Gallery</h1>
        </div>
        <%
            List<MediaFeedData> mediaList = instagram.getUserFeeds().getData();
       		AppHandler handler = new AppHandler();

        %>
		
        <h3>Media Count :  <%=mediaList.size()%>
        </h3>

        <%
            for (MediaFeedData mediaFeedData : mediaList) 
            {
        %>
        <div class="col-lg-3 col-md-4 col-xs-6 thumb">
            <a class="thumbnail" href="#">
                <img class="img-responsive" src="<%=mediaFeedData.getImages().getLowResolution().getImageUrl()%>"
                     alt="">
            </a>
        </div>

        <%
            }
        %>

	
	<div>

		<select name = "userPosts" value="userUpdates2">
		<option value="none">--Select--</option>
       <%
			List<AppUser> userUpdates = handler.getUserUpdates(instagram, userId2);
			for(int i=0; i<userUpdates.size(); i++)
			{
       %>
       <optionsCollection name="InputForm" property="stateList">
       		<option value ="<%= userUpdates.get(i).getPostUrl() %>"></option>
       </select>
        <%
            }
        %>
       
	</div>
    </div>

    <hr>


<jsp:include page="common/footer.jsp"/>