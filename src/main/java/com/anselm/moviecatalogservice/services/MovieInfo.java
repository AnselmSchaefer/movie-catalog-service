package com.anselm.moviecatalogservice.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.anselm.moviecatalogservice.models.CatalogItem;
import com.anselm.moviecatalogservice.models.Movie;
import com.anselm.moviecatalogservice.models.Rating;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;

@Service
public class MovieInfo {
	
	//only creates one instance => each call will use this!
	@Autowired
	private RestTemplate restTemplate;

	@HystrixCommand(
		// circuit Breaker
		fallbackMethod = "getFallbackCatalogItem",
		commandProperties = {
				@HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "2000"),
				@HystrixProperty(name = "circuitBreaker.requestVolumeThreshold", value = "5"),
				@HystrixProperty(name = "circuitBreaker.errorThresholdPercentage", value = "50"),
				@HystrixProperty(name = "circuitBreaker.sleepWindowInMilliseconds", value = "5000")
				
		},
		// Bulkhead Pattern
		threadPoolKey = "movieInfoPool",
		threadPoolProperties = {
				@HystrixProperty(name = "coreSize", value = "20"),
				@HystrixProperty(name = "maxQueueSize", value = "10")
		}
	)
	public CatalogItem getCatalogItem(Rating rating) {
		// For each movie ID, call movie info service and get details
		//localhost:8082
		//movie-info-service
		Movie movie = restTemplate.getForObject("http://movie-info-service/movies/" + rating.getMovieId(), Movie.class);
		return new CatalogItem(movie.getName(), movie.getDesc(), rating.getRating());
	}
	
	public CatalogItem getFallbackCatalogItem(Rating rating) {
		return new CatalogItem("Movie name not found", "", rating.getRating());
	}

}
