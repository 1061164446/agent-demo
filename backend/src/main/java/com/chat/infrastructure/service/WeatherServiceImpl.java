package com.chat.infrastructure.service;

import com.chat.domain.model.WeatherResponse;
import com.chat.domain.service.WeatherService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@Slf4j
@Service
public class WeatherServiceImpl implements WeatherService {

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;
    private final String weatherApiKey;
    private final String weatherApiUrl;

    public WeatherServiceImpl(
            RestTemplate restTemplate,
            ObjectMapper objectMapper,
            @Value("${weather.api.key}") String weatherApiKey,
            @Value("${weather.api.url}") String weatherApiUrl) {
        this.restTemplate = restTemplate;
        this.objectMapper = objectMapper;
        this.weatherApiKey = weatherApiKey;
        this.weatherApiUrl = weatherApiUrl;
    }

    @Override
    public WeatherResponse getWeather(String city) {
        try {
            log.info("开始查询天气，城市: {}", city);
            
            // 调用天气API
            String url = String.format("%s?key=%s&location=%s", weatherApiUrl, weatherApiKey, city);
            String response = restTemplate.getForObject(url, String.class);
            
            // 解析响应
            JsonNode root = objectMapper.readTree(response);
            JsonNode now = root.path("now");
            
            WeatherResponse weatherResponse = new WeatherResponse();
            weatherResponse.setCity(city);
            weatherResponse.setWeather(now.path("text").asText());
            weatherResponse.setTemperature(now.path("temp").asText() + "°C");
            weatherResponse.setHumidity(now.path("humidity").asText() + "%");
            weatherResponse.setWind(now.path("windDir").asText() + " " + now.path("windScale").asText() + "级");
            
            log.info("天气查询成功: {}", weatherResponse);
            return weatherResponse;
            
        } catch (Exception e) {
            log.error("天气查询失败", e);
            throw new RuntimeException("天气查询失败: " + e.getMessage());
        }
    }
} 