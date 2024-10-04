package com.spring.blog.controller.api;

import com.spring.blog.service.WeatherService;
import com.spring.blog.service.dto.response.Item;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class WeatherApiController {

    private final WeatherService weatherService;

    @GetMapping("/api/weather")
    public ApiResponse<List<Item>> getWeather(HttpServletRequest request){

        int nx = (Integer) request.getAttribute("nx");
        int ny = (Integer) request.getAttribute("ny");

        List<Item> weatherData = weatherService.getWeatherData(nx, ny);

        return ApiResponse.ok(weatherData);
    }
}