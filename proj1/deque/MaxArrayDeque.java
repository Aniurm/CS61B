package deque;

import java.util.Comparator;

public class MaxArrayDeque<T> extends ArrayDeque<T>{
    Comparator<T> comparator;

    public MaxArrayDeque(Comparator<T> c) {
        comparator = c;
    }
    public T max() {
        int index = 0;
        for (int i = 1; i < this.size(); i++) {
            if (comparator.compare(array[i], array[index]) > 0) {
                index = i;
            }
        }
        return array[index];
    }

    public T max(Comparator<T> c) {
        int index = 0;
        for (int i = 1; i < this.size(); i++) {
            if (c.compare(array[i], array[index]) > 0) {
                index = i;
            }
        }
        return array[index];
    }


}
