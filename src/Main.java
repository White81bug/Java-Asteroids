import static com.raylib.Raylib.*;
import static com.raylib.Raylib.KeyboardKey.*;
import static com.raylib.Raylib.MouseButton.*;

import com.raylib.Vector2;

import java.lang.ArrayIndexOutOfBoundsException;

public class Main {
    public static void main(String args[]) {
        GLOBALS.DEBUG = System.getenv("Debug") != null ? true : false;
        GameState state = GameState.MENU;

        initWindow(1280, 800, "Fuck this shit");
        setTargetFPS(60);

        LogicMaster joel = new LogicMaster();
        CameraController camCtrl = new CameraController();

        joel.CreatePlayer(new Vector2(200, 200));
        joel.CreateAsteroid(new Vector2(200, 100));
        while (!windowShouldClose()) {
            beginDrawing();
            clearBackground(BLACK);

            switch (state) {
                case MENU:
                    drawText("ASTEROIDS GAME", 480, 180, 40, RAYWHITE);
                    drawText("Press [ENTER] to Start", 500, 240, 20, GRAY);
                    //controls explanation
                    drawText("W / S - Move Forward / Backward", 460, 320, 18, LIGHTGRAY);
                    drawText("A / D - Rotate Left / Right", 460, 350, 18, LIGHTGRAY);
                    drawText("SPACE - Shoot", 460, 380, 18, LIGHTGRAY);
                    drawText("P - Pause", 460, 410, 18, LIGHTGRAY);
                    drawText("C - Toggle Camera Follow", 460, 440, 18, LIGHTGRAY);
                    drawText("0 - Reset Player Position", 460, 470, 18, LIGHTGRAY);
                    drawText("ESC - Exit", 460, 500, 18, LIGHTGRAY);


                    if (isKeyPressed(KEY_ENTER)) {
                        joel = new LogicMaster(); // logic restart
                        joel.CreatePlayer(new Vector2(200, 200));
                        joel.CreateAsteroid(new Vector2(200, 100));
                        state = GameState.GAME;
                    }
                    break;

                case GAME:
                    if (isKeyPressed(KEY_P)) {
                        state = GameState.PAUSE;
                        break;
                    }

                    camCtrl.Update(joel.playerRef.position);

                    beginMode2D(camCtrl.getCamera());
                    Renderer.drawGrid();

                    joel.RunLogic();

                    endMode2D();

                    drawFPS(20, 20);
                    drawText(
                            String.format("Rotation: %f",
                                    joel.playerRef.shape.rotation),
                            20, 40, 18, RAYWHITE);

                    if (joel.playerRef.askToDie) {
                        state = GameState.GAME_OVER;
                    }
                    break;

                case PAUSE:
                    drawText("PAUSED", 560, 300, 40, YELLOW);
                    drawText("Press [P] to Resume", 500, 350, 20, GRAY);

                    if (isKeyPressed(KEY_P)) {
                        state = GameState.GAME;
                    }
                    break;

                case GAME_OVER:
                    drawText("GAME OVER", 550, 300, 40, RED);
                    drawText("Press [ENTER] to go to Menu", 500, 350, 20, GRAY);
                    if (isKeyPressed(KEY_ENTER)) {
                        state = GameState.MENU;
                    }
                    break;
            }

            endDrawing();

        }
        closeWindow();
    }
}
