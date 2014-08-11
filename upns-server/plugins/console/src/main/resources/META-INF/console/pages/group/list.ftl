<#escape x as (x)!>
<html>
<head>
    <title>群组管理</title>
    <meta name="page" content="group">
    <script type="text/javascript">
        $(function () {
            $("#group-add, [id*=group-edit], #group-publish, #message-broadcast").click(function () {
                $.colorbox({width: "640",
                    height: "500",
                    iframe: true,
                    href: $(this).attr("rel")});
            });

            //删除群组
            $("[id*=group-remove]").click(function () {
                if (confirm("确实要删除群组?")) {
                    location.href = $(this).attr("rel");
                }
            });
        })
    </script>
</head>
<body>

	<div class="panel panel-default">
	    <div class="panel-heading breadcrumb">
	    		<li>群组管理</li>
			<li class="active">列表</li>
	    		  
	    		<div class="btn-toolbar pull-right">		  
			  <div class="btn-group">
			  	<a id="group-add" rel="${rc.contextPath}/${pluginName}/group/edit.dlg" class="btn btn-primary btn-xs">新增群组</a>
			  </div>
			  <div class="btn-group">
			  	<a id="message-broadcast" rel="${rc.contextPath}/${pluginName}/deliverBox/broadcast.dlg" class="btn btn-primary btn-xs">应用广播</a>
			  </div>
			</div>
	    </div>
	    <div class="table-responsive">
		    <table class="table table-hover">
		        <thead>
			        <tr>
			            <th>序号</th>
			            <th>群组名称</th>
			            <th>群组编号</th>
			            <th>应用编号</th>
			            <th>操作</th>
			        </tr>
		        </thead>
		        <tbody>
		            <#list pagination.items as g>
		            <tr>
		                <td>${g_index+1}</td>
		                <td><a href="${rc.contextPath}/${pluginName}/member/list?example.groupId=${g.id}">${g.name}</a></td>
		                <td>${g.id}</td>
		                <td>${g.appId}</td>
		                <td>
		                		<a id="group-publish" rel="${rc.contextPath}/${pluginName}/deliverBox/publish/${g.id}.dlg" class="btn btn-success btn-xs">发布消息</a>
							<div class="btn-group">
							  <button type="button" class="btn btn-success btn-xs dropdown-toggle" data-toggle="dropdown">
							    管理 <span class="caret"></span>
							  </button>
							  <ul class="dropdown-menu" role="menu">
								<li><a id="group-edit-${g_index+1}" rel="${rc.contextPath}/${pluginName}/group/edit/${g.id}.dlg">编辑</a></li>
								<li><a id="group-remove-${g_index+1}" rel="${rc.contextPath}/${pluginName}/group/remove/${g.id}?${searcher.URLAppender}">删除</a></li>
								<li class="divider"></li>
								<li><a href="${rc.contextPath}/${pluginName}/member/list?example.groupId=${g.id}">成员管理</a></li>
								<li><a href="${rc.contextPath}/${pluginName}/message/list?example.groupId=${g.id}">消息管理</a></li>
							  </ul>
							</div>
		                </td>
		            </tr>
		            </#list>
		        </tbody>
		    </table>
	    </div>
	    <div class="panel-footer">
	    		<ul class="pager">
	    			<li><a href="${rc.contextPath}/${pluginName}/group/list?${searcher.previousURLAppender}">上一页</a></li>
	    			<li>	当前第${pagination.page.number}页/共${pagination.page.size}页</li>
	    			<li>	<a href="${rc.contextPath}/${pluginName}/group/list?${searcher.nextURLAppender}">下一页</a></li>
	    		</ul>
	    </div>
	</div>
</body>
</html>
</#escape>