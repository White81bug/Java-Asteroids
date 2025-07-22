package src;

import static com.raylib.Raylib.*;
import static com.raylib.Raylib.KeyboardKey.*;

public class Renderer {
    static final int GRID_STEP =
            (GLOBALS.MAX_WORLD_POS - GLOBALS.MIN_WORLD_POS) / 40;

    static final com.raylib.Color GRID_COLOR = new com.raylib.Color((byte) 100,
            (byte) 100, (byte) 100, (byte) 100);

    static final com.raylib.Color BORDER_COLOR = new com.raylib.Color(
            (byte) 0xa0, (byte) 0xa0, (byte) 0xa0, (byte) 0xff);

    static void drawGrid() {
        for (int i = GLOBALS.MIN_WORLD_POS
                + GRID_STEP; i < GLOBALS.MAX_WORLD_POS; i += GRID_STEP) {
            drawLine(i, GLOBALS.MIN_WORLD_POS, i, GLOBALS.MAX_WORLD_POS,
                    GRID_COLOR);
            drawLine(GLOBALS.MIN_WORLD_POS, i, GLOBALS.MAX_WORLD_POS, i,
                    GRID_COLOR);
        }
        drawLine(GLOBALS.MIN_WORLD_POS, GLOBALS.MIN_WORLD_POS,
                GLOBALS.MIN_WORLD_POS, GLOBALS.MAX_WORLD_POS, BORDER_COLOR);
        drawLine(GLOBALS.MIN_WORLD_POS, GLOBALS.MAX_WORLD_POS,
                GLOBALS.MAX_WORLD_POS, GLOBALS.MAX_WORLD_POS, BORDER_COLOR);
        drawLine(GLOBALS.MAX_WORLD_POS, GLOBALS.MAX_WORLD_POS,
                GLOBALS.MAX_WORLD_POS, GLOBALS.MIN_WORLD_POS, BORDER_COLOR);
        drawLine(GLOBALS.MAX_WORLD_POS, GLOBALS.MIN_WORLD_POS,
                GLOBALS.MIN_WORLD_POS, GLOBALS.MIN_WORLD_POS, BORDER_COLOR);
    }
}
