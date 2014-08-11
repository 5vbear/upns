<#escape x as (x)!>
<html>
<head>
    <title>成员管理</title>
    <meta name="page" content="member">
    <script type="text/javascript">
        $(function () {
            $("#member-add, [id*=deliverbox-send]").click(function () {
                $.colorbox({width: "640", height: "480", iframe: true,href: $(this).attr("rel")});
            });

            $("[id*=member-remove]").click(function () {
                if (confirm("确实要删除成员?")) {
                    location.href = $(this).attr("rel");
                }
            });
        })
    </script>
</head>
<body>
<div class="panel panel-default">
    <div class="panel-heading breadcrumb">
        <li>成员管理</li>
        <li class="active"><#if group?exists>组[${group.name}]<#else>所有</#if></li>
        <div class="btn-toolbar pull-right">
            <div class="btn-group">
                <a id="member-add" rel="${rc.contextPath}/${pluginName}/member/join/${group.id}.dlg" class="btn btn-primary btn-xs">新增成员</a>
            </div>
            <div class="btn-group">
                <a href="${rc.contextPath}/${pluginName}/message/list?example.groupId=${group.id}" class="btn btn-primary btn-xs">消息管理</a>
            </div>
        </div>
    </div>
    <div class="panel-body">
        <form class="form-horizontal" role="form" action="${rc.contextPath}/${pluginName}/member/list" method="post">
            <div class="form-group">
                <label for="title" class="col-lg-2 control-label">组编号</label>

                <div class="col-lg-10">
                    <div class="input-group">
                        <input type="text" class="form-control" id="groupId" name="example.groupId" value="${searcher.example.groupId}"
                               placeholder="目标组"/>
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
                <th>用户编号</th>
                <th>所在组</th>
                <th>创建时间</th>
                <th>操作</th>
            </tr>
            </thead>
            <tbody>
                <#list pagination.items as i>
                <tr>
                    <td>${i_index+1}</td>
                    <td>${i.userId}</td>
                    <td><a href="${rc.contextPath}/${pluginName}/member/list?example.groupId=${i.groupId}">${groups[i.groupId].name}</a>
                    </td>
                    <td>${i.createDate?string('yyyy-MM-dd HH:mm:ss')}</td>
                    <td>
                        <a id="member-remove-${i_index+1}"
                           rel="${rc.contextPath}/${pluginName}/member/remove/${i.groupId}/${i.userId}?${searcher.URLAppender}"
                           class="btn btn-xs btn-danger">删除</a>
                        <a id="deliverbox-send-${i_index+1}" rel="${rc.contextPath}/${pluginName}/deliverBox/send/${i.userId}.dlg"
                           class="btn btn-xs btn-success">发送私信</a>
                    </td>
                </tr>
                </#list>
            </tbody>
        </table>
    </div>
    <div class="panel-footer">
        <ul class="pager">
            <li><a href="${rc.contextPath}/${pluginName}/member/list?${searcher.previousURLAppender}">上一页</a></li>
            <li> 当前第${pagination.page.number}页/共${pagination.page.size}页</li>
            <li><a href="${rc.contextPath}/${pluginName}/member/list?${searcher.nextURLAppender}">下一页</a></li>
        </ul>
    </div>
</div>
</body>
</html>
</#escape>