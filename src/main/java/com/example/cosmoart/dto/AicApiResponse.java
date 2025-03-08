package com.example.cosmoart.dto;

import com.example.cosmoart.models.Config;
import com.example.cosmoart.models.Pagination;
import lombok.Data;

import java.util.List;

@Data
public class AicApiResponse<T> {
    private List<T> data;
    private Pagination pagination;
    private Config config;
}