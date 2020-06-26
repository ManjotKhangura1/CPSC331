package hw2.student;

public interface BubbleSortInterface {
  
  /**
   * Generic bubble sort for a singly linked list
   * 
   * @param <T> A Comparable type
   * @param list The singly-linked list (SLL) to bubble sort
   */
  public <T extends Comparable<? super T>> void BubbleSort( SLL<T> list );
  

}
