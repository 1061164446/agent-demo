package com.chat.infrastructure.controller;

import com.chat.domain.model.WeatherFunction;
import com.chat.domain.model.WeatherRequest;
import com.chat.domain.model.WeatherResponse;
import com.chat.domain.service.WeatherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 天气查询控制器
 * 处理天气查询相关的HTTP请求
 */
@RestController
@RequestMapping("/api/weather")
public class WeatherController {
    
    @Autowired
    private WeatherService weatherService;
    
    /**
     * 获取天气信息
     * @param request 包含城市名称的请求
     * @return 天气信息
     */
    @PostMapping("/query")
    public WeatherResponse getWeather(@RequestBody WeatherRequest request) {
        return weatherService.getWeather(request.getCity());
    }
    
    /**
     * 获取天气查询函数的定义
     * @return 函数定义JSON
     */
    @PostMapping("/function")
    public String getWeatherFunction() {
        return String.format("""
            {
                "name": "%s",
                "description": "%s",
                "parameters": %s
            }
            """, 
            WeatherFunction.NAME,
            WeatherFunction.DESCRIPTION,
            WeatherFunction.PARAMETERS
        );
    }
} 