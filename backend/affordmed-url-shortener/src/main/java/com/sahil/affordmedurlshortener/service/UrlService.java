package com.sahil.affordmedurlshortener.service;

import com.sahil.affordmedurlshortener.model.ClickDetail;
import com.sahil.affordmedurlshortener.model.Url;
import com.sahil.affordmedurlshortener.repository.UrlRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Random;

@Service
public class UrlService {
    @Autowired
    private UrlRepository urlRepository;

    public Url createShortUrl(String originalUrl, Integer validity, String shortcode) {
        if (shortcode == null || shortcode.isEmpty()) {
            shortcode = generateShortcode();
        } else {
            if (!shortcode.matches("^[a-zA-Z0-9]{4,10}$"))
                throw new IllegalArgumentException("Invalid shortcode format");
            if (urlRepository.existsByShortcode(shortcode))
                throw new IllegalArgumentException("Shortcode already exists");
        }
        LocalDateTime now = LocalDateTime.now();
        Url url = new Url();
        url.setShortcode(shortcode);
        url.setOriginalUrl(originalUrl);
        url.setCreatedAt(now);
        url.setExpireAt(now.plusMinutes(validity != null ? validity : 30));
        url.setClickCount(0);
        url.setClickDetails(new ArrayList<>());
        return urlRepository.save(url);
    }

    private String generateShortcode() {
        String chars = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        Random rnd = new Random();
        String shortcode;
        do {
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < 6; i++) sb.append(chars.charAt(rnd.nextInt(chars.length())));
            shortcode = sb.toString();
        } while (urlRepository.existsByShortcode(shortcode));
        return shortcode;
    }

    public Url getUrlStats(String shortcode) {
        Url url = urlRepository.findByShortcode(shortcode);
        if (url == null) throw new IllegalArgumentException("Shortcode not found");
        return url;
    }

    public String redirectToOriginal(String shortcode, String referrer, String geoLocation) {
        Url url = urlRepository.findByShortcode(shortcode);
        if (url == null) throw new IllegalArgumentException("Shortcode not found");
        if (LocalDateTime.now().isAfter(url.getExpireAt()))
            throw new IllegalStateException("Link expired");
        url.setClickCount(url.getClickCount() + 1);
        ClickDetail detail = new ClickDetail(LocalDateTime.now(), referrer, geoLocation);
        url.getClickDetails().add(detail);
        urlRepository.save(url);
        return url.getOriginalUrl();
    }
}

