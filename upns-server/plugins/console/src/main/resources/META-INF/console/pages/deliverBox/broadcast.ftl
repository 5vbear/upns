<#escape x as (x)!>
<html>
<head>
    <title>应用内消息广播</title>
    <meta name="dialogParent" content="消息中心">
    <meta name="dialogTitle" content="应用广播">
    <script type="text/javascript">

    </script>
</head>
<body>
	<form role="form" action="${rc.contextPath}/${pluginName}/deliverBox/broadcast" method="post">
		<#if message?exists>
		<input type="hidden" name="id" value="${message.id}" />
		</#if>
		
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
	                   id="title" name="title" placeholder="消息标题" value="${message.title}"/>
	        </div>
	    </div>
	    
	    <div class="form-group">
	        <label for="content" class="col-lg-2 control-label">消息内容</label>
	        <div class="col-lg-10">
	            <textarea class="form-control" id="content" name="content" placeholder="消息内容" rows="4">${message.content}</textarea>
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