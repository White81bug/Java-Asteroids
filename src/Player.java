import com.raylib.Vector2;

import static com.raylib.Raylib.RAYWHITE;
import static com.raylib.Raylib.drawTriangle;

//делаем игрока
class Player{
    Vector2 position;
    Vector2 speed;
    float heading;

    Player()
    {
        position = new Vector2(400, 225);
        speed = new Vector2(0, 0);
        heading = 0;
    }
    void Update()
    {
        position.setX(position.getX() + speed.getX());
        position.setY(position.getY() + speed.getY());
        if(speed.getX() != 0 || speed.getY() != 0) heading = (float) Math.atan2(speed.getY(), speed.getX());

    }
    void Draw(){

        float size = 20;
        Vector2 nose = new Vector2(size, 0);
        Vector2 left = new Vector2(-size * 0.5f, size * 0.5f);
        Vector2 right = new Vector2(-size * 0.5f, -size * 0.5f);

        Vector2 p1 = rotateAndTranslate(nose, heading, position);
        Vector2 p2 = rotateAndTranslate(left, heading, position);
        Vector2 p3 = rotateAndTranslate(right, heading, position);

        drawTriangle(p2, p1, p3, RAYWHITE);
    }

    Vector2 rotateAndTranslate(Vector2 point, float angle, Vector2 origin) {
        float cos = (float) Math.cos(angle);
        float sin = (float) Math.sin(angle);

        float x = point.getX() * cos - point.getY() * sin;
        float y = point.getX() * sin + point.getY() * cos;

        return new Vector2(origin.getX() + x, origin.getY() + y);
    }
}