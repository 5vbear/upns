<#escape x as (x)!>
<html>
<head>
    <title>群组内消息发布</title>
    <meta name="dialogParent" content="消息中心">
    <meta name="dialogTitle" content="私信">
    <script type="text/javascript">

    </script>
</head>
<body>
	<form role="form" action="${rc.contextPath}/${pluginName}/deliverBox/send/${userId}" method="post">

		<div class="form-group">
	        <label for="app" class="col-lg-2 control-label">用户编号</label>
	        <div class="col-lg-10">
	            <input type="text" class="form-control"
	                   id="appId" name="userId" placeholder="用户编号" value="${userId}" />
	        </div>
	    </div>
		
		<div class="form-group">
	        <label for="app" class="col-lg-2 control-label">来源APP</label>
	        <div class="col-lg-10">
	            <input type="text" class="form-control"
	                   id="appId" name="appId" placeholder="来源APP" value="" />
	        </div>
	    </div>
	
	    <div class="form-group">
	        <label for="title" class="col-lg-2 control-label">消息标题</label>
	        <div class="col-lg-10">
	            <input type="text" class="form-control"
	                   id="title" name="title" placeholder="消息标题" value=""/>
	        </div>
	    </div>
	    
	    <div class="form-group">
	        <label for="content" class="col-lg-2 control-label">消息内容</label>
	        <div class="col-lg-10">
	            <textarea class="form-control" id="content" name="content" placeholder="消息内容" rows="4"></textarea>
	        </div>
	    </div>
	    
	    <div class="form-group">
	        <div class="col-lg-offset-2 col-lg-10">
	            <button type="submit" class="btn btn-success pull-right">发布</button>
	        </div>
	    </div>
	</form>
</body>
</html>
</#escape>