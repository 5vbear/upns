<#escape x as (x)!>
<html>
<head>
    <title>群组编辑</title>
    <meta name="dialogParent" content="群组管理">
    <meta name="dialogTitle" content="编辑">
    <script type="text/javascript">

    </script>
</head>
<body>
	<form class="form-horizontal" role="form" action="${rc.contextPath}/${pluginName}/group/edit" method="post">
	    <input type="hidden" name="id" value="${group.id}" />
	    
	    <div class="form-group">
	        <label for="appId" class="col-lg-2 control-label">应用ID</label>
	        <div class="col-lg-10">
	            <input type="text" class="form-control" id="appId" name="appId" value="${group.appId}" placeholder="应用ID" />
	        </div>
	    </div>
	    
	    <div class="form-group">
	        <label for="name" class="col-lg-2 control-label">群组名称</label>
	        <div class="col-lg-10">
	            <input type="text" class="form-control" id="name" name="name" value="${group.name}" placeholder="群组名称" />
	        </div>
	    </div>
	    
	    <div class="form-group">
	        <div class="col-lg-offset-2 col-lg-10">
	            <button type="submit" class="btn btn-success pull-right">保存</button>
	        </div>
	    </div>
	</form>
</body>
</html>
</#escape>