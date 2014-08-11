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
    <link rel="stylesheet" href="${rc.contextPath}/${pluginName}/static/js/colorbox/colorbox.css" type="text/css"/>
    <script type="text/javascript" src="${rc.contextPath}/${pluginName}/static/js/colorbox/jquery.colorbox-min.js"></script>
    <script type="text/javascript">
        function closeColorbox(reload) {
            jQuery.colorbox.close();
            if (reload) {
                window.location.reload();
            }
        }
    </script>
<@d.head/>
</head>
<body>
<#assign page>
    <@d.getProperty property="meta.page" default=""/>
</#assign>
<div class="navbar navbar-inverse navbar-fixed-top">
    <div class="container">
        <div class="navbar-header">
            <button type="button" class="navbar-toggle" data-toggle="collapse" data-target=".navbar-collapse">
                <span class="icon-bar"></span>
                <span class="icon-bar"></span>
                <span class="icon-bar"></span>
            </button>
            <a class="navbar-brand" href="${rc.contextPath}/${pluginName}/index">UPNS</a>
        </div>
        <div class="navbar-collapse collapse">
            <ul class="nav navbar-nav">
                <li ${(page=='index')?string("class='active'","")}><a href="${rc.contextPath}/${pluginName}/index">首页</a></li>
                <li ${(page=='group')?string("class='active'","")}><a href="${rc.contextPath}/${pluginName}/group/list">群组管理</a></li>
                <li ${(page=='member')?string("class='active'","")}><a href="${rc.contextPath}/${pluginName}/member/list">成员管理</a></li>
                <li ${(page=='message')?string("class='active'","")}><a href="${rc.contextPath}/${pluginName}/message/list">消息管理</a></li>
                <li class="dropdown">
                    <a href="#" class="dropdown-toggle" data-toggle="dropdown">其他<b class="caret"></b></a>
                    <ul class="dropdown-menu">
                        <li><a href="#">文档</a></li>
                        <li><a href="#">论坛</a></li>
                        <li class="divider"></li>
                        <li><a href="#">关于</a></li>
                    </ul>
                </li>
            </ul>
        </div>
    </div>
</div>

<#if page=='index'>
<div class="jumbotron">
    <div class="container">
        <br/>

        <h1>UPNS</h1>
        <blockquote>
            <p>
                <strong class="text-danger">U</strong>nified
                <strong class="text-danger">P</strong>ush
                <strong class="text-danger">N</strong>otification
                <strong class="text-danger">S</strong>ervice
                为IOS,Android,Web提供的统一消息推送服务
            </p>
        </blockquote>
    </div>
</div>
<#else>
<div style="margin-bottom: 60px;height: 20px;"></div>
</#if>

<div class="container">
<@d.body/>
    <hr>
    <footer>
        <p>&copy; 2013 上海电信网上大学</p>
    </footer>
</div>
<!-- /container -->
</body>
</html>