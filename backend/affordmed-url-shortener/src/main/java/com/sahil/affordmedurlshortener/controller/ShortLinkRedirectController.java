package com.sahil.affordmedurlshortener.controller;

import com.sahil.affordmedurlshortener.service.UrlService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
@RestController
public class ShortLinkRedirectController {
    @Autowired
    private UrlService urlService;

    @GetMapping("/{shortcode}")
    public ResponseEntity<?> handleShortLink(@PathVariable String shortcode, HttpServletRequest request) {
        try {
            String referrer = request.getHeader("Referer");
            String geoLocation = request.getRemoteAddr();
            String url = urlService.redirectToOriginal(shortcode, referrer, geoLocation);
            return ResponseEntity.status(HttpStatus.FOUND).header("Location", url).build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponse(e.getMessage()));
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.GONE).body(new ErrorResponse(e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ErrorResponse("Internal error"));
        }
    }
}