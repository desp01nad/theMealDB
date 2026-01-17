package services.api;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.io.entity.EntityUtils;

import com.fasterxml.jackson.databind.ObjectMapper;

import services.exceptions.MealApiException;
import services.models.Meal;
import services.models.MealResponse;

public class MealClient {
	
	private static final String BASE_URL = "https://www.themealdb.com/api/json/v1/1";
	
	private final CloseableHttpClient httpClient;
	private final ObjectMapper mapper;
	
	// Public Constructor (Default)
	public MealClient() {
		this(HttpClients.createDefault());
	}
	
	// Package-Private Constructor (For Tests)
	MealClient(CloseableHttpClient httpClient) {
		this.httpClient = httpClient;
		this.mapper = new ObjectMapper();
	}
	
	
	private MealResponse executeRequest(String url) {
		HttpGet request = new HttpGet(url);
		
		try {
			return this.httpClient.execute(request, response -> {
				int status = response.getCode();
				
				// Handle Server Errors 
				if (status >= 400) {
					throw new MealApiException("API request faiiled with error status: " + status);
				}
						
			String body = EntityUtils.toString(response.getEntity());
			
			// Handle Empty Responses
			if (body == null || body.trim().isEmpty()) {
				throw new MealApiException("API returned an empty response");
			}
			
			// Handle Parsing Responses
			try {
				return mapper.readValue(body, MealResponse.class);
				} catch (Exception e) {
					throw new MealApiException("Failed to parse API JSON response", e);
				}		
		});
			
		
		} catch (IOException e) {
			// Handle Network Errors
			throw new MealApiException("Network Error: Unable to conncect to theMealDB", e);
		}
	}
	
	
	private Meal getFirstMealOrNull(MealResponse response) {
		if (response.getMeals() != null && !response.getMeals().isEmpty()) {
			return response.getMeals().get(0);
		}
		return null;
	}
	

	public Meal getRandomMeal() {
		MealResponse response = executeRequest(BASE_URL + "/random.php");
		return getFirstMealOrNull(response);
	}
		
	public Meal getMealById(String id) {
		MealResponse response = executeRequest(BASE_URL + "/lookup.php?i=" + id);
		return getFirstMealOrNull(response);
	}

	public List<Meal> searchMealsByName(String name) {
		MealResponse response = executeRequest(BASE_URL + "/search.php?s=" + name);
		if (response.getMeals() == null) {
			return Collections.emptyList();
		}
		return response.getMeals();
	}
	
	public List<Meal> filterMealsByIngredient(String ingredient) {
		MealResponse response = executeRequest(BASE_URL + "/filter.php?i=" + ingredient);
		if (response.getMeals() == null) {
			return Collections.emptyList();
		}
		return response.getMeals();
	}
	
}