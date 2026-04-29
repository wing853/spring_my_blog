package com.example.myblog.util;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PageDTO {
    private int data;
    private int display;
    private boolean active;
}
