package services.api;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.when;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;

import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.core5.http.ClassicHttpResponse;
import org.apache.hc.core5.http.HttpEntity;
import org.apache.hc.core5.http.io.HttpClientResponseHandler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import services.exceptions.MealApiException;
import services.models.Meal;

@ExtendWith(MockitoExtension.class)
class MealClientTest {

	@Mock
	private CloseableHttpClient mockHttpClient;

	@Mock
	private ClassicHttpResponse mockResponse;

	@Mock
	private HttpEntity mockEntity;

	private MealClient mealClient;

	@BeforeEach
	void setUp() {
		mealClient = new MealClient(mockHttpClient);
	}

	// Helper to setup the mock response 
	@SuppressWarnings("unchecked")
	private void setupMockResponse(int statusCode, String jsonBody) throws IOException {
		// Setup Status Code
		when(mockResponse.getCode()).thenReturn(statusCode);

		// Setup Response Body (if present)
		if (jsonBody != null) {
			when(mockEntity.getContent())
					.thenReturn(new ByteArrayInputStream(jsonBody.getBytes(StandardCharsets.UTF_8)));
			lenient().when(mockResponse.getEntity()).thenReturn(mockEntity);
		}

		// Setup the Execute Logic (Intercept the Lambda)
		when(mockHttpClient.execute(any(HttpGet.class), any(HttpClientResponseHandler.class)))
				.thenAnswer(invocation -> {
					// Get the 'handler' lambda that was passed in by MealClient
					HttpClientResponseHandler<?> handler = invocation.getArgument(1);

					// Manually run that handler with our mockResponse
					return handler.handleResponse(mockResponse);
				});
	}
	
	
	// executeRequest Tests 
	
	@Test
    void executeRequest_ShouldThrowException_WhenResponseIsEmpty() throws Exception {
        // Arrange: API returns 200 OK but empty body
        setupMockResponse(200, "");

        // Act & Assert
        Exception e = assertThrows(MealApiException.class, () -> mealClient.getRandomMeal());
        assertTrue(e.getMessage().contains("empty response"));
    }
	
	@Test
    void executeRequest_ShouldThrowException_WhenServerReturnsError() throws Exception {
        // Arrange: Simulate a 404 Not Found
        setupMockResponse(404, null);

        // Act & Assert
        Exception e = assertThrows(MealApiException.class, () -> mealClient.getRandomMeal());
        assertTrue(e.getMessage().contains("error status: 404"));
    }
	
	
	// getRandomMeal Tests 

    @Test
    void getRandomMeal_ShouldReturnMeal_WhenApiReturnsData() throws Exception {
        // Arrange
        String json = "{ \"meals\": [{ \"idMeal\": \"999\", \"strMeal\": \"Surprise Stew\" }] }";
        setupMockResponse(200, json);

        // Act
        Meal result = mealClient.getRandomMeal();

        // Assert
        assertEquals("Surprise Stew", result.getStrMeal());
    }
    
    
    // getMealById Tests 

    @Test
    void getMealById_ShouldReturnMeal_WhenIdExists() throws Exception {
        // Arrange
        String json = "{ \"meals\": [{ \"idMeal\": \"52772\", \"strMeal\": \"Teriyaki Chicken\" }] }";
        setupMockResponse(200, json);

        // Act
        Meal result = mealClient.getMealById("52772");

        // Assert
        assertEquals("Teriyaki Chicken", result.getStrMeal());
    }
    
    @Test
    void getMealById_ShouldReturnNull_WhenIdDoesNotExist() throws Exception {
        // Arrange: API returns { "meals": null } for invalid IDs
        String json = "{ \"meals\": null }";
        setupMockResponse(200, json);

        // Act
        Meal result = mealClient.getMealById("00000");

        // Assert
        assertEquals(null, result);
    }
    
    
    // searchMealsByName Tests (Remaining) 
	
	@Test
    void searchMealsByName_ShouldReturnList_WhenMultipleMealsFound() throws Exception {
        // Arrange
        String json = "{ \"meals\": [ " +
                      "{ \"idMeal\": \"1\", \"strMeal\": \"Burger\" }, " +
                      "{ \"idMeal\": \"2\", \"strMeal\": \"Cheeseburger\" } " +
                      "] }";
        setupMockResponse(200, json);

        // Act
        List<Meal> results = mealClient.searchMealsByName("Burger");

        // Assert
        assertEquals(2, results.size());
        assertEquals("Burger", results.get(0).getStrMeal());
    }
	
	@Test
    void searchMealsByName_ShouldReturnEmptyList_WhenNoMealsFound() throws Exception {
        // Arrange: API returns null for "meals" if search has no results
        String json = "{ \"meals\": null }";
        setupMockResponse(200, json);

        // Act
        List<Meal> results = mealClient.searchMealsByName("InvalidFoodName");

        // Assert
        assertTrue(results.isEmpty());
    }
	
	
	// filterMealsByIngredient Tests 
	
	@Test
    void filterMealsByIngredient_ShouldReturnList_WhenMatchesFound() throws Exception {
        // Arrange
        String json = "{ \"meals\": [ " +
                      "{ \"strMeal\": \"Chicken Curry\" }, " +
                      "{ \"strMeal\": \"Chicken Soup\" } " +
                      "] }";
        setupMockResponse(200, json);

        // Act
        List<Meal> results = mealClient.filterMealsByIngredient("Chicken");

        // Assert
        assertEquals(2, results.size());
    }
	
	@Test
    void filterMealsByIngredient_ShouldReturnEmptyList_WhenNoMatchesFound() throws Exception {
        // Arrange
        String json = "{ \"meals\": null }";
        setupMockResponse(200, json);

        // Act
        List<Meal> results = mealClient.filterMealsByIngredient("Rocks");

        // Assert
        assertTrue(results.isEmpty());
    }
	
	
	
}
