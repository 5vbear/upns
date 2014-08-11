package com.chinatelecom.myctu.upnsa.protocol;

import java.util.List;
import java.util.Map;

/**
 * 包体接口
 * <p/>
 * User: snowway
 * Date: 10/23/13
 * Time: 5:28 PM
 */
public interface Payload {


    /**
     * 获取字符串内容
     */
    String getString(String key);

    /**
     * 获取Boolean内容
     *
     * @param key
     * @return
     * @throws Exception
     */
    boolean getBoolean(String key);


    /**
     * 获取整型内容
     *
     * @param key
     * @return
     */
    int getInt(String key);


    /**
     * 获取Map内容
     *
     * @param key
     * @return
     */
    Map getMap(String key);


    /**
     * 设置字符串内容
     *
     * @param key
     * @param value
     */
    void put(String key, String value);

    /**
     * 设置boolean内容
     *
     * @param key
     * @param value
     * @throws Exception
     */
    void put(String key, boolean value);

    /**
     * 设置int内容
     *
     * @param key
     * @param value
     * @throws Exception
     */
    void put(String key, int value);

    /**
     * 读取long类型内容
     *
     * @param key key
     * @return value
     */
    long getLong(String key);


    /**
     * 设置long类型内容
     *
     * @param key
     * @param value
     * @throws Exception
     */
    void put(String key, long value);


    /**
     * @return 转换成字节数组
     */
    byte[] toByteArray();

    /**
     * 作为列表的Payload
     *
     * @param key key
     * @return list of payload
     */
    List<Payload> getPayloadList(String key);
}
