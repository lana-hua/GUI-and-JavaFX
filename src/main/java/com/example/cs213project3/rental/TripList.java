package com.example.cs213project3.rental;

import com.example.cs213project3.util.List;

/**
 * TripList class represents the circular linked list.
 * This class contains the reference to the last node in the linked list.
 * @author Lana Huang, Sharon Chen
 */
public class TripList extends List<Node> {
    private Node last;

    /**
     * Returns the last Node in the TripList Linked List
     * @return the last Node
     */
    public Node getLast() {
        return last;
    }

    /**
     * Finds the length of Triplist and returns the int length.
     * @return integer length.
     */
    public static int getLength() {
        if (Frontend.tripList.getLast() == null) {
            return 0;
        }

        if (Frontend.tripList.getLast() == Frontend.tripList.getLast().getNext()) {
            return 1;
        }

        int length = 1;
        Node ptr = Frontend.tripList.getLast().getNext();

        while (ptr != Frontend.tripList.getLast()) {
            length++;
            ptr = ptr.getNext();
        }

        return length;
    }

    /**
     * Add given New Node to circular linked list.
     * @param newNode The new node to be added to linked list.
     */
    @Override
    public void add(Node newNode) {
        if (last == null) {
            newNode.next = newNode;
            last = newNode;
        } else {
            newNode.next = last.next;
            last.next = newNode;
            last = newNode;
        }
    }

    /**
     * Checks if the list is empty.
     */
    @Override
    public boolean isEmpty() {
        return last == null;
    }
}
