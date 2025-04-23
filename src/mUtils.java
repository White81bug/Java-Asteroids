import com.raylib.Vector2;

public final class mUtils {
    // For an integer
    static final float TAU = (float) Math.PI * 2;

    static int GetSign(int i) {
        return i >> 31 | 1;
    }

    // This expands into something like 5 lines of assembly including the ret
    static int GetSign(float f) {
        return (int) f >> 31 | 1;
    }

    static int RollOver(int d, int min, int max) {
        final int t = d < min ? max : d;
        return t > max ? min : t;
    }

    // For a float
    static float RollOver(float d, float min, float max) {
        final float t = d < min ? max : d;
        return t > max ? min : t;
    }

    static float Clamp(float d, float min, float max) {
        final float t = d < min ? min : d;
        return t > max ? max : t;
    }

    static int Clamp(int d, int min, int max) {
        final int t = d < min ? min : d;
        return t > max ? max : t;
    }

    static boolean vecLess(Vector2 a, Vector2 b) {
        return a.x() < b.x() && a.y() < b.y();
    }

    static boolean vecMore(Vector2 a, Vector2 b) {
        return a.x() > b.x() && a.y() > b.y();
    }

    static boolean vecEq(Vector2 a, Vector2 b) {
        return a.x() == b.x() && a.y() == b.y();
    }

    static Vector2 vecAdd(Vector2 a, Vector2 b) {
        return new Vector2(a.getX() + b.getX(), a.getY() + b.getY());
    }

    static Vector2 vecSub(Vector2 a, Vector2 b) {
        return new Vector2(a.getX() - b.getX(), a.getY() - b.getY());
    }

    static Vector2 vecMul(Vector2 a, Vector2 b) {
        return new Vector2(a.getX() * b.getX(), a.getY() * b.getY());
    }

    static Vector2 vecMul(Vector2 a, float b) {
        return new Vector2(a.getX() * b, a.getY() * b);
    }

    static Vector2 vecDiv(Vector2 a, Vector2 b) {
        return new Vector2(a.getX() / b.getX(), a.getY() / b.getY());
    }

    static Vector2 vecDiv(Vector2 a, float b) {
        return new Vector2(a.getX() / b, a.getY() / b);
    }

    static int LogLevelToInt(LogLevel level) {
        switch (level) {
            case LogLevel.FATAL:
                return 0;
            case LogLevel.ERROR:
                return 1;
            case LogLevel.WARN:
                return 2;
            case LogLevel.BENCH:
                return 3;
            case LogLevel.INFO:
                return 4;
            case LogLevel.FIXME:
                return 5;
            case LogLevel.DEBUG:
                return 6;
            case LogLevel.TRACE:
                return 7;
        }
        return 7; // Because fuck you that's why
    }

    static LogLevel IntToLogLevel(int level) {
        switch (level) {
            case 0:
                return LogLevel.FATAL;
            case 1:
                return LogLevel.ERROR;
            case 2:
                return LogLevel.WARN;
            case 3:
                return LogLevel.BENCH;
            case 4:
                return LogLevel.INFO;
            case 5:
                return LogLevel.FIXME;
            case 6:
                return LogLevel.DEBUG;
            case 7:
                return LogLevel.TRACE;
        }
        return LogLevel.TRACE; // Because fuck you that's why
    }
}
