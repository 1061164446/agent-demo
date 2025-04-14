package com.chat.domain.service;

import com.chat.domain.model.WeatherResponse;

/**
 * 天气查询服务接口
 */
public interface WeatherService {
    /**
     * 获取指定城市的天气信息
     * @param city 城市名称
     * @return 天气信息
     */
    WeatherResponse getWeather(String city);
} 