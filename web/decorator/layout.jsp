<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/1.9.1/jquery.min.js"></script>
    <script src="js/kickstart.js"></script>
    <link rel="stylesheet" href="css/kickstart.css" media="all"/>

    <title><sitemesh:write property='title'/></title>
    <sitemesh:write property='head'/>
</head>
<body>

<div class="gallery">
    <%--<a href="http://placehold.it/600x450/4D99E0/ffffff.png&text=600x450"><img src="http://placehold.it/100x100/4D99E0/ffffff.png&text=100x100" width="500" height="100"/></a>--%>
    <%--<a href="http://placehold.it/600x450/75CC00/ffffff.png&text=600x450"><img src="http://placehold.it/100x100/75CC00/ffffff.png&text=100x100" width="100" height="100"/></a>--%>
    <%--<a href="http://placehold.it/600x450/E49800/ffffff.png&text=600x450"><img src="http://placehold.it/100x100/E49800/ffffff.png&text=100x100" width="100" height="100"/></a>--%>
    <%--<a href="http://placehold.it/600x450/E4247E/ffffff.png&text=600x450"><img src="http://placehold.it/100x100/E4247E/ffffff.png&text=100x100" width="100" height="100"/></a>--%>
    <%--<a href="http://placehold.it/600x450/00C6E4/ffffff.png&text=600x450"><img src="http://placehold.it/100x100/00C6E4/ffffff.png&text=100x100" width="100" height="100"/></a>--%>
    <%--<a href="http://placehold.it/600x450/E4DB0F/ffffff.png&text=600x450"><img src="http://placehold.it/100x100/E4DB0F/ffffff.png&text=100x100" width="100" height="100"/></a>--%>
    <%--<a href="http://placehold.it/600x450/7400E4/ffffff.png&text=600x450"><img src="http://placehold.it/100x100/7400E4/ffffff.png&text=100x100" width="100" height="100"/></a>--%>
    <%--<a href="http://placehold.it/600x450/C50000/ffffff.png&text=600x450"><img src="http://placehold.it/100x100/C50000/ffffff.png&text=100x100" width="100" height="100"/></a>--%>
</div>
<table>
    <tr>
        <th colspan="2">top</th>
    </tr>
    <tr>
        <td>left</td>
        <td><sitemesh:write property='body'/></td>
    </tr>
</table>

</body>
</html>
