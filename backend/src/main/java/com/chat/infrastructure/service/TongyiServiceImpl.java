package com.chat.infrastructure.service;

import com.alibaba.dashscope.aigc.generation.Generation;
import com.alibaba.dashscope.aigc.generation.GenerationResult;
import com.alibaba.dashscope.aigc.generation.models.QwenParam;
import com.alibaba.dashscope.common.Message;
import com.alibaba.dashscope.common.Role;
import com.chat.domain.model.WeatherFunction;
import com.chat.infrastructure.config.TongyiConfig;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.context.annotation.Primary;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@Primary
public class TongyiServiceImpl implements AIChatService {

    private final TongyiConfig tongyiConfig;
    private final List<Message> conversationHistory;
    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;
    private static final int MAX_HISTORY_SIZE = 5;

    @Autowired
    public TongyiServiceImpl(TongyiConfig tongyiConfig, RestTemplate restTemplate, ObjectMapper objectMapper) {
        this.tongyiConfig = tongyiConfig;
        this.conversationHistory = new ArrayList<>();
        this.restTemplate = restTemplate;
        this.objectMapper = objectMapper;
        log.info("通义千问服务已初始化，模型: {}", tongyiConfig.getModel());
        log.info("通义千问API密钥: {}", tongyiConfig.getKey() != null ? "已设置" : "未设置");
    }

    @Override
    public String sendMessage(String content) {
        try {
            log.info("开始调用通义千问API，用户消息: {}", content);
            
            // 添加用户消息到历史
            Message userMessage = Message.builder().role(Role.USER.getValue()).content(content).build();
            conversationHistory.add(userMessage);
            
            // 保留历史最近的消息
            while (conversationHistory.size() > MAX_HISTORY_SIZE * 2) {
                conversationHistory.remove(0);
                conversationHistory.remove(0);
            }

            // 创建请求参数
            Generation gen = new Generation();
            QwenParam param = QwenParam.builder()
                    .apiKey(tongyiConfig.getKey())
                    .model(tongyiConfig.getModel())
                    .messages(conversationHistory)
                    .resultFormat("message")
                    .topP(0.8d)
                    .temperature(0.7f)
                    .maxTokens(800)
                    .functions(List.of(
                        Map.of(
                            "name", WeatherFunction.NAME,
                            "description", WeatherFunction.DESCRIPTION,
                            "parameters", objectMapper.readTree(WeatherFunction.PARAMETERS)
                        )
                    ))
                    .build();

            // 发送请求
            GenerationResult result = gen.call(param);
            String assistantResponse = result.getOutput().getChoices().get(0).getMessage().getContent();
            
            // 检查是否需要调用函数
            if (result.getOutput().getChoices().get(0).getMessage().getFunctionCall() != null) {
                String functionName = result.getOutput().getChoices().get(0).getMessage().getFunctionCall().getName();
                if (WeatherFunction.NAME.equals(functionName)) {
                    // 调用天气查询函数
                    String arguments = result.getOutput().getChoices().get(0).getMessage().getFunctionCall().getArguments();
                    Map<String, String> params = objectMapper.readValue(arguments, Map.class);
                    String city = params.get("city");
                    
                    // 调用天气查询API
                    String weatherResponse = restTemplate.postForObject(
                        "http://localhost:8080/api/weather/query",
                        Map.of("city", city),
                        String.class
                    );
                    
                    // 将天气信息添加到对话中
                    assistantResponse = "根据查询结果：" + weatherResponse;
                }
            }
            
            // 添加助手回复到历史
            Message assistantMessage = Message.builder()
                    .role(Role.ASSISTANT.getValue())
                    .content(assistantResponse)
                    .build();
            conversationHistory.add(assistantMessage);
            
            return assistantResponse;

        } catch (Exception e) {
            log.error("调用通义千问API失败", e);
            return "抱歉，AI服务出现错误：" + e.getMessage();
        }
    }
} 