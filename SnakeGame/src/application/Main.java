package application;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;

public class Main extends Application {

	static int speed = 5;
	static int size = 25;
	static int foodX = 0;
	static int foodY = 0;
	static List<Vector> snake = new ArrayList<>();
	static Direction direction = Direction.LEFT;
	static boolean endGame = false;
	


	public void start(Stage primaryStage) {
		try {
			newFood();

			VBox root = new VBox();
			Canvas c = new Canvas(size * size, size * size);
			GraphicsContext gc = c.getGraphicsContext2D();
			root.getChildren().add(c);

			new AnimationTimer() {
				long lastTick = 0;

				public void handle(long now) {
					if (lastTick == 0) {
						lastTick = now;
						tick(gc);
						return;
					}

					if (now - lastTick > 10000000 / speed) {
						lastTick = now;
						tick(gc);
					}
				}

			}
			.start();

			Scene scene = new Scene(root, size * 20, size * 20);

			// control
			scene.addEventFilter(KeyEvent.KEY_PRESSED, key -> {
				if (key.getCode() == KeyCode.W) {
					direction = Direction.UP;
				}
				if (key.getCode() == KeyCode.A) {
					direction = Direction.LEFT;
				}
				if (key.getCode() == KeyCode.S) {
					direction = Direction.DOWN;
				}
				if (key.getCode() == KeyCode.D) {
					direction = Direction.RIGHT;
				}

			});

			// add start snake parts
			snake.add(new Vector(size / 2, size / 2));
			snake.add(new Vector(size / 2, size / 2));
			snake.add(new Vector(size / 2, size / 2));
			//If you do not want to use css style, you can just delete the next line.
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			primaryStage.setScene(scene);
			primaryStage.setTitle("SNAKE GAME");
			primaryStage.show();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// tick
	public static void tick(GraphicsContext gc) {
		if (endGame) {
			gc.setFill(Color.RED);
			gc.setFont(new Font("", 50));
			gc.fillText("GAME OVER", 100, 250);
			return;
		}

		for (int i = snake.size() - 1; i >= 1; i--) {
			snake.get(i).x = snake.get(i - 1).x;
			snake.get(i).y = snake.get(i - 1).y;
		}

		switch (direction) {
		case UP:
			snake.get(0).y--;
			if (snake.get(0).y < 0) {
				endGame = true;
			}
			break;
		case DOWN:
			snake.get(0).y++;
			if (snake.get(0).y > size) {
				endGame = true;
			}
			break;
		case LEFT:
			snake.get(0).x--;
			if (snake.get(0).x < 0) {
				endGame = true;
			}
			break;
		case RIGHT:
			snake.get(0).x++;
			if (snake.get(0).x > size) {
				endGame = true;
			}
			break;

		}

		// eat food
		if (foodX == snake.get(0).x && foodY == snake.get(0).y) {
			snake.add(new Vector(-1, -1));
			newFood();
		}

		// self destroy
		for (int i = 1; i < snake.size(); i++) {
			if(snake.get(0).x == snake.get(i).x && snake.get(0).y == snake.get(i).y) {
				endGame = true;
			}
		}

		// fill
		// background
		gc.setFill(Color.BLACK);
		gc.fillRect(0, 0, size * size, size * size);

		// score
		gc.setFill(Color.WHITE);
		gc.setFont(new Font("", 30));
		gc.fillText("Score: " + (speed - 6), 10, 30);

		
		gc.setFill(Color.WHITE);
		gc.fillOval(foodX * 20, foodY * 20, 20, 20);

		// snake
		for (Vector c : snake) {
			gc.setFill(Color.LIGHTGREEN);
			gc.fillRect(c.x * 20, c.y * 20, 20 - 1, 20 - 1);
			gc.setFill(Color.GREEN);
			gc.fillRect(c.x * 20, c.y * 20, 20 - 2, 20 - 2);

		}

	}
	static Random random = new Random();
	// food
	public static void newFood() {
		start: while (true) {
			foodX = random.nextInt(size);
			foodY = random.nextInt(size);

			for (Vector c : snake) {
				if (c.x == foodX && c.y == foodY) {
					continue start;
				}
			}
			//foodcolor = random.nextInt(5);
			speed++;
			break;

		}
	}

	public static void main(String[] args) {
		launch(args);
	}
}