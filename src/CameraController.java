package src;

import com.raylib.Vector2;
import com.raylib.Camera2D;
import static com.raylib.Raylib.*;
import static com.raylib.Raylib.KeyboardKey.*;
import static com.raylib.Raylib.MouseButton.*;

public class CameraController {
    private Vector2 target = new Vector2(0, 0);
    private float zoom = 2.0f;
    private boolean followPlayer = true;

    Camera2D camera = new Camera2D();

    void Update(Vector2 targetOriginal) {

        if (isKeyPressed(KEY_C)) {
            followPlayer = !followPlayer;
        }

        float wheel = getMouseWheelMove();
        if (wheel != 0.0f) {
            zoom += wheel * 0.1f;
            zoom = Math.max(0.1f, Math.min(zoom, 5.0f));
        }

        if (isMouseButtonDown(MOUSE_BUTTON_RIGHT)) {
            if (followPlayer)
                followPlayer = false;
            Vector2 delta = getMouseDelta();
            delta = mUtils.vecMul(delta, -1.0f / zoom);
            target = mUtils.vecAdd(target, delta);
        }

        if (followPlayer) {
            target = new Vector2(targetOriginal.getX(), targetOriginal.getY());
        }

        camera.setOffset(
                new Vector2(getScreenWidth() / 2.0f, getScreenHeight() / 2.0f));
        camera.setTarget(target);
        camera.setRotation(0);
        camera.setZoom(zoom);
    }

}
