import static com.raylib.Raylib.*;
import static com.raylib.Raylib.KeyboardKey.*;

import com.raylib.Camera2D;
import com.raylib.Vector2;

import java.lang.ArrayIndexOutOfBoundsException;

class Bullet extends Thing {
    static final Shape bulletShape = new Shape(new Vector2[] {
            new Vector2(0, -0.5f), new Vector2(0.5f, 0.5f),
            new Vector2(-0.5f, 0.5f)
    });
    float speed = 8.0f;

    Bullet(Player player) {
        super(Bullet.bulletShape);
        this.position = player.position;
        this.shape.rotation = player.shape.rotation;
        this.speed = 8.0f;
    }

    void Update() {
        super.Update();
    }
}

class Player extends Thing {
    //    *
    //   / \
    //  //^\\
    // */   \*
    //
    static final Shape playerShape = new Shape(new Vector2[] {
            new Vector2(0, -2),
            new Vector2(-2, 2),
            new Vector2(0, 1),
            new Vector2(2, 2)
    });

    final float moveSpeed = 3.5f;
    final float rotSpeed = 2.5f;

    Player() {
        super(Player.playerShape);
        this.position = new Vector2(100, 100);
    }

    // Не знаю, надо будет для физики или нет, но тут можно в return поставить
    // position
    void UpdatePlayerPosition() {
        Vector2 input = new Vector2(0, 0);

        if (isKeyDown(KEY_W))
            input.setY(input.getY() - 1);
        if (isKeyDown(KEY_S))
            input.setY(input.getY() + 1);
        if (isKeyDown(KEY_A))
            input.setX(input.getX() - 1);
        if (isKeyDown(KEY_D))
            input.setX(input.getX() + 1);
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
