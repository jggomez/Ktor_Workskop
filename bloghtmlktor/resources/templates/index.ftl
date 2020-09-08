<html>
<head>
    <link rel="stylesheet" href="/static/styles.css">
</head>
<body>
<#if error??>
    <p style="color:red;">${error}</p>
</#if>
<form action="/blog" method="post" enctype="application/x-www-form-urlencoded">
    <br/>
    <div><h2>Post Blog</h2></div>
    <br/>
    <div>Title:</div>
    <div><input type="text" name="title" /></div>
    <br/>
    <div>Description:</div>
    <div><input type="text" name="description" /></div>
    <br/>
    <div>Date:</div>
    <div><input type="date" name="date" /></div>
    <br/>
    <div><input type="submit" value="Send" /></div>
</form>
</body>
</html>