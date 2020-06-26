package hw2.student;
//************************  SLLNode.java  *******************************
//           node in a generic singly linked list class 
// 
// From Drozdek:
// http://www.mathcs.duq.edu/drozdek/DSinJava/

public class SLLNode<T> {
    public T info;
    public SLLNode<T> next;
    public SLLNode() {
        this(null,null);
    }
    public SLLNode(T el) {
        this(el,null);
    }
    public SLLNode(T el, SLLNode<T> ptr) {
        info = el; next = ptr;
    }
}


