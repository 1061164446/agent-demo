package com.chat.domain.model;

/**
 * 天气查询函数定义类
 * 用于定义Function Calling中的函数结构
 */
public class WeatherFunction {
    /**
     * 函数名称
     */
    public static final String NAME = "get_weather";
    
    /**
     * 函数描述
     */
    public static final String DESCRIPTION = "获取指定城市的天气信息";
    
    /**
     * 函数参数定义
     */
    public static final String PARAMETERS = """
        {
            "type": "object",
            "properties": {
                "city": {
                    "type": "string",
                    "description": "城市名称，例如：北京、上海"
                }
            },
            "required": ["city"]
        }
        """;
} 