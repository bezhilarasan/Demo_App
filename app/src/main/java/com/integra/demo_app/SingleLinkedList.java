package com.integra.demo_app;

public class SingleLinkedList {
    private Node head;

    public SingleLinkedList(Node head) {
        this.head = head;
    }

    public void add(Node node) {
        Node current = head;
        while (current != null) {

        }
    }

    static class Node {
        private int data;
        private Node nextnode;

        public Node(int node) {
            this.data = node;
        }

        public int getData() {
            return data;
        }

        public void setData(int data) {
            this.data = data;
        }

        public Node getNextnode() {
            return nextnode;
        }
    }
}
