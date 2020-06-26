package hw3.student;

/**
 * A class that represents a node in a binary expression tree.
 */ 

public class ExpressionTreeNode {

  // Item stored in the node as a String
  public String el;

  // References to left and right children
  public ExpressionTreeNode left, right;

  /**
   * Creates a node with a given String. Both children set to null.
   * @param e The string to store in this node. 
   */
  public ExpressionTreeNode( String e ) { 
    this(e, null, null);
  }

  /**
   * Creates a node with a given String and left and right children.
   * @param e The string to store in this node. 
   * @param l The left child of this node.
   * @param r The right child of this node.
   */
  public ExpressionTreeNode( String e, ExpressionTreeNode l, ExpressionTreeNode r ) {
    el = new String( e ); 
    left = l;
    right = r;
  } 
}
