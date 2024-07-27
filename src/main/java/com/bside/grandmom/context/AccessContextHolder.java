package com.bside.grandmom.context;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.core.NamedThreadLocal;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class AccessContextHolder {
    private static final ThreadLocal<AccessContext> accessContext = new NamedThreadLocal<>("Access Context");

    public static void setAudit(AccessContext access) {
        accessContext.set(access);
    }

    public static AccessContext getAccessContext() {
        return accessContext.get();
    }

    public static void clear() {
        accessContext.remove();
    }
}
