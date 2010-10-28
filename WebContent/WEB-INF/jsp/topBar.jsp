<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>

<script type="text/javascript">
function checkForSubmit(event) {
	if(event && event.which == 13) doSearch();
}

function doSearch() {
	var query = document.getElementById('searchBox').value;
	var link = document.getElementById('searchLink');
	link.href += "?q=" + escape(query);
}
</script>

<ul id="menu">
	<li><a href="home.htm" target="_self">Home</a></li>
	<li><a href="profile.htm" target="_self">Profile</a></li>
	<li>
		<a id="searchLink" href="search.htm" target="_self" onclick="doSearch()">Search</a>
		<input id="searchBox" class="searchBox" type="text" onkeypress="checkForSubmit(event)"
			value="(enter a name, email or company)" size="40">
	</li>
	<li><a href="logout.htm" target="_self">Logout</a></li>
</ul>

<link rel="stylesheet" type="text/css" href="styles/menu_style.css" />
