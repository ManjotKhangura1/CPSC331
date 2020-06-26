package hw2.student;

public class SLLBubbleSort implements BubbleSortInterface {

  /**
   * A method that will sort a singly linked list that holds Comparable
   * elements. Bubble sort is used to sort the elements. In this version of
   * bubble sort, the largest element will bubble to the right in each pass.
   * Note that the linked list is sorted in place, i.e. the specified list is
   * modified; a copy is not made.
   *
   * @param list The SLL list to sort
   */

  public <T extends Comparable<? super T>> void BubbleSort(SLL<T> list) {

    // TO DO

    if (list.getLength() > 1) {
      boolean change; //Checks if 2 adjacent nodes need to be swapped

      //do while loop so that it runs at least once to set up the three nodes
      do {
        SLLNode<T> prevNode = null;
        SLLNode<T> currNode = list.head;
        SLLNode<T> nextNode = list.head.next;
        change = false;

        while (nextNode != null) {

          //Checks if the left node is bigger than the right node
          if (currNode.info.compareTo(nextNode.info) >= 1) {
            SLLNode<T> temp = nextNode.next;
            change = true;
            if (prevNode != null) {
              prevNode.next = nextNode;
            } else {
              list.head = nextNode;
            }
            //Swaps the nodes
            nextNode.next = currNode;
            currNode.next = temp;
            prevNode = nextNode;
            nextNode = currNode.next;

            //If the left node is smaller than the right node, then it moves on
          } else {
            prevNode = currNode;
            currNode = nextNode;
            nextNode = nextNode.next;
          }
        }
        /*
        Keeps loop going if changes still are being made (change = true) but once
        no change is made from a full run through the linked list, then it leaves
        the loop
        */
      } while (change);
    }
  }
}
