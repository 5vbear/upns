<#escape x as (x)!>
<html>
<head>
    <title>成员加入</title>
    <meta name="dialogParent" content="成员管理:[${group.name}]">
    <meta name="dialogTitle" content="新增成员">
    <script type="text/javascript">

    </script>
</head>
<body>
	<form role="form" action="${rc.contextPath}/${pluginName}/member/join" method="post">
	    <div class="form-group">
	        <label for="appId" class="col-lg-2 control-label">目标组</label>
	        <div class="col-lg-10">
	            <input type="text" class="form-control" id="groupId" name="groupId" value="${group.id}" placeholder="目标组" />
	        </div>
	    </div>
	    
	    <div class="form-group">
	        <label for="name" class="col-lg-2 control-label">用户ID</label>
	        <div class="col-lg-10">
	            <input type="text" class="form-control" id="userId" name="userId" placeholder="用户ID" />
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