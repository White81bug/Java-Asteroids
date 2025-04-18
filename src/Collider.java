final class Collider {
    static boolean CheckCollide(Thing a, Thing b) {

        float length = (float) Math
                .sqrt(Math.pow(b.position.getX() - a.position.getX(), 2)
                        + Math.pow(b.position.getY() - a.position.getY(), 2));

        if (a.shape.colliderRadius + b.shape.colliderRadius < length)
            return false;
        return true;
    }

    static int RunCollider(StaticList<Thing> list, int index) {
        int collisionCount = 0;
        Thing first = list.Get(index);
        Thing second;
        for (int i = index + 1; i < list.GetLen(); i++) {
            second = list.Get(index);

            // This is an optimization, so we don't have to check the rest of the list
            // if the current object is out of reach.
            // It assumes that the list is sorted by X
            if (second.shape.colliderRadius
                    + first.shape.colliderRadius < second.position.getX()
                            + first.position.getX())
                break;

            if (!CheckCollide(first, second))
                continue;
            collisionCount++;
        }
        return collisionCount;
    }
}
