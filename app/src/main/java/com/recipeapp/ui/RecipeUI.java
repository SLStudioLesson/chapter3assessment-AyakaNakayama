package com.recipeapp.ui;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import com.recipeapp.datahandler.DataHandler;
import com.recipeapp.model.Ingredient;
import com.recipeapp.model.Recipe;

public class RecipeUI {
    private BufferedReader reader;
    private DataHandler dataHandler;

    public RecipeUI(DataHandler dataHandler) {
        reader = new BufferedReader(new InputStreamReader(System.in));
        this.dataHandler = dataHandler;
    }
    
    public void displayMenu() {

        System.out.println("Current mode: " + dataHandler.getMode());

        while (true) {
            try {
                System.out.println();
                System.out.println("Main Menu:");
                System.out.println("1: Display Recipes");
                System.out.println("2: Add New Recipe");
                System.out.println("3: Search Recipe");
                System.out.println("4: Exit Application");
                System.out.print("Please choose an option: ");

                String choice = reader.readLine();

                switch (choice) {
                    case "1":
                        displayRecipes();
                        break;
                    case "2":
                        addNewRecipe();
                        break;
                    case "3":
                        searchRecipe();
                        break;
                    case "4":
                        System.out.println("Exiting the application.");
                        return;
                    default:
                        System.out.println("Invalid choice. Please select again.");
                        break;
                }
            } catch (IOException e) {
                System.out.println("Error reading input from user: " + e.getMessage());
            }
        }
    }

    private void displayRecipes(){
        System.out.println();
        try {
            // レシピデータを取得
            ArrayList<Recipe> recipes = dataHandler.readData();

            if (recipes.size() != 0){
                // レシピデータを出力
                System.out.println("Recipes:");
                System.out.println("-----------------------------------");
                for (Recipe recipe : recipes){
                    System.out.println("Recipe Name: " + recipe.getName());
                    System.out.print("Main Ingredients: ");
    
                    // 材料をすべて取得
                    ArrayList<Ingredient> ingredients = recipe.getIngredients();
                    for (int i = 0; i < ingredients.size(); i++){
                        System.out.print(ingredients.get(i).getName());
                        if(i < ingredients.size() - 1){
                            System.out.print(", ");
                        }
                    }
                    System.out.println();
                    System.out.println("-----------------------------------");
                }
            } else {
                // レシピが0件の時は出力
                System.out.println("No recipes available.");
            }
        } catch (IOException ex) {
            System.out.println("Error reading file: " + ex.getMessage());
        }
    }

    private void addNewRecipe(){
        System.out.println();
        System.out.println("Adding a new recipe.");
        try {
            // 入力受付
            // 名前を受け付ける
            System.out.print("Enter recipe name: ");
            String name = reader.readLine();
            
            // ループして材料を受け付ける。逐次Recipeインスタンスの材料リストに挿入
            System.out.println("Enter ingredients (type 'done' when finished):");
            ArrayList<Ingredient> ingredients = new ArrayList<>();
            while(true){
                System.out.print("Ingredient: ");
                String ingredient = reader.readLine();
                
                // doneが入力された場合はループを抜ける
                if ("done".equals(ingredient)) break;
                
                // 入力された材料名をIngredientインスタンスとして材料リストに追加
                ingredients.add(new Ingredient(ingredient));
            }
            // Recipeのインスタンスを作成してファイル書き込み
            dataHandler.writeData(new Recipe(name, ingredients));
            System.out.println("Recipe added successfully.");

        } catch (IOException ex) {
                System.out.println("Failed to add new recipe: " + ex.getMessage());
            }
        }
        
        private void searchRecipe(){
            try{
                // 入力受付
                System.out.print("Enter search query (e.g., 'name=Tomato&ingredient=Garlic'): ");
                String input = reader.readLine();
                
                // 検索
                ArrayList<Recipe> recipes = dataHandler.searchData(input);
                
                // 結果出力
                System.out.println("Matching Recipes:");
                for (Recipe recipe : recipes){
                    System.out.println("Recipe Name: " + recipe.getName());
                    System.out.print("Main Ingredients: ");
    
                    // 材料をすべて取得
                    ArrayList<Ingredient> ingredients = recipe.getIngredients();
                    for (int i = 0; i < ingredients.size(); i++){
                        System.out.print(ingredients.get(i).getName());
                        if(i < ingredients.size() - 1){
                            System.out.print(", ");
                        }
                    }
                    System.out.println();
                }
            } catch (IOException ex){
            System.out.println("Failed to add new recipe: " + ex.getMessage());
        }
    }
}
