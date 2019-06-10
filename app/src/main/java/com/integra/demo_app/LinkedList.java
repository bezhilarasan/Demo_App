package com.integra.demo_app;

// Java program to implement
// a Singly Linked List
public class LinkedList {

    Node head; // head of list

    // Method to insert a new node
    public static LinkedList insert(LinkedList list, int data) {
        // Create a new node with given data
        Node new_node = new Node(data);
        new_node.next = null;

        // If the Linked List is empty,
        // then make the new node as head
        if (list.head == null) {
            list.head = new_node;
        } else {
            // Else traverse till the last node
            // and insert the new_node there
            Node last = list.head;
            while (last.next != null) {
                last = last.next;
            }

            // Insert the new_node at last node
            last.next = new_node;
        }

        // Return the list by head
        return list;
    }

    // Method to print the LinkedList.
    public static void printList(LinkedList list) {
        Node currNode = list.head;

        System.out.println("LinkedList: ");

        // Traverse through the LinkedList
        while (currNode != null) {
            // Print the data at current node
            System.out.println("LinkedList " + currNode.data + " ");

            // Go to next node
            currNode = currNode.next;
        }
    }

    // method to create a Singly linked list with n nodes
    public static void main() {
        /* Start with the empty list. */
        LinkedList list = new LinkedList();

        //
        // ******INSERTION******
        //

        // Insert the values
        list = insert(list, 1);
        list = insert(list, 2);
        list = insert(list, 3);
        list = insert(list, 4);
        list = insert(list, 5);
        list = insert(list, 6);
        list = insert(list, 7);
        list = insert(list, 8);

        // Print the LinkedList
        printList(list);

        reverse(list);
        printList(list);
    }

    // **************MAIN METHOD**************

    private static void reverse(LinkedList list) {
        Node pointer = list.head;
        Node previous = null;
        Node current = null;
        while (pointer != null) {
            current = pointer;
            pointer = pointer.next;

            // reverse
            current.next = previous;
            previous = current;
            list.head = current;
        }
    }

    // Linked list Node.
    // This inner class is made static
    // so that main() can access it
    static class Node {

        int data;
        Node next;

        // Constructor
        Node(int d) {
            data = d;
            next = null;
        }
    }
} 
