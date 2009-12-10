<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">

<head>
  <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
  <link rel="stylesheet" href="<spring:url value="/css/swagswap.css" htmlEscape="true" />" type="text/css"/>
  <link rel="stylesheet" href="<spring:url value="/css/maven-base.css" htmlEscape="true" />" type="text/css"/>
  <link rel="stylesheet" href="<spring:url value="/css/maven-theme.css" htmlEscape="true" />" type="text/css"/>
  <link rel="stylesheet" href="<spring:url value="/css/site.css" htmlEscape="true" />" type="text/css"/>
  <link rel="stylesheet" href="<spring:url value="/css/screen.css" htmlEscape="true" />" type="text/css"/>
  <link rel="stylesheet" href="<spring:url value="/css/teststyles.css" htmlEscape="true" />" type="text/css"/>
  <link rel="stylesheet" href="<spring:url value="/css/alternative.css" htmlEscape="true" />" type="text/css"/>
  <title>Appengine Spring 3 Starter App</title>	
</head>

<%--Google Analytics (only for swagswap, you can put your own here if deploying yourself --%>
<script type="text/javascript">
var gaJsHost = (("https:" == document.location.protocol) ? "https://ssl." : "http://www.");
document.write(unescape("%3Cscript src='" + gaJsHost + "google-analytics.com/ga.js' type='text/javascript'%3E%3C/script%3E"));
</script>
<script type="text/javascript">
try {
var pageTracker = _gat._getTracker("UA-7317033-11");
pageTracker._trackPageview();
} catch(err) {}</script>

<body>
<div id="mainContent">
<table>
<tr><td style="width: 557px;">
This is a demo of Spring 3 RESTful MVC CRUD operations on App Engine.
It also has incoming mail support for adding items. To add an item via email, send an email to:<br/>
add (at) springstarterapp.appspotmail.com<br/>
with the item name in the subject, item description in the body, and an optional attached image
the item will appear on this site (reload the page)<br/>
For the code see <a href="http://code.google.com/p/appengine-springmvc3-starter-app/">http://code.google.com/p/appengine-springmvc3-starter-app/</a>
For a bigger sample app see SwagSwap: <a href="http://swagswap.appspot.com">http://swagswap.appspot.com</a>
</td></tr>
</table>