import com.recipeapp.datahandler.CSVDataHandler;
import com.recipeapp.datahandler.DataHandler;
import com.recipeapp.datahandler.JSONDataHandler;
import com.recipeapp.ui.RecipeUI;
import java.io.*;

public class App {

    public static void main(String[] args) {

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(System.in))) {
            System.out.println("Choose the file format:");
            System.out.println("1. CSV");
            System.out.println("2. JSON");
            System.out.print("Select (1/2): ");
            String choice = reader.readLine();
            
            // DataHandler型の変数用意
            DataHandler dataHandler;
            // 入力の判定
            switch (choice) {
                // 変数choiceが2のときJSON
                case "2":
                    dataHandler = new JSONDataHandler();
                    break;
            
                // 変数choiceが1または2以外のときCSV
                case "1":
                default:
                    dataHandler = new CSVDataHandler();
                    break;
            }
            // RecipeUIにdataHandlerを引き渡してdisplayMenu実行
            RecipeUI recipe = new RecipeUI(dataHandler);
            recipe.displayMenu();

        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
        }
    }
}