package com.anselm.moviecatalogservice.models;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
public class UserRating {
	
	//private String userId;
	private List<Rating> userRating;

}
