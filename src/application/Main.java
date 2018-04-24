package application;
	
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;


public class Main extends Application {
	
	private GridPane root;
	
	@Override
	public void start(Stage primaryStage) {
		try {
			
			root = new GridPane();
			root.setMinSize( 500, 500);
			root.setPadding( new Insets( 10, 10, 10, 10));
			root.setVgap( 5 );
			root.setHgap( 5 );
			root.setAlignment( Pos.CENTER );
			
			Scene scene = new Scene(root,400,400);
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			primaryStage.setScene(scene);
			primaryStage.show();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}
