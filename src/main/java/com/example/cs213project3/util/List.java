package com.example.cs213project3.util;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * This is the List util class where the collection classes, Fleet, Reservation and TripList extend from.
 * It allows the user to search, resize, add, remove, and match objects in the list.
 * @param <E> The objects that the List holds.
 * @author Lana Huang
 */
public class List<E> implements Iterable<E> {
    private E[] objects; //E is the name for the generic type
    private int size;

    /**
     * Constructs a List with objects E
     * It instantiates the List with a starting capacity of 4 and a size of 0
     */
    public List() {
        Object[] temp = new Object[4];
        objects = (E[]) temp;
        size = 0;
    }

    /**
     * Finds the index of the given object in the fleet.
     * If it does not exist in the List it returns -1, NOT_FOUND.
     * @param e The object to be found in the fleet.
     * @return index if found; -1, NOT_FOUND otherwise.
     */
    private int find(E e) {
        for (int i = 0; i < size; i++) {
            if (objects[i].equals(e)) {
                return i;
            }
        }
        return -1;
    } //return -1 if not found

    /**
     * Grows the array of the List by 4 if the List reaches capacity
     */
    private void grow() {
        Object[] temp = new Object[size + 4]; // create a new larger array
        E[] newArray = (E[]) temp;

        for (int i = 0; i < size; i++) {
            newArray[i] = objects[i];
        }

        objects = newArray;
    } //grow the size of the array by 4

    /**
     * Checks if the List contains an object given the object.
     * @param e The object being checked against the List.
     * @return true if the object is found; false otherwise.
     */
    public boolean contains(E e) {
        if (find(e) != -1) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Add given object to the List.
     * If the List is already at capacity, call grow to increase capacity.
     * @param e object to be added to the List.
     */
    public void add(E e) {
        if (!contains(e)) {
            if (size == objects.length) {
                grow();
            }
            objects[size] = e;
            size++;
        }
    }

    /**
     * Remove the given object from the List
     * It does nothing if object is not in List. It overwrites with the last object in the List
     * @param e the object to be removed from the List
     */
    public void remove(E e) {
        int index = find(e);
        if (index != -1) {
            objects[index] = objects[size - 1];
            objects[size - 1] = null;
            size--;
        }
    }

    /**
     * Checks if the List is empty by reading the size of the list.
     * @return true if List is empty; false otherwise.
     */
    public boolean isEmpty() {
        if (size == 0) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Returns the size of the object list.
     * @return the number of objects in the list.
     */
    public int size() {
        return size;
    }

    /**
     * Create a new ListIterator to help with traversing a List
     * @return ListIterator
     */
    public Iterator<E> iterator() {
        return new ListIterator<>();
    }

    /**
     * Finds and returns the object at the given index from the list.
     * @param index the index at which we are finding the object.
     * @return object that is at the given index.
     */
    public E get(int index) {
        if (index < 0 || index >= size)
            throw new IndexOutOfBoundsException();
        return objects[index];
    }

    /**
     * Sets an object e at the given index in the list.
     * @param index The index in the list where we are setting the object at.
     * @param e The object that should be put at given index.
     */
    public void set(int index, E e) {
        if (index < 0 || index >= size)
            throw new IndexOutOfBoundsException();
        objects[index] = e;
    }

    /**
     * Finds the index of object E in the list.
     * It uses the private method find(e) that returns the index of an object.
     * @param e The object that should be found in the list
     * @return The index that the object is at in the list or return -1 if it's not found.
     */
    public int indexOf(E e) {
        return find(e);
    }

    /**
     * Private inner class for the iterator to work.
     * @param <E> The object list to iterate.
     */
    private class ListIterator<E> implements Iterator<E> {
        int current = 0; //current index when traversing the list (array)

        /**
         * Checks if the List is empty or if it's at the end of the list
         * @return true if it's not empty and not at the end of the list; return false otherwise
         */
        public boolean hasNext(){
            if (isEmpty()) {
                return false;
            } else {
                return current < size;
            }
        }

        /**
         * Returns the next object in the list if there is a next in the list
         * @return object or throw Exception otherwise
         */
        public E next() {
            if (hasNext()) {
                return (E) objects[current++];
            } else {
                throw new NoSuchElementException();
            }
        }
    }
}
