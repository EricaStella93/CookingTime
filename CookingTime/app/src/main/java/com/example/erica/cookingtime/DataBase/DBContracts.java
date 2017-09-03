package com.example.erica.cookingtime.DataBase;

public class DBContracts {

    public static final String DATABASE_NAME = "cookingtime.db";

    public static class FridgeTable {
        public static final String TABLE_NAME = "fridge";
        public static final String COLUMN_ID = "_id";
        public static final String COLUMN_NAME = "name";
        public static final String COLUMN_CATEGORY = "category";
        public static final String COLUMN_QUANTITY = "quantity";
        public static final String COLUMN_UDM = "udm";
        public static final String COLUMN_BEST_BEFORE = "bestBefore";
    }

    public static class ShoppingListTable {
        public static final String TABLE_NAME = "shopping_list";
        public static final String COLUMN_ID = "_id";
        public static final String COLUMN_NAME = "name";
        public static final String COLUMN_CATEGORY = "category";
        public static final String COLUMN_QUANTITY = "quantity";
        public static final String COLUMN_UDM = "udm";
        public static final String COLUMN_RECIPE = "recipe";
        public static final String COLUMN_GOT_IT = "got_it";
    }

    public static class DietsTable {
        public static final String TABLE_NAME = "diets";
        public static final String COLUMN_ID = "_id";
        public static final String COLUMN_NAME = "name";
        public static final String COLUMN_CHOSEN = "chosen";
        public static final String COLUMN_YUMMLY_CODE = "code";
    }

    public static class AllergiesTable {
        public static final String TABLE_NAME = "allergies";
        public static final String COLUMN_ID = "_id";
        public static final String COLUMN_NAME = "name";
        public static final String COLUMN_CHOSEN = "chosen";
        public static final String COLUMN_YUMMLY_CODE = "code";
    }

    public static class DislikedIngredientsTable {
        public static final String TABLE_NAME = "disliked_ingredients";
        public static final String COLUMN_ID = "_id";
        public static final String COLUMN_NAME = "name";
    }

    public static class CuisineTable {
        public static final String TABLE_NAME = "cuisines";
        public static final String COLUMN_ID = "_id";
        public static final String COLUMN_NAME = "name";
        public static final String COLUMN_CHOSEN = "chosen";
        public static final String COLUMN_YUMMLY_CODE = "code";
    }

    public static class CoursesTable {
        public static final String TABLE_NAME = "courses";
        public static final String COLUMN_ID = "_id";
        public static final String COLUMN_NAME = "name";
        public static final String COLUMN_CHOSEN = "chosen";
        public static final String COLUMN_YUMMLY_CODE = "code";
    }

    public static class FavouritesTable {
        public static final String TABLE_NAME = "favourites";
        public static final String COLUMN_ID = "_id";
        public static final String COLUMN_RECIPE_ID = "recipe_id";
        public static final String COLUMN_RECIPE_NAME = "recipe_name";
        public static final String COLUMN_RATING = "rating";
        public static final String COLUMN_TOTAL_TIME_IN_SECONDS = "totalTimeInSeconds";
        public static final String COLUMN_SERVINGS = "numServings";
        public static final String COLUMN_SOURCE_RECIPE_URL = "sourceRecipeUrl";
    }

    public static class IncludedIngrTable{
        public static final String TABLE_NAME = "included_ingrs";
        public static final String COLUMN_ID = "_id";
        public static final String COLUMN_NAME = "name";

    }

    public static class IngredientsTable{
        public static final String TABLE_NAME = "recipe_ingrs";
        public static final String COLUMN_ID = "_id";
        public static final String COLUMN_RECIPE_ID = "recipeId";
        public static final String COLUMN_INGREDIENT = "ingredient";
    }

    public static class IngredientLinesTable{
        public static final String TABLE_NAME = "recipe_ingrs_lines";
        public static final String COLUMN_ID = "_id";
        public static final String COLUMN_RECIPE_ID = "recipeId";
        public static final String COLUMN_INGREDIENT = "ingredient";
    }

    public static class ImagesTable{
        public static final String TABLE_NAME = "images";
        public static final String COLUMN_ID = "_id";
        public static final String COLUMN_RECIPE_ID = "recipeId";
        public static final String COLUMN_LARGE_IMAGE = "largeImage";

    }
}
