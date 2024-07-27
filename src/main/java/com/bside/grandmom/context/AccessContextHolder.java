package com.bside.grandmom.context;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.core.NamedThreadLocal;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class AccessContextHolder {
    private static final ThreadLocal<AccessContext> accessContext = new NamedThreadLocal<>("Access Context");

    public static void setAccess(AccessContext access) {
        accessContext.set(access);
    }

    public static AccessContext getAccessContextNonRequired() {
        return accessContext.get();
    }

    public static AccessContext getAccessContext() {
        AccessContext context = accessContext.get();
        if (!context.isValid()) {
            throw new IllegalStateException("uid or did 없음");
        }
        return context;
    }

    public static void clear() {
        accessContext.remove();
    }
}
