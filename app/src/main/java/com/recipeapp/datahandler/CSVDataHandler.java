package com.recipeapp.datahandler;

import java.util.ArrayList;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import com.recipeapp.model.Recipe;
import com.recipeapp.model.Ingredient;

public class CSVDataHandler implements DataHandler{
    private String filePath;

    public CSVDataHandler(){
        filePath = "app/src/main/resources/recipes.csv";
    }

    public CSVDataHandler(String filePath){
        this.filePath = filePath;
    }

    @Override
    public String getMode(){
        return "CSV";
    }

    @Override
    public ArrayList<Recipe> readData() throws IOException{
        // 代入するリストを作成
        ArrayList<Recipe> recipes = new ArrayList<>();
        // ファイルを開く
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))){
            // 一行ずつ読み込む
            String line;
            while((line = reader.readLine()) != null){
                // 料理名と材料を取得
                String[] items = line.split(",");
                ArrayList<Ingredient> ingredients = new ArrayList<>();
                for (int i = 1; i < items.length; i++){
                    ingredients.add(new Ingredient(items[i].trim()));
                }
                // Recipeクラスを作成して要素を収納
                Recipe recipe = new Recipe(items[0], ingredients);
                recipes.add(recipe);
            }
        } catch (IOException ex) {
            // IOExceptionはスローする
            throw ex;
        }
        return recipes;
    }

    @Override
    public void writeData(Recipe recipe) throws IOException{
        // ファイル書き込み準備
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath, true))){
            // 書き込む一行を作成
            // レシピ名を追加
            String line = recipe.getName();
            // 材料を追加
            for(Ingredient ingredient : recipe.getIngredients()){
                line += "," + ingredient.getName();
            }

            // ファイルに書き込み
            writer.write(line + "\n");
        }
    }

    @Override
    public ArrayList<Recipe> searchData(String keyword){
        // 検索結果のリスト作成
        ArrayList<Recipe> searchedRecipes = new ArrayList<>();
        try{
            
            // レシピ取得
            ArrayList<Recipe> recipes = readData();
            
            // レシピをループさせる
            for (Recipe recipe : recipes){
                // 検索用語の配列を作る
                String[] pairs = keyword.split("&");
                // 配列で検索するものをfalse、検索しないものがある場合はtrueとなるフラグ作成
                Boolean[] flags = new Boolean[pairs.length];
                if (pairs.length == 2){
                    flags[0] = false;
                    flags[1] = false;
                } else if (pairs.length == 1){
                    flags[0] = false;
                    flags[1] = true;
                }

                // 検索用語の配列をループさせる
                for (int i = 0; i < pairs.length; i++){
                    // 配列から、名前/材料どちらの検索かと検索する名称を分割
                    String[] items = pairs[i].split("=");
                    // 文字列が一致したらフラグtrueでcontinue
                    if ("name".equals(items[0])){
                        if(recipe.getName().contains(items[1])){
                            flags[i] = true;
                            continue;
                        }
                    }

                    if ("ingredient".equals(items[0])){
                        ArrayList<Ingredient> ingredients = recipe.getIngredients();
                        for (Ingredient ingredient : ingredients){
                            if(ingredient.getName().contains(items[1])){
                                flags[i] = true;
                                continue;
                            }
                        }
                    }
                }
                // フラグが両方trueのときレシピをリストに追加
                if (flags[0] && flags[1]) searchedRecipes.add(recipe);
            }
        } catch (IOException ex) {}
        return searchedRecipes;
    }
}