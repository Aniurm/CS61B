package deque;


import java.util.Iterator;

public class LinkedListDeque<T> implements Iterable<T>, Deque<T> {
    private IntNode sentinel;

    private int size;

    private class IntNode {
        private T item;
        private IntNode next;
        private IntNode last;

        // IntNode's constructor
        IntNode(T i, IntNode n, IntNode l) {
            item = i;
            next = n;
            last = l;
        }
    }

    // create empty LinkedListDeque
    public LinkedListDeque() {
        sentinel = new IntNode(null, null, null);
        //make circle
        sentinel.next = sentinel;
        sentinel.last = sentinel;

        size = 0;
    }

    @Override
    public void addFirst(T item) {
        //new node
        IntNode tmp = new IntNode(item, sentinel.next, sentinel);
        //connect
        sentinel.next = tmp;
        tmp.next.last = tmp;

        size += 1;
    }

    @Override
    public void addLast(T item) {
        //new node
        IntNode tmp = new IntNode(item, sentinel, sentinel.last);
        //connect
        sentinel.last = tmp;
        tmp.last.next = tmp;

        size += 1;
    }

    @Override
    public int size() {
        return size;
    }

    // print the items from first to last, separated by a space
    // once all the items have been printed, print out a new line
    @Override
    public void printDeque() {
        int count;
        IntNode cursor = sentinel.next;
        for (count = 0; count < size; count++) {
            System.out.print(cursor.item + " ");
            cursor = cursor.next;
        }
        System.out.println(" ");
    }

    @Override
    public T removeFirst() {
        if (size == 0) {
            return null;
        }

        IntNode tmp = sentinel.next;
        // change connection
        sentinel.next = tmp.next;
        tmp.next.last = sentinel;
        size--;

        return tmp.item;
    }

    @Override
    public T removeLast() {
        if (size == 0) {
            return null;
        }

        IntNode tmp = sentinel.last;
        // change connection
        tmp.last.next = sentinel;
        sentinel.last = tmp.last;
        size--;

        return tmp.item;
    }

    @Override
    public T get(int index) {
        if (size == 0 || index >= size) {
            return null;
        }
        IntNode cursor = sentinel.next;
        for (int i = 0; i < index; i++) {
            cursor = cursor.next;
        }

        return cursor.item;
    }

    public T getRecursive(int index) {
        if (size == 0 || index < 0 || index >= size) {
            return null;
        }

        return getRecursivehelp(sentinel.next, index);
    }

    // helper function for getRecursive
    // to use recursion, we need to pass an Intnode parameter
    // but we can't let users do it
    // so I make a helper function to pass IntNode
    private T getRecursivehelp(IntNode cursor, int index) {
        if (index == 0) {       // base case
            return cursor.item;
        } else {      // recursive case
            return getRecursivehelp(cursor.next, index - 1);
        }
    }

    public Iterator<T> iterator() {
        return new LinkedListDequeIterator();
    }

    private class LinkedListDequeIterator implements Iterator<T> {
        private IntNode cursor;

        LinkedListDequeIterator() {
            cursor = sentinel.next;
        }

        public boolean hasNext() {
            return cursor.item != null;
        }

        public T next() {
            T returnItem = cursor.item;
            cursor = cursor.next;
            return returnItem;
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null) {
            return false;
        }
        if (!(o instanceof Deque)) {
            return false;
        }
        Deque<T> obj = (Deque<T>) o;
        if (obj.size() != this.size()) {
            return false;
        }
        // traverse and compare two LinkedListDeque
        for (int i = 0; i < this.size(); i++) {
            if (obj.get(i) != this.get(i)) {
                return false;
            }
        }
        return true;
    }
}



























