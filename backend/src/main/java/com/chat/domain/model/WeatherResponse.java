package com.chat.domain.model;

import lombok.Data;

/**
 * 天气查询响应类
 * 用于返回天气查询结果
 */
@Data
public class WeatherResponse {
    /**
     * 城市名称
     */
    private String city;
    
    /**
     * 天气状况
     */
    private String weather;
    
    /**
     * 温度
     */
    private String temperature;
    
    /**
     * 湿度
     */
    private String humidity;
    
    /**
     * 风力
     */
    private String wind;
} 