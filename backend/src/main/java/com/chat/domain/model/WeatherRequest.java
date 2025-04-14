package com.chat.domain.model;

import lombok.Data;

/**
 * 天气查询请求类
 * 用于接收Function Calling中的参数
 */
@Data
public class WeatherRequest {
    /**
     * 城市名称
     */
    private String city;
} 