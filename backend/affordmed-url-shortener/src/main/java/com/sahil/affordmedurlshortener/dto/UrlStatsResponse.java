package com.sahil.affordmedurlshortener.dto;

import com.sahil.affordmedurlshortener.model.ClickDetail;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class UrlStatsResponse {
    private String id;
    private String shortLink;
    private String shortcode;
    private String originalUrl;
    private String createdAt;
    private String expireAt;
    private long clickCount;
    private List<ClickDetail> clickDetails;
}
