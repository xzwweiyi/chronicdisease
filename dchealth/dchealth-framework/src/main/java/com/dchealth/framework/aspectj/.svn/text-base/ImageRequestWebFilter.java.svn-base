package com.dchealth.framework.aspectj;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ReadListener;
import javax.servlet.ServletException;
import javax.servlet.ServletInputStream;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class ImageRequestWebFilter implements Filter {



    private String oldDomain;
    private String newDomain;

    public ImageRequestWebFilter(String oldDomain,String newDomain){
        this.oldDomain = oldDomain;
        this.newDomain = newDomain;
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain chain)
            throws IOException, ServletException {
        ServletRequest request = null;
        if (servletRequest instanceof HttpServletRequest) {
            request = new RequestWrapper((HttpServletRequest) servletRequest);
        }
        if (request instanceof RequestWrapper) {
            RequestWrapper requestWrapper = (RequestWrapper) request;
            String body = requestWrapper.getBody();
            String replace = body.replace(newDomain, oldDomain);
            requestWrapper.setBody(replace);
        }
        if (request == null) {
            chain.doFilter(servletRequest, servletResponse);
        } else {
            chain.doFilter(request, servletResponse);
        }
    }


    class RequestWrapper extends HttpServletRequestWrapper {

        private String body;

        public RequestWrapper(HttpServletRequest request) throws IOException {
            super(request);
            StringBuilder stringBuilder = new StringBuilder();
            BufferedReader bufferedReader = null;
            try {
                InputStream inputStream = request.getInputStream();
                if (inputStream != null) {
                    bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                    char[] charBuffer = new char[128];
                    int bytesRead = -1;
                    while ((bytesRead = bufferedReader.read(charBuffer)) > 0) {
                        stringBuilder.append(charBuffer, 0, bytesRead);
                    }
                } else {
                    stringBuilder.append("");
                }
            } catch (IOException ex) {
                throw ex;
            } finally {
                if (bufferedReader != null) {
                    try {
                        bufferedReader.close();
                    } catch (IOException ex) {
                        throw ex;
                    }
                }
            }
            body = stringBuilder.toString();
        }

        @Override
        public ServletInputStream getInputStream() throws IOException {
            final ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(body.getBytes());
            ServletInputStream servletInputStream = new ServletInputStream() {
                public boolean isFinished() {
                    return false;
                }
                public boolean isReady() {
                    return false;
                }
                public void setReadListener(ReadListener readListener) {
                }
                public int read() throws IOException {
                    return byteArrayInputStream.read();
                }
            };
            return servletInputStream;

        }

        @Override
        public BufferedReader getReader() throws IOException {
            return new BufferedReader(new InputStreamReader(this.getInputStream()));
        }
        public String getBody() {
            return this.body;
        }
        // 赋值给body字段
        public void setBody(String body) {
            this.body = body;
        }
    }
}