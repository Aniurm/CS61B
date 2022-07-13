package deque;

import java.util.Iterator;

public class ArrayDeque<T> implements Iterable<T>, Deque<T>   {
    private int first;
    private int last;
    private int size;
    private int capacity;
    private T[] array;

    // create an empty array deque
    public ArrayDeque() {
        size = 0;
        capacity = 8;
        last = capacity / 2;
        first = last + 1;
        array = (T []) new Object[8];
    }

    private void resize(int capacity) {
        T[] newArray = (T []) new Object[capacity];
        // copy data
        int start = capacity / 3;
        for (int i = start, j = first; i < start + this.size; i++, j++) {
            if (j == this.capacity) {
                j = 0;
            }
            else if (i == capacity) {
                i = 0;
            }

            newArray[i] = this.array[j];
        }

        first = start;
        last = first + size - 1;
        this.array = newArray;
        this.capacity = capacity;
    }

    @Override
    public void addFirst(T item) {
        // full
        if (size == capacity) {
            resize(capacity * 2);
        }

        if (first == 0) {
            first = capacity - 1;
        } else {
            first -= 1;
        }
        array[first] = item;

        size++;
    }

    @Override
    public void addLast(T item) {
        //full
        if (size == capacity) {
            resize(capacity * 2);
        }

        if (last == capacity - 1) {
            last = 0;
        } else {
            last += 1;
        }

        array[last] = item;

        size++;
    }

    @Override
    public int size() {
        return size;
    }

    public void printDeque() {
        for (int i = 0, j = first; i < size; i++, j++) {
            if (j == capacity - 1) {
                j = 0;
            }

            System.out.print(array[j] + " ");
        }
        System.out.println(" ");
    }

    @Override
    public T removeFirst() {
        if (size == 0) {
            return null;
        }
        needCut();

        if (first <= last) {
            size--;
            return array[first++];
        } else {
            T result = array[first];

            if (first == capacity - 1) {
                first = 0;
            } else {
                first++;
            }
            size--;

            return result;
        }
    }

    // determine whether cut capacity according to usage factor
    private void needCut() {
        double minRate = 0.25;
        if (1.0 * (size - 1) / capacity < minRate && capacity >= 16) {
            resize(capacity / 2);
        }
    }

    @Override
    public T removeLast() {
        if (size == 0) {
            return null;
        }
        needCut();

        if (first <= last) {
            size--;
            return array[last--];
        } else {
            T result = array[last];
            if (last == 0) {
                last = capacity - 1;
            } else {
                last--;
            }
            size--;

            return result;
        }
    }

    @Override
    public T get(int index) {
        if (size == 0 || index < 0 || index >= size) {
            return null;
        }

        T result;
        if (first + index < capacity) {
            result = array[first + index];
        } else {
            result = array[index - (capacity - first)];
        }

        return result;
    }

    public Iterator<T> iterator() {
        return new ArrayDequeIterator();
    }

    private class ArrayDequeIterator implements Iterator<T> {
        int count;
        int cursor;

        public ArrayDequeIterator() {
            count = 0;
            cursor = first;
        }

        public boolean hasNext() {
            return count < size;
        }

        public T next() {
            T returnItem = array[cursor];
            // move cursor to next place
            if (cursor + 1 == capacity) {
                cursor = 0;
            } else {
                cursor++;
            }

            count++;

            return returnItem;
        }
    }

    @Override
    public boolean equals(Object o) {
        if (o == null) {
            return false;
        }
        Deque<T> obj = (Deque<T>) o;
        if (obj.size() != this.size()) {
            return false;
        }

        for (int i = 0; i < this.size(); i++) {
            if (obj.get(i) != this.get(i)) {
                return false;
            }
        }

        return true;
    }

    public T[] getArray() {
        return array;
    }
}
