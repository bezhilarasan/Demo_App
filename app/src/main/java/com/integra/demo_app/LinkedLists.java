package com.integra.demo_app;

public class LinkedLists {
    static Node head;

    public static void main() {
        LinkedLists linkedLists = new LinkedLists();
        linkedLists.head = new Node(1);
        linkedLists.head.next = new Node(2);
        linkedLists.head.next.next = new Node(3);
        linkedLists.head.next.next.next = new Node(4);
        linkedLists.printlist(head);
        linkedLists.deletenode(head);
        linkedLists.printlist(head);
    }

    void deletenode(Node node) {
        Node temp = node.next;
        node.data = temp.data;
        node.next = temp.next;
        System.gc();

    }

    void printlist(Node node) {
        while (node != null) {
            System.out.print("" + node.data);
            node = node.next;
        }
    }

    static class Node {

        int data;
        Node next;

        Node(int datal) {
            data = datal;
            next = null;
        }
    }


}
