Meal Lab

Meal Lab is a JavaFX client for searching recipes from TheMealDB API and managing favorites and cooked meals.

Prerequisites
- JDK 17+
- Maven 3.8+

Install (build all modules)
```bash
mvn -pl meal-lab-api,meal-lab-app -am clean install
```

Run the app
```bash
mvn -pl meal-lab-app javafx:run
```

Run tests
```bash
mvn -pl meal-lab-app,meal-lab-api -am test
```
