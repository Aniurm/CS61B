package deque;

import java.util.Comparator;

public class MaxArrayDeque<T> extends ArrayDeque<T>{
    private Comparator<T> comparator;

    public MaxArrayDeque(Comparator<T> c) {
        comparator = c;
    }
    public T max() {
        T[] cmpArray = getArray();
        int index = 0;
        for (int i = 1; i < this.size(); i++) {
            if (comparator.compare(cmpArray[i], cmpArray[index]) > 0) {
                index = i;
            }
        }
        return cmpArray[index];
    }

    public T max(Comparator<T> c) {
        T[] cmpArray = getArray();
        int index = 0;
        for (int i = 1; i < this.size(); i++) {
            if (c.compare(cmpArray[i], cmpArray[index]) > 0) {
                index = i;
            }
        }
        return cmpArray[index];
    }
}
