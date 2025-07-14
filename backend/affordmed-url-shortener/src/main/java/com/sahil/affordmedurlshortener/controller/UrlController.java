package com.sahil.affordmedurlshortener.controller;

import com.sahil.affordmedurlshortener.dto.UrlRequest;
import com.sahil.affordmedurlshortener.dto.UrlStatsResponse;
import com.sahil.affordmedurlshortener.model.Url;
import com.sahil.affordmedurlshortener.service.UrlService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/shorturls")
public class UrlController {
    @Autowired
    private UrlService urlService;

    @PostMapping
    public ResponseEntity<?> createShortUrl(@RequestBody UrlRequest req, HttpServletRequest request) {
        try {
            Url url = urlService.createShortUrl(req.getUrl(), req.getValidity(), req.getShortcode());
            String baseUrl = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort();
            String shortLink = baseUrl + "/" + url.getShortcode();
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(new ShortUrlResponse(shortLink, url.getExpireAt().toString()));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse(e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ErrorResponse("Internal error"));
        }
    }

    // Changed analytics endpoint to avoid conflict with redirect
    @GetMapping("/stats/{shortcode}")
    public ResponseEntity<?> getStats(@PathVariable String shortcode, HttpServletRequest request) {
        try {
            Url url = urlService.getUrlStats(shortcode);
            String baseUrl = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort();
            String shortLink = baseUrl + "/" + url.getShortcode();
            return ResponseEntity.ok(
                    new UrlStatsResponse(
                            url.getId(),
                            shortLink,
                            url.getShortcode(),
                            url.getOriginalUrl(),
                            url.getCreatedAt().toString(),
                            url.getExpireAt().toString(),
                            url.getClickCount(),
                            url.getClickDetails()
                    )
            );
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponse(e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ErrorResponse("Internal error"));
        }
    }

    @GetMapping("/{shortcode}/redirect")
    public ResponseEntity<?> redirect(@PathVariable String shortcode, HttpServletRequest request) {
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





@Data
@AllArgsConstructor
class ShortUrlResponse {
    private final String shortLink;
    private final String expiry;
}

@Data
@AllArgsConstructor
class ErrorResponse {
    private final String error;
}
