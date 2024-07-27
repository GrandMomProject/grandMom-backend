package com.bside.grandmom.context;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class AccessContextInterceptor implements HandlerInterceptor {

    private static final String UID_HEADER = "UID";
    private static final String DID_HEADER = "DID";

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        String uid = request.getHeader(UID_HEADER);
        String did = request.getHeader(DID_HEADER);
        AccessContextHolder.setAudit(new AccessContext(uid, did));
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        AccessContextHolder.clear();
    }
}
