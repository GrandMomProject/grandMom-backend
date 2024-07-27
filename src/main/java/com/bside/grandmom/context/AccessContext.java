package com.bside.grandmom.context;

import org.apache.commons.lang3.StringUtils;

public record AccessContext(String uid, String did) {

    public boolean isValid() {
        return StringUtils.isNotEmpty(uid) && StringUtils.isNotEmpty(did);
    }

}
