package com.spring.blog.service;

import com.spring.blog.service.dto.response.Item;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.net.URISyntaxException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.IntStream;

@Slf4j
@Service
@RequiredArgsConstructor
public class WeatherService {

    private final RestTemplate restTemplate;

    @Value("${weather.serviceKey}")
    private String serviceKey;

    @Cacheable(value = "weatherData", key = "#nx + '-' + #ny")
    public List<Item> getWeatherData(int nx, int ny) {

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
        String now = LocalDate.now().format(formatter);

        String uri = UriComponentsBuilder.fromHttpUrl("http://apis.data.go.kr/1360000/VilageFcstInfoService_2.0/getVilageFcst")
                .queryParam("pageNo", 1)
                .queryParam("numOfRows", 1000)
                .queryParam("dataType", "JSON")
                .queryParam("base_date", now)
                .queryParam("base_time", "0200")
                .queryParam("nx", nx)
                .queryParam("ny", ny)
                .toUriString();

        uri += "&serviceKey=" + serviceKey;

        try {
            URI requestUri = new URI(uri);

            ResponseEntity<String> stringResponse = restTemplate.exchange(requestUri, HttpMethod.GET, null, String.class);

            JSONObject jsonObj = new JSONObject(stringResponse.getBody());
            JSONObject response = jsonObj.getJSONObject("response");
            JSONObject body = response.getJSONObject("body");
            JSONObject items = body.getJSONObject("items");
            JSONArray itemArray = items.getJSONArray("item");

            return IntStream.range(0, itemArray.length())
                    .mapToObj(itemArray::getJSONObject)
                    .filter(categoryPredicate())    //필요한 category만 추출
                    .filter(fcstDatePredicate(now)) //오늘 날짜 정보만 추출
                    .filter(fcstTimePredicate())    //08시 이후 정보만 추출(TMN 제외)
                    .map(item -> new Item(
                            item.getString("category"),
                            item.getString("fcstDate"),
                            item.getString("fcstTime"),
                            item.getString("fcstValue")
                    ))
                    .toList();

        } catch (URISyntaxException ex) {
            throw new RuntimeException("날씨 API 요청 중 오류 발생", ex);
        }
    }

    @Scheduled(cron = "0 0 * * * ?")
    @CacheEvict(value = "weatherData", allEntries = true)
    public void refreshWeatherDate() {
        log.info("매시 정각 weatherData 캐시 데이터 제거");
    }

    //08시 이후 정보만 추출(TMN(일 최저 온도) 제외)
    private Predicate<JSONObject> fcstTimePredicate() {
        return item -> {
            String fcstTime = item.getString("fcstTime");
            String category = item.getString("category");

            if (category.equals("TMN")) {
                return true;
            }

            return fcstTime.compareTo("0800") >= 0;

        };
    }

    //오늘 날짜 정보만 추출
    private Predicate<JSONObject> fcstDatePredicate(String now) {
        return item -> {
            String fcstDate = item.getString("fcstDate");
            return fcstDate.equals(now);
        };
    }

    //필요한 category만 추출
    private Predicate<JSONObject> categoryPredicate() {
        return item -> {
            String category = item.getString("category");
            return category.equals("POP") || category.equals("TMP") || category.equals("TMN") || category.equals("TMX");
        };
    }
}