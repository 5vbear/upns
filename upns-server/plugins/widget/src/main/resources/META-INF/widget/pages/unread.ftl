<!doctype html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>未读消息</title>
    <link rel='stylesheet' href="${rc.contextPath}/widget/static/css/common.css"/>
    <script type="text/javascript" src="${rc.contextPath}/widget/static/js/jquery-1.7.2.min.js"></script>
    <script type="text/javascript" src="http://utb.myctu.cn/toolbar.js?draw=0"></script>
    <script type="text/javascript">
        $(function () {
            $(document).bind("utb:initialized", function () {
                $.get("${rc.contextPath}/widget/msg/ack/${userId}", function (result) {
                    if (result.successful) {
                        utb.dispatchEvent("badge", {
                            widgetId: 'message-center',
                            targetId: location.hash.replace("#", ""),
                            data: {
                                badge: 0
                            }
                        });
                    }
                })
            });
        });
    </script>
</head>
<body class="pg_space">
<i></i>

<div>
    <div class="esnNav_FixTitle">
        <h2>${totalUnread}条新通知</h2>
    </div>
    <div class="boxscroll" id="boxscroll"
         tabindex="5000">
        <div id="ct" class="cl bgnoz">
            <div class="bgwhite mn_home fpr w_width">
                <div class="bm pdbm">
                    <div id="feed_div" class="e">
                        <div class="xld xlda">
                            <div id="feed">
                            <#list messages! as message>
                                    <@show_message message/>
                                </#list>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="pg"></div>
                <div id="ajax_wait" style="display: none;">
                    <div style="padding-top:12px; text-align:center;"><span class="contentLoading"><i
                            class="ld_ico"></i>正在加载，请稍后...</span>
                    </div>
                </div>
            </div>
        </div>
    </div>

</div>

</body>
</html>


<#macro show_message msg>
    <#escape x as (x)!>
    <dl class="list cl" id="1187916" data-lastone="1" data-id="1187916" data-comment="0"
        data-zhuanfa="0" commentdata="notice+2750+1187916">
        <dd class="m avt wid56">
            <a class="perPanel" href="#" target="_blank">
                <img src="${rc.contextPath}/widget/static/img/noavatar_small.gif"/>
            </a>
        </dd>
        <dd class="contBox cl">
            <div><a added="ignore" target="_blank" href="?uid=${msg.senderId}"
                    class="c_t1 f14 fb mg_rt20">${msg.senderName!("未知用户")}</a><span
                    class="c_t3">${msg.createTime?datetime}</span>&nbsp;&nbsp;<span class="c_t3">来自<a
                    href="javascript:;" class="xi2"></a><a class="xi2" href="javascript:;">桌面端</a></span>
            </div>
            <div class="triangle"></div>
            <div class="content1">
                <div class="content2">
                    <div class="content old">
                        <div class="mediatitle">
                            <a href="${msg.linkUrl}">${msg.title}</a>
                        </div>
                        <div>${msg.content}</div>
                    </div>
                </div>
            </div>
        </dd>
    </dl>
    </#escape>
</#macro>