Meal Lab

Meal Lab is a JavaFX client for searching recipes from TheMealDB API and managing favorites and cooked meals.

Prerequisites
- JDK 21+
- Maven 3.8+

Install (build API then app):
- `cd meal-lab-api && mvn clean install`
- `cd ../meal-lab-app && mvn clean install`

Test:
- `cd meal-lab-api && mvn test`
- `cd ../meal-lab-app && mvn test`

Run the app:
- `cd meal-lab-app && mvn javafx:run`

Eclipse (using embedded Maven)
- Import: `File` → `Import...` → `Maven` → `Existing Maven Projects`, select the repo root and finish.
- Use embedded Maven: `Window` → `Preferences` → `Maven` → `Installations`, select `Embedded` and apply.
- Install API: Right‑click `meal-lab-api` → `Run As` → `Maven build...`, Goals: `clean install`.
- Run tests: Right‑click `meal-lab-api` (then `meal-lab-app`) → `Run As` → `Maven test`.
- Run UI: Right‑click `meal-lab-app` → `Run As` → `Maven build...`, Goals: `javafx:run`.
