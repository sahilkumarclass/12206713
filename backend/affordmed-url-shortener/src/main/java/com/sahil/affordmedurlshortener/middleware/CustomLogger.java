package com.sahil.affordmedurlshortener.middleware;


import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import jakarta.servlet.Filter;


public class CustomLogger implements Filter {
    private static final String LOG_FILE = "requests.log";
    private static final Object lock = new Object();

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;

        // Gather log details
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
        String method = req.getMethod();
        String uri = req.getRequestURI();
        String query = req.getQueryString();
        String remoteAddr = req.getRemoteAddr();

        StringBuilder logEntry = new StringBuilder();
        logEntry.append("[").append(timestamp).append("] ")
                .append(method).append(" ")
                .append(uri);
        if (query != null) {
            logEntry.append("?").append(query);
        }
        logEntry.append(" from ").append(remoteAddr).append("\n");
        synchronized (lock) {
            try (FileWriter fw = new FileWriter(LOG_FILE, true);
                 BufferedWriter bw = new BufferedWriter(fw)) {
                bw.write(logEntry.toString());
            }
        }

        chain.doFilter(request, response);
    }
}

