<!DOCTYPE html>

<!--
  ~ Copyright © 2010 www.myctu.cn. All rights reserved.
  -->

<html>
<head>
    <title>websocket demo</title>

    <meta http-equiv="content-type" content="text/html; charset=UTF-8"/>

    <script type="text/javascript" src="http://code.jquery.com/jquery-1.10.2.min.js"></script>
    <script>
        var CTUWebsocket = function (callback) {

            var socket;

            return {

                open: function (protocol) {
                    if (socket == null) {
                        socket = new WebSocket("${websocket_url}/ctu.web-socket/pippo", protocol);
                    }

                    socket.onmessage = function (event) {
                        console.log(event);
                        callback(socket, event);
                    }

                    socket.onopen = function (event) {
                        console.log(event);
                        callback(socket, event);
                    }

                    socket.onclose = function (event) {
                        console.log(event);
                        callback(socket, event);
                    }

                    socket.onerror = function (event) {
                        console.log(event);
                        callback(socket, event);
                    }
                },

                close: function () {
                    if (socket != null) {
                        socket.close();
                    }
                    socket = null;
                },

                send: function (message) {
                    socket.send(message);
                },

                getSocket: function () {
                    return socket;
                },

                isOpen: function () {
                    return socket != null && socket.readyState == 1;
                }
            };
        }


        $(function () {
            var ctu_websocket = new CTUWebsocket(function (socket, event) {
                $("#logger").append("<p>" + JSON.stringify(event) + "</p>").append("<hr/>");

                if (event.type == "message") {
                    $("#receive-box").append("<p>" + event.data + "</p>");
                }
            });

            $("#connect").click(function () {
                ctu_websocket.open($("#protocol").val());
            })

            $("#disconnect").click(function () {
                ctu_websocket.close();
            })

            $("#send").click(function () {
                if (!ctu_websocket.isOpen()) {
                    ctu_websocket.open($("#protocol").val());
                }

                ctu_websocket.send($("#send-box").val());
                $("#send-box").val("")
            });
        })
    </script>
</head>
<body>
<div>
    <div>
        <select id="protocol">
            <option value="echo">echo</option>
            <option value="json">json</option>
        </select>
        <button id="connect" type="button">连接</button>
        <button id="disconnect" type="button">断开</button>
    </div>
    <hr/>

    <span>收到的消息包</span>

    <div id="logger" style="background: blue;height: 300px;overflow:auto" title="日志">

    </div>
    <hr/>

    <span>收到的消息</span>

    <div id="receive-box" style="background: blue;height: 100px;overflow:auto">

    </div>
    <hr/>

    <span>发送的消息</span>

    <div style="background: #ffffff;height: 100px;">
        <textarea id="send-box"
                  style="background: #222222;color: lawngreen;width: 80%;height: 100px;overflow:auto"></textarea>
        <button id="send" type="button">发送</button>
    </div>
    <hr/>
</div>
</body>
</html>