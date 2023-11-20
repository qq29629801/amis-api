package com.yuyaogc.lowcode.engine.cache;

public class LinkList<T> {
    public LinkList(){
        this.size = 0;
    }


    public LinkList(int size){
        this.size = size;
    }

    public void put(T value){
        Node<T> newNode = new Node<T>(value);
        first = newNode;
        size++;
    }

    public void put(T value, int index){

    }


    Node first;
    int size;

    class Node<T> {
        public T value;
        public Node<T> next;
        public Node<T> prev;
        public Node(T value){
            this.value = value;
        }
    }
}
