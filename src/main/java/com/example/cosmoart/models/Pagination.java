package com.example.cosmoart.models;

import lombok.Data;

@Data
public class Pagination {
    private int total;
    private int limit;
    private int offset;
    private int total_pages;
    private int current_page;
    private String next_url;
}