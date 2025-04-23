
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

        // It doesn't work as expected without fps lock
        // and I don't care enough to go through all the places
        // that I need to add the frame time correction to
        setTargetFPS(60);
        LogicMaster joel = new LogicMaster();

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
                        joel = joel == null ? new LogicMaster() : joel;
                        joel.CreatePlayer(new Vector2(GLOBALS.MAX_WORLD_POS / 2,
                                GLOBALS.MAX_WORLD_POS / 2));
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

                    if (joel.playerRef.askToDie
                            || (GLOBALS.DEBUG && isKeyPressed(KEY_K)))
                        state = GameState.GAME_OVER;

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
