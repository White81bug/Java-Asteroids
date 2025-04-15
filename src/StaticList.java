public class StaticList<T> {
    static final int ARRAY_SIZE = 1024;
    private Object[] list;
    private int size;

    StaticList() {
        list = new Object[StaticList.ARRAY_SIZE];
        size = 0;
    }

    T Push(T obj) {
        list[size] = obj;
        size++;
        return obj;
    }

    int GetLen() {
        return size;
    }

    T Get(int i) throws ArrayIndexOutOfBoundsException {
        if (i < 0) {
            System.err.printf("Got i == %d when real_len == %d\n", i, size);
            throw new ArrayIndexOutOfBoundsException();
        }

        @SuppressWarnings("unchecked")
        T ret = (T) list[i];
        return ret;
    }

    T Pop(int i) throws ArrayIndexOutOfBoundsException {
        if (i < 0 || size <= i) {
            throw new ArrayIndexOutOfBoundsException();
        }
        @SuppressWarnings("unchecked")
        T tmp = (T) list[i];
        list[i] = null;
        return tmp;
    }

    // Hopefully swap two objects in a list, though I'm not sure if it'll work...
    void Swap(int index_a, int index_b) {
        T temp = -1 < index_a && index_a < StaticList.ARRAY_SIZE ? (T)list[index_a] : null;
        if (temp != null)
            list[index_a] = -1 < index_b && index_b < StaticList.ARRAY_SIZE ? (T)list[index_a] : null;

        if (-1 < index_b && index_b < StaticList.ARRAY_SIZE)
            list[index_b] = temp;
    }

    void Sort() {
        int i        = 1;
        int gotToPos = 0;
        while (i < this.size) {

            Thing prev    = (Thing)this.list[i - 1];
            Thing current = (Thing)this.list[i];

            if (current.position.getX() >= prev.position.getX()) {
                if (i > gotToPos) {
                    gotToPos = i;
                } else
                    i = gotToPos;
                i++;
                continue;
            }
            this.list[i]     = prev;
            this.list[i - 1] = current;
            if (i > 0) i--;
        }
    }
}
