package com.sirius.upns.protocol.business.msg;

import java.util.Map;

/**
 * Created by pippo on 14-3-29.
 */
public class Private extends Custom {

    private static final long serialVersionUID = -4266526152971385517L;

    public static final String USER_ID = "u";

    public Private() {
        super();
    }

    @Override
    public Map<String, Object> toMap() {
        Map<String, Object> out = super.toMap();
        out.put(USER_ID, userId);
        return out;
    }

    @Override
    public void fromMap(Map<String, Object> in) {
        super.fromMap(in);
        this.userId = (String) in.get(USER_ID);
    }

    public String userId;

    @Override
    public String toString() {
        return String.format("Private [id=%s, appId=%s, groupId=%s, time=%s, title=%s, content=%s, extension=%s, userId=%s]",
                             id,
                             appId,
                             groupId,
                             time,
                             title,
                             content,
                             extension,
                             userId);
    }

}
