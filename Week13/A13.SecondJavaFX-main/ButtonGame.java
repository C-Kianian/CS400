import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.Group;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.application.Platform;
import java.util.Random;

public class ButtonGame extends Application {

    private int score = 0;
    @Override
    public void start(final Stage stage) {
	BorderPane root = new BorderPane();

	Label top = new Label("Score: " + score);
	root.setTop(top);

	Button exit = new Button("Exit");
	root.setBottom(exit);
	exit.setOnAction( action -> {Platform.exit();});
	exit.requestFocus();

	Pane pn = new Pane();
	root.setCenter(pn);

	Random gen = new Random();

	Button click = new Button("Click me!");
	click.setLayoutX(0);
	click.setLayoutY(0);
	pn.getChildren().add(click);

	for (int i = 0; i < 8; i++){
		Button button = new Button("Click me?");
		button.setLayoutX(0);
        	button.setLayoutY(0);
		pn.getChildren().add(button);
	}
	Button[] buttons = pn.getChildren().toArray(new Button[8]);

	int i = 0;
	for (Button button: buttons){
		if (i == 0){
			button.setOnAction(action -> {this.updateScore(1,top);
		        scrambleButtons(gen, buttons);
			exit.requestFocus();});
			i++;
		}
		else {
			button.setOnAction(action -> {this.updateScore(-1, top);
	                exit.requestFocus();
			scrambleButtons(gen, buttons);});
		}
	}

	scrambleButtons(gen, buttons);
        Scene scene = new Scene(root, 600, 500);
        stage.setTitle("ButtonGame");
        stage.setScene(scene);
        stage.show();
    }

    private void scrambleButtons(Random randGen, Button[] list){
	for (Button button: list){
		button.setLayoutX(randGen.nextInt(500));
		button.setLayoutY(randGen.nextInt(400));
	}
    }

    private void updateScore(int val, Label lbl){
	this.score += val;
        lbl.setText("Score: " + this.score);
    }

    public static void main(String[] args) {
        Application.launch(args);
    }
    
}
