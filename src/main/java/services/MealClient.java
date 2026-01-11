package services;

import java.io.IOException;
import java.util.List;

import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.io.entity.EntityUtils;

import com.fasterxml.jackson.databind.ObjectMapper;

public class MealClient {

	private static final String BASE_URL = "https://www.themealdb.com/api/json/v1/1";

	public static Meal getRandomMeal() throws IOException {
		CloseableHttpClient httpClient = HttpClients.createDefault();

		try {
			String endpoint = BASE_URL + "/random.php";
			HttpGet request = new HttpGet(endpoint);
			String responseBody = httpClient.execute(request, response -> EntityUtils.toString(response.getEntity()));

			MealResponse deserializedResponse = new ObjectMapper().readValue(responseBody, MealResponse.class);

			if (deserializedResponse.getMeals() != null && !deserializedResponse.getMeals().isEmpty()) {
				return deserializedResponse.getMeals().get(0);
			}

			return null;
		} finally {
			httpClient.close();
		}
	}

	public static Meal getMealById(String id) throws IOException {
		CloseableHttpClient httpClient = HttpClients.createDefault();

		try {
			String endpoint = BASE_URL + "/lookup.php?i=" + id;
			HttpGet request = new HttpGet(endpoint);
			String responseBody = httpClient.execute(request, response -> EntityUtils.toString(response.getEntity()));

			MealResponse deserializedResponse = new ObjectMapper().readValue(responseBody, MealResponse.class);

			if (deserializedResponse.getMeals() != null && !deserializedResponse.getMeals().isEmpty()) {
				return deserializedResponse.getMeals().get(0);
			}

			return null;
		} finally {
			httpClient.close();
		}
	}

	public static List<Meal> searchMealsByName(String name) throws IOException {
		CloseableHttpClient httpClient = HttpClients.createDefault();

		try {
			String endpoint = BASE_URL + "/search.php?s=" + name;
			HttpGet request = new HttpGet(endpoint);
			String responseBody = httpClient.execute(request, response -> EntityUtils.toString(response.getEntity()));

			MealResponse deserializedResponse = new ObjectMapper().readValue(responseBody, MealResponse.class);

			return deserializedResponse.getMeals();
		} finally {
			httpClient.close();
		}

	}

	public static List<Meal> filterMealsByIngredient(String ingredient) throws IOException {
		CloseableHttpClient httpClient = HttpClients.createDefault();

		try {
			String endpoint = BASE_URL + "/filter.php?i=" + ingredient;
			HttpGet request = new HttpGet(endpoint);
			String responseBody = httpClient.execute(request, response -> EntityUtils.toString(response.getEntity()));

			MealResponse deserializedResponse = new ObjectMapper().readValue(responseBody, MealResponse.class);

			return deserializedResponse.getMeals();
		} finally {
			httpClient.close();
		}

	}
}