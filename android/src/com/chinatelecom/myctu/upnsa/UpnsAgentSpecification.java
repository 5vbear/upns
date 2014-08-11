package com.chinatelecom.myctu.upnsa;

/**
 * 常量接口
 * <p/>
 * User: snowway
 * Date: 12/3/13
 * Time: 13:26
 */
public interface UpnsAgentSpecification {


    interface Error {

        String NETWORK_UNAVAILABLE_CODE = "AGENT:001";

        String NETWORK_UNAVAILABLE_MSG = "无可用的网络连接";

        String CONNECT_FAILED_CODE = "AGENT:101";

        String CONNECT_FAILED_MSG = "无法建立连接";

        String PUSH_SERVER_ERROR_CODE_PREFX = "SERVER:";

        String PUSH_SERVER_ERROR_MSG = "推送服务器发生错误";
    }
}
