package com.chinatelecom.myctu.upnsa.core.utils;

import java.io.*;

/**
 * Utility class for Serialization
 * <p/>
 * User: snowway
 * Date: 11/2/13
 * Time: 10:45 PM
 */
public class SerializationUtils {

    /**
     * 对象序列化
     *
     * @param obj
     * @return
     * @throws IOException
     */
    public static byte[] serialize(Object obj) throws IOException {
        ByteArrayOutputStream b = new ByteArrayOutputStream();
        ObjectOutputStream o = new ObjectOutputStream(b);
        o.writeObject(obj);
        return b.toByteArray();
    }

    /**
     * 对象反序列化
     *
     * @param bytes
     * @return
     * @throws IOException
     * @throws ClassNotFoundException
     */
    public static Object deserialize(byte[] bytes) throws IOException, ClassNotFoundException {
        ByteArrayInputStream b = new ByteArrayInputStream(bytes);
        ObjectInputStream o = new ObjectInputStream(b);
        return o.readObject();
    }
}
