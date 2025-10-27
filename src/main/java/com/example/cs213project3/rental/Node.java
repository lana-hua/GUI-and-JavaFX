package com.example.cs213project3.rental;

/**
 * Node class represents a node in a linked list.
 * Each node contains a trip element and a reference to the next node in the sequence.
 * @author Lana Huang
 */
public class Node {
    Trip trip;
    Node next;

    /**
     * Returns the next Node in the linked list
     * @return the next Node
     */
    public Node getNext() {
        return next;
    }

    /**
     * Returns the trip data that the Node has
     * @return the trip data
     */
    public Trip getTrip() {
        return trip;
    }

    /**
     * Constructs a new node with trip.
     * The next node reference is initialized to null.
     * @param trip The trip to be stored in this node.
     */
    Node(Trip trip){
        this.trip = trip;
        this.next = null;
    }
}
