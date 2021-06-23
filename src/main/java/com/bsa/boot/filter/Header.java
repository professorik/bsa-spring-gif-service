package com.bsa.boot.filter;

import com.bsa.boot.exception.Handler;
import com.bsa.boot.exception.NoHeaderException;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
/**
 * @author professorik
 * @created 22/06/2021 - 09:43
 * @project boot
 */
@Component
public class Header implements Filter {

    private Handler handler;

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        String header = request.getHeader("X-BSA-GIPHY");
        if (header == null) {
            handler.handleNoBsaGiphyException(new NoHeaderException("The header wasn't found"));
            response.sendError(403);
        }
        filterChain.doFilter(request, response);
    }

    @Override
    public void init(FilterConfig filterConfig) {
        WebApplicationContext ctx = WebApplicationContextUtils
                .getRequiredWebApplicationContext(filterConfig.getServletContext());
        this.handler = ctx.getBean(Handler.class);
    }

    @Override
    public void destroy() {
    }
}