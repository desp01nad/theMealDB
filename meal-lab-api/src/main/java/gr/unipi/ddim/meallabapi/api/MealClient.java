package gr.unipi.ddim.meallabapi.api;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.io.entity.EntityUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.fasterxml.jackson.databind.ObjectMapper;

import gr.unipi.ddim.meallabapi.exceptions.MealApiException;
import gr.unipi.ddim.meallabapi.models.ImageSize;
import gr.unipi.ddim.meallabapi.models.Meal;
import gr.unipi.ddim.meallabapi.models.MealResponse;

/** Client wrapper for TheMealDB API endpoints and image downloads. */
public class MealClient {

	private static final String BASE_URL = "https://www.themealdb.com/api/json/v1/1";
	private static final Logger logger = LogManager.getLogger(MealClient.class);

	private final CloseableHttpClient httpClient;
	private final ObjectMapper mapper;

	// Public Constructor (Default)
	/** Creates a client with a default HTTP client configuration. */
	public MealClient() {
		this(HttpClients.createDefault());
	}

	// Package-Private Constructor (For Tests)
	/** Creates a client with a provided HTTP client. */
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
			logger.error("Network error while calling TheMealDB: {}", url, e);
			throw new MealApiException("Network Error: Unable to conncect to theMealDB", e);
		}
	}

	private Meal getFirstMealOrNull(MealResponse response) {
		if (response.getMeals() != null && !response.getMeals().isEmpty()) {
			return response.getMeals().get(0);
		}
		return null;
	}

	/** Returns a randomly selected meal from the API. */
	public Meal getRandomMeal() {
		MealResponse response = executeRequest(BASE_URL + "/random.php");
		return getFirstMealOrNull(response);
	}

	/** Looks up a single meal by its id. */
	public Meal getMealById(String id) {
		MealResponse response = executeRequest(BASE_URL + "/lookup.php?i=" + id);
		return getFirstMealOrNull(response);
	}

	/** Searches meals by name and returns a list of results. */
	public List<Meal> searchMealsByName(String name) {
		String formatted = name.trim().replaceAll("\\s+", "_");
		MealResponse response = executeRequest(BASE_URL + "/search.php?s=" + formatted);
		if (response.getMeals() == null) {
			return Collections.emptyList();
		}
		return response.getMeals();
	}

	/** Filters meals by ingredient and returns a list of results. */
	public List<Meal> filterMealsByIngredient(String ingredient) {
		String formatted = ingredient.trim().replaceAll("\\s+", "_");
		MealResponse response = executeRequest(BASE_URL + "/filter.php?i=" + formatted);
		if (response.getMeals() == null) {
			return Collections.emptyList();
		}
		return response.getMeals();
	}

	/** Downloads a meal image using the provided size variant. */
	public byte[] fetchMealImage(Meal meal, ImageSize size) {
		String baseUrl = meal.getStrMealThumb();

		if (baseUrl == null || baseUrl.trim().isEmpty()) {
			throw new MealApiException("No image URL present for meal: " + meal.getStrMeal());
		}

		String finalUrl = baseUrl + size.getUrlSuffix();

		HttpGet request = new HttpGet(finalUrl);

		try {
			return this.httpClient.execute(request, response -> {
				int status = response.getCode();
				if (status >= 400) {
					throw new MealApiException("Failed to fetch image. HTTP Status: " + status);
				}
				return EntityUtils.toByteArray(response.getEntity());
			});

		} catch (IOException e) {
			logger.error("Network error while downloading image from {}", finalUrl, e);
			throw new MealApiException("Network error while downloading image", e);
		}
	}

}
