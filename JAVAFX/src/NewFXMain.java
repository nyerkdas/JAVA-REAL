import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.util.Random;

public class NewFXMain extends Application {
    private Image backpic, birdpic, welcomescreen, tpipe, bpipe;
    private int game, score, highscore, x, y, vertical;
    private int[] wallx = new int[2];
    private int[] wally = new int[2];
    private Canvas canvas;
    private GraphicsContext gc;
    private Random random;

    private static final int PIPE_SPEED = 6;
    private static final int PIPE_OFFSET = 100;
    private double pipeWidth = 150;
    private double pipeHeight = 300;
    private double pipeGap = 550;

    @Override
    public void start(Stage primaryStage) {
        Pane root = new Pane();
        canvas = new Canvas(600, 800);
        gc = canvas.getGraphicsContext2D();
        root.getChildren().add(canvas);
        Scene scene = new Scene(root, 600, 800);
        primaryStage.setTitle("Flappy Bird");
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.show();

        backpic = new Image("bg.png");
        birdpic = new Image("catfly.png");
        welcomescreen = new Image("cat1.jpg");
        tpipe = new Image("tube_bottom.png");
        bpipe = new Image("tube_top.png");

        game = 1;
        score = 0;
        highscore = 0;
        x = -200;
        vertical = 0;
        wallx[0] = 600;
        wallx[1] = 900;
        random = new Random();

        canvas.setOnMousePressed(e -> mousePressed());

        AnimationTimer gameLoop = new AnimationTimer() {
            @Override
            public void handle(long now) {
                update();
                render();
            }
        };
        gameLoop.start();
    }

    private void update() {
        if (game == 0) {
            x -= 5;
            vertical += 1;
            y += vertical;

            for (int i = 0; i < 2; i++) {
                if (wallx[i] < -tpipe.getWidth() / 2) {
                    wally[i] = random.nextInt(600) + 200; // Adjust the random placement range
                    wallx[i] = (int) canvas.getWidth();
                }
                if (wallx[i] == canvas.getWidth() / 2) {
                    highscore = Math.max(++score, highscore);
                }
                wallx[i] -= PIPE_SPEED;
            }
        
            if (x == -backpic.getWidth()) x = 0;
            if (y > canvas.getHeight() || y + birdpic.getHeight() <= 0)
                game = 1;
        }
    }

    private void render() {
        gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
        if (game == 0) {
            gc.drawImage(backpic, x, 0, canvas.getWidth(), canvas.getHeight());
            gc.drawImage(backpic, x + backpic.getWidth(), 0, canvas.getWidth(), canvas.getHeight());
            for (int i = 0; i < 2; i++) {
                double topPipeY = canvas.getHeight() / 150 - pipeHeight / 215 - pipeGap / 150;
                double bottomPipeY = canvas.getHeight() / 3 + pipeHeight / 3 + pipeGap / 3;

                gc.drawImage(bpipe, wallx[i], topPipeY, pipeWidth, pipeHeight);
                gc.drawImage(tpipe, wallx[i], bottomPipeY, pipeWidth, pipeHeight);
            }
            gc.drawImage(birdpic, canvas.getWidth() / 15, y);
            gc.setFill(Color.BLACK);
            gc.fillText("Score: " + score, 10, 20);
        } else {
            gc.drawImage(welcomescreen, canvas.getWidth() / 2 - welcomescreen.getWidth() / 2,
                    canvas.getHeight() / 2 - welcomescreen.getHeight() / 2);
            gc.setFill(Color.BLACK);
            gc.fillText("High Score: " + highscore, 50, 130);
        }
    }

    private void mousePressed() {
        vertical = -15;
        if (game == 1) {
            y = (int) canvas.getHeight() / 2;
            score = 0;
            game = 0;
            resetPipes();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }

    private void resetPipes() {
        wallx[0] = 600;
    wallx[1] = 900;
    for (int i = 0; i < 2; i++) {
        wally[i] = random.nextInt(600) + 200; // Adjust the random placement range
    }
}
    }