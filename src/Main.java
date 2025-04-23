
import static com.raylib.Raylib.*;
import static com.raylib.Raylib.KeyboardKey.*;
import static com.raylib.Raylib.MouseButton.*;

import com.raylib.Camera2D;
import com.raylib.Vector2;

import java.lang.ArrayIndexOutOfBoundsException;


public class Main {
    public static void main(String args[]) {
        GameState state = GameState.MENU;

        initWindow(1280, 800, "Fuck this shit");
        setTargetFPS(60);

        Camera2D camera = new Camera2D(
                //changed initial coords for camera. Without this change player in top-right corner
                new Vector2(getScreenWidth() / 2.0f, getScreenHeight() / 2.0f),
                new Vector2(0, 0), 0, 2f);
        LogicMaster joel = joel = new LogicMaster(difficulty);
        CameraController camCtrl = new CameraController();

        joel.CreatePlayer(new Vector2(200, 200));
        joel.CreateAsteroid(new Vector2(200, 100));
        while (!windowShouldClose()) {
            beginDrawing();
            clearBackground(BLACK);

            switch (state) {
                case MENU:
                    drawText("ASTEROIDS GAME", 500, 300, 40, RAYWHITE);
                    drawText("Press [ENTER] to Start", 500, 350, 20, GRAY);

                    drawText("A/D to change", 560, 560, 16, GRAY);
                    drawText(LogicMaster.GetDifficulty().toString(), 500, 400,
                            20, GRAY);

                    if (isKeyPressed(KEY_A)) {
                        LogicMaster.SetDifficulty(
                                LogicMaster.GetDifficulty().prev());
                    }

                    if (isKeyPressed(KEY_D)) {
                        LogicMaster.SetDifficulty(
                                LogicMaster.GetDifficulty().next());
                    }

                    if (isKeyPressed(KEY_ENTER)) {
                        joel = new LogicMaster(difficulty);
                        joel.resetScore(); //just to be sure
                        joel.CreatePlayer(new Vector2(200, 200));
                        joel.CreateAsteroid(new Vector2(200, 100));
                        joel.spawnCooldown = difficulty.getSpawnCooldown();
                        state = GameState.GAME;
                    }
                    break;

                case GAME:
                    beginMode2D(joel.camCtl.camera);
                    Renderer.drawGrid();

                    joel.RunLogic();

                    endMode2D();
                    drawFPS(20, 20);
                    drawText(String.format("Score: %d\n" + "Lives: %d",
                            LogicMaster.GetScore(), joel.playerRef.GetLives()),
                            20, 65, 18, RAYWHITE);
                    if (GLOBALS.DEBUG)
                        drawText(
                                String.format("Rotation: %f",
                                        joel.playerRef.shape.rotation),
                                20, 40, 18, RAYWHITE);
                        state = GameState.GAME_OVER;
                    }
                    break;

                case GAME_OVER:
                    drawText("GAME OVER", 550, 200, 40, RED);
                    drawText("Press [ENTER] to go to Menu", 500, 325, 20, GRAY);
                    drawText("SCORE: " + LogicMaster.GetScore(), 500, 300, 22,
                            RAYWHITE);
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
