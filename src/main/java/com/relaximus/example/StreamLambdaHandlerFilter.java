package com.relaximus.example;

import com.amazonaws.serverless.proxy.RequestReader;
import com.relaximus.example.controller.PingController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.*;
import java.io.IOException;
import java.util.Map;

public class StreamLambdaHandlerFilter implements Filter {
    private Logger log = LoggerFactory.getLogger(StreamLambdaHandlerFilter.class);

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        Object stageVars = servletRequest.getAttribute(RequestReader.API_GATEWAY_STAGE_VARS_PROPERTY);
        if (stageVars == null) {
            log.info("API Gateway stage vars context is null");
            filterChain.doFilter(servletRequest, servletResponse);
            return;
        }
        if (!Map.class.isAssignableFrom(stageVars.getClass())) {
            log.info("API Gateway stage vars object is not of valid type");
            filterChain.doFilter(servletRequest, servletResponse);
            return;
        }

        Map<String, String> vars = (Map<String, String>) stageVars;

        log.info("Vars: " + stageVars.toString());

        PingController.MY_URL = vars.get("webhookUrl");

        filterChain.doFilter(servletRequest, servletResponse);
    }

    @Override
    public void destroy() {

    }
}
