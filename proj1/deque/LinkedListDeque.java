package deque;


public class LinkedListDeque<T> {
    private IntNode sentinel;
    private int size;

    public class IntNode {
        public T item;
        public IntNode next;
        public IntNode last;

        // IntNode's constructor
        public IntNode(T i, IntNode n, IntNode l) {
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

    public LinkedListDeque(T x) {
        sentinel = new IntNode(null, null, null);
        // new node to store x
        IntNode tmp = new IntNode(x, sentinel, sentinel);
        //connect
        sentinel.next = tmp;
        sentinel.last = tmp;

        size = 1;
    }

    public void addFirst(T item) {
        //new node
        IntNode tmp = new IntNode(item, sentinel.next, sentinel);
        //connect
        sentinel.next = tmp;
        tmp.next.last = tmp;

        size += 1;
    }

    public void addLast(T item) {
        //new node
        IntNode tmp = new IntNode(item, sentinel, sentinel.last);
        //connect
        sentinel.last = tmp;
        tmp.last.next = tmp;

        size += 1;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public int size() {
        return size;
    }

    // print the items from first to last, separated by a space
    // once all the items have been printed, print out a new line
    public void printDeque() {
        int count;
        IntNode cursor = sentinel.next;
        for (count = 0; count < size; count++) {
            System.out.print(cursor.item + " ");
            cursor = cursor.next;
        }
        System.out.println(" ");
    }

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
        }
        else {      // recursive case
            return getRecursivehelp(cursor.next, index - 1);
        }
    }
}



























