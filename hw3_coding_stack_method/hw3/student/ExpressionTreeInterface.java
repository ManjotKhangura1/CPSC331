package hw3.student;

import java.text.ParseException;

public interface ExpressionTreeInterface {

  /**
   * Build a parse tree from an expression.
   * @param line String containing the expression
   *
   * @throws ParseException If an error occurs during parsing. 
   */

  public void parse( String line ) throws ParseException;

  /**
   * Evaluate the expression tree and return the integer result. An 
   * empty tree returns 0.
   */
  public int evaluate();
  
  /**
   * Simplifies the tree to a specified height h (h >= 0). After simplification, the tree 
   * has a height of h. Any subtree rooted at a height of h is replaced by a leaf node 
   * containing the evaluated result of that subtree.  
   * If the height of the tree is already less than the specified height, the tree is unaffected. 
   * 
   * @param h The height to simplify the tree to.
   */
  public void simplify(int h);

  /**
   * Returns a parentheses-free postfix representation of the 
   * expression. Tokens are separated by whitespace. An empty tree 
   * returns a zero length string.
   */
  public String postfix(); 

  /**
   * Returns a parentheses-free prefix representation of the expression. Tokens are 
   * separated by whitespace. An empty tree returns a zero length string.
   */
  public String prefix();
}
