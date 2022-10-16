package com.anselm.moviecatalogservice.resources;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.anselm.moviecatalogservice.models.CatalogItem;
import com.anselm.moviecatalogservice.models.Movie;
import com.anselm.moviecatalogservice.models.Rating;
import com.anselm.moviecatalogservice.models.UserRating;
import com.anselm.moviecatalogservice.services.MovieInfo;
import com.anselm.moviecatalogservice.services.UserRatingInfo;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;

@RestController
@RequestMapping("/catalog")
public class MovieCatalogResource {

	//only creates one instance => each call will use this!
	@Autowired
	private RestTemplate restTemplate;
	
	
	@Autowired MovieInfo movieInfo;
	  
	@Autowired UserRatingInfo userRatingInfo;
	 
	
	@RequestMapping("/{userId}")
	public List<CatalogItem> getCatalog(@PathVariable("userId") String userId) { 
		
		UserRating userRating = userRatingInfo.getUserRating(userId);
		return userRating.getUserRating().stream()
			.map(rating -> {
				return movieInfo.getCatalogItem(rating);
			})
			.collect(Collectors.toList());
	}
}