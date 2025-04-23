import static com.raylib.Raylib.*;
import com.raylib.Vector2;

final class Collider {
    static boolean CheckCollide(Thing a, Thing b) {

        float length = (float) Math
                .sqrt(Math.pow(b.position.getX() - a.position.getX(), 2)
                        + Math.pow(b.position.getY() - a.position.getY(), 2));

        if (a.shape.colliderRadius + b.shape.colliderRadius < length)
            return false;
        return true;
    }

    static int RunCollider(StaticList<Thing> list, int startFrom) {
        int collisionCount = 0;
        Thing first = list.Get(startFrom);
        Thing second;
        for (int i = startFrom + 1; i < list.GetLen(); i++) {
            second = list.Get(i);

            // This is an optimization, so we don't have to check the rest of the list
            // if the current object is out of reach.
            // It assumes that the list is sorted by X
            if (second.shape.colliderRadius
                    + first.shape.colliderRadius < second.position.getX()
                            - first.position.getX())
                break;

            if (!CheckCollide(first, second))
                continue;

            if (first.priority >= second.priority)
                first.OnCollision(second);
            else
                second.OnCollision(first);

            if (GLOBALS.DEBUG) {
                drawRectangleV(first.position, new Vector2(10, 10), RED);
                drawRectangleV(second.position, new Vector2(10, 10), RED);
            }

            collisionCount++;
        }
        return collisionCount;
    }

    static void Bounce(Thing first, Thing second) {

        Vector2 pos_delta = mUtils.vecSub(second.position, first.position);
        Vector2 speed_delta = mUtils.vecSub(second.speed, first.speed);

        if (mUtils.GetSign(pos_delta.getX()) == mUtils
                .GetSign(speed_delta.getX())
                && mUtils.GetSign(pos_delta.getY()) == mUtils
                        .GetSign(speed_delta.getY()))
            return;

        {

            // I wasted 24 fucking hours trying to make this work
            float Sa = first.speed.getX();
            float Sb = second.speed.getX();

            float ma = mUtils.Clamp(first.mass, 1, 1024);
            float mb = mUtils.Clamp(second.mass, 1, 1024);

            first.speed.setX(Sa + (Sb - Sa) / ma);
            second.speed.setX(Sb + (Sa - Sb) / mb);
        }

        {
            float Sa = first.speed.getY();
            float Sb = second.speed.getY();

            float ma = mUtils.Clamp(first.mass, 1, 1024);
            float mb = mUtils.Clamp(second.mass, 1, 1024);

            first.speed.setY(Sa + (Sb - Sa) / ma);
            second.speed.setY(Sb + (Sa - Sb) / mb);
        }
    }
}
