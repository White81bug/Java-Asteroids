import static com.raylib.Raylib.*;
import static com.raylib.Raylib.KeyboardKey.*;

import com.raylib.Camera2D;
import com.raylib.Vector2;

import java.lang.ArrayIndexOutOfBoundsException;

class Player extends Thing {

    float moveSpeed = 2.5f;

    Player(Shape shape) {
        super(shape);
       this.radius = 10;
    }

    //Не знаю, надо будет для физики или нет, но тут можно в  return поставить position
    void UpdatePlayerPosition() {
        Vector2 input = new Vector2(0, 0);

        if (isKeyDown(KEY_W)) input.setY(input.getY()-1);
        if (isKeyDown(KEY_S)) input.setY(input.getY()+1);
        if (isKeyDown(KEY_A)) input.setX(input.getX()-1);
        if (isKeyDown(KEY_D)) input.setX(input.getX()+1);
        float x = input.getX();
        float y = input.getY();
        float length = (float) Math.sqrt(x * x + y * y);

        if (length > 0) {

            x /= length;
            y /= length;


            heading = (float) Math.atan2(y, -x) + (float) Math.PI / 2;

            position = mUtils.vecAdd(position, new Vector2(x * moveSpeed, y * moveSpeed));
        }

        if (isKeyPressed(KEY_SPACE)) {
            LogicMaster.RequestBullet(position, heading);
        }
    }
}
