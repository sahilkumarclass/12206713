package com.sahil.affordmedurlshortener.model;


import lombok.*;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ClickDetail {
    private LocalDateTime timestamp;
    private String referrer;
    private String geoLocation;
}
