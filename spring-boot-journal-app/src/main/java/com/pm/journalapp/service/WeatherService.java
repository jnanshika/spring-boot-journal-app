package com.pm.journalapp.service;

import com.pm.journalapp.api.response.WeatherResponse;
import com.pm.journalapp.cache.AppCache;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Component
public class WeatherService {
    @Value("${weather.api.key}")
    private String apiKey;

    private final RestTemplate restTemplate;
    private final AppCache appCache;
    public WeatherService(RestTemplate restTemplate, AppCache appCache){
        this.restTemplate = restTemplate;
        this.appCache = appCache;
    }

    @Autowired
    private RedisService redisService;

    public WeatherResponse getWeather(String city){
        WeatherResponse cachedResponse = redisService.get("weather_of_" + city, WeatherResponse.class);
        if(cachedResponse !=null){
            log.info("Returned cached response");
            return cachedResponse;
        }
        else{
            log.info("Fetching response from weather API");
            String uri = appCache.appCache.get(AppCache.keys.weather_api.toString());
            String finalUri = uri.replace("<city>", city).replace("<apikey>", apiKey);

            ResponseEntity<WeatherResponse> response = restTemplate.exchange(finalUri, HttpMethod.GET, null, WeatherResponse.class);

            //TO-DO: can enhance exceptional handling based on http status code received.
            HttpStatusCode responseCode = response.getStatusCode();
            WeatherResponse responseBody = null;
            if(responseCode == HttpStatus.OK)
                responseBody = response.getBody();
            if(responseBody != null){
                redisService.set("weather_of_" + city, responseBody, 300l);
            }
            return responseBody;
        }

    }

//    public WeatherResponse getWeather(String city){
//        String uri = appCache.appCache.get(AppCache.keys.weather_api.toString());
//        String finalUri = uri.replace("<city>", city).replace("<apikey>", apiKey);
//
//        ResponseEntity<WeatherResponse> response = restTemplate.exchange(finalUri, HttpMethod.GET, null, WeatherResponse.class);
//
//        //TO-DO: can enhance exceptional handling based on http status code received.
//        HttpStatusCode responseCode = response.getStatusCode();
//        WeatherResponse responseBody = null;
//        if(responseCode == HttpStatus.OK)
//            responseBody = response.getBody();
//        return responseBody;
//    }
}
