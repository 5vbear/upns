<#assign d=JspTaglibs[taglibs+"/sitemesh-decorator.tld"]>
<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>网大UPNS控制台</title>
    <link rel='stylesheet' href="${rc.contextPath}/${pluginName}/static/css/bootstrap.css"/>
    <link rel='stylesheet' href="${rc.contextPath}/${pluginName}/static/css/upns.css"/>
    <script src="${rc.contextPath}/${pluginName}/static/js/jquery-1.8.1.js"></script>
    <script src="${rc.contextPath}/${pluginName}/static/js/bootstrap.js"></script>
<@d.head/>
</head>
<body>
<div class="panel panel-default">
    <div class="panel-heading breadcrumb">
        <li><@d.getProperty property="meta.dialogParent" default=""/></li>
        <li class="active"><@d.getProperty property="meta.dialogTitle" default=""/></li>
    </div>
    <div class="panel-body">
    <@d.body/>
    </div>
</div>
</body>
</html>