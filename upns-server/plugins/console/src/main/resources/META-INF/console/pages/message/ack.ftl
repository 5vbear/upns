<#escape x as (x)!>
<html>
<head>
    <title>送达统计</title>
    <meta name="page" content="message">
</head>
<body>
	<div class="panel panel-default">
	    <div class="panel-heading breadcrumb">
	    		<li>送达统计</li>
	    		<li class="active">${message.title}</li>
	    </div>
	    <div class="panel-body">
			<span class="badge">${message.content}</span>
	    </div>
	    <div class="table-responsive">
		    <table class="table table-hover">
		        <thead>
			        <tr>
			            <th>序号</th>
			            <th>ID</th>
			            <th>用户ID</th>
			            <th>创建时间</th>
			            <th>是否送达</th>
			            <th>送达时间</th>
			        </tr>
		        </thead>
		        <tbody>
		            <#list pagination.items as i>
		            <tr>
		                <td>${i_index+1}</td>
		                <td>${i.id}</td>
		                <td>${i.userId}</td>
		                <td>${i.createDate?string('yyyy-MM-dd HH:mm:ss')}</td>
		                <td>${i.ack?string("是","否")}</td>
		                <td>${i.ackDate?string('yyyy-MM-dd HH:mm:ss')}</td>
		            </tr>
		            </#list>
		        </tbody>
		    </table>
	    </div>
	    <div class="panel-footer pager">
	    		<a href="${rc.contextPath}/${pluginName}/message/ack?${searcher.previousURLAppender}">上一页</a>
	        	当前第${pagination.page.number}页/共${pagination.page.size}页
	        	<a href="${rc.contextPath}/${pluginName}/message/ack?${searcher.nextURLAppender}">下一页</a>
	    </div>
	</div>
</body>
</html>
</#escape>