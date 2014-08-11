<#escape x as (x)!>
<html>
<head>
    <title>消息管理</title>
    <meta name="page" content="message">
</head>
<body>
<div class="panel panel-default">
    <div class="panel-heading breadcrumb">
        <li>消息管理</li>
        <li class="active"><#if group?exists>组[${group.name}]<#else>所有</#if></li>
    </div>
    <div class="panel-body">
        <form class="form-horizontal" role="form" action="${rc.contextPath}/${pluginName}/message/list" method="post">
            <input type="hidden" name="groupId" value="${group.id}"/>

            <div class="form-group">
                <label for="title" class="col-lg-2 control-label">消息标题</label>

                <div class="col-lg-10">
                    <div class="input-group">
                        <input type="text" class="form-control" id="title" name="example.name" value="${group.name}" placeholder="消息标题"/>
						<span class="input-group-btn">
							<a class="btn btn-success">查询</a>
						</span>
                    </div>
                </div>
            </div>
        </form>
    </div>
    <div class="table-responsive">
        <table class="table table-hover">
            <thead>
            <tr>
                <th>序号</th>
                <th>消息编号</th>
                <th>来源APP</th>
                <th>目标组</th>
                <th>消息标题</th>
                <th>创建时间</th>
                <th>Actions...</th>
            </tr>
            </thead>
            <tbody>
                <#list pagination.items?if_exists as i>
                <tr>
                    <td>${i_index+1}</td>
                    <td>${i.id}</td>
                    <td>${i.appId}</td>
                    <td><a href="${rc.contextPath}/${pluginName}/message/list?example.groupId=${i.groupId}">${groups[i_index].name}</a></td>
                    <td><#if i.title?default("")?length &gt; 5 >${i.title?default("")?substring(0,5)}<#else>${i.title}</#if></td>
                    <td>${i.createDate?string('yyyy-MM-dd HH:mm:ss')}</td>
                    <td>
                        <a href="${rc.contextPath}/${pluginName}/message/ack?example.messageId=${i.id}" class="btn btn-xs btn-success">送达统计</a>
                    </td>
                </tr>
                </#list>
            </tbody>
        </table>
    </div>
    <div class="panel-footer pager">
        <a href="${rc.contextPath}/${pluginName}/message/list?${searcher.previousURLAppender}">上一页</a>
        当前第${pagination.page.number}页/共${pagination.page.size}页
        <a href="${rc.contextPath}/${pluginName}/message/list?${searcher.nextURLAppender}">下一页</a>
    </div>
</div>
</body>
</html>
</#escape>