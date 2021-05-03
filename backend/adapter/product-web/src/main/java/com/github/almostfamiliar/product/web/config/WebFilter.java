package com.github.almostfamiliar.product.web.config;

import javax.servlet.*;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@javax.servlet.annotation.WebFilter("*")
public class WebFilter implements Filter {
  @Override
  public void doFilter(
      ServletRequest servletRequest, ServletResponse servletResponse, FilterChain chain)
      throws IOException, ServletException {
    HttpServletResponse httpServletResponse = (HttpServletResponse) servletResponse;
    httpServletResponse.setHeader("Access-Control-Allow-Origin", "*");
    chain.doFilter(servletRequest, servletResponse);
  }
}
