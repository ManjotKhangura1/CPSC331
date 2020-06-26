package hw3.student;

import java.text.ParseException;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Stack;
import java.util.StringTokenizer;

public class ExpressionTree<expr> implements ExpressionTreeInterface {
	private ExpressionTreeNode root;

	/**
	 * Constructor to build an empty parse tree.
	 */

	public ExpressionTree() { root = null; }

	/**
	 * Build a parse tree from an expression.
	 *
	 * @param line String containing the expression
	 * @throws ParseException If an error occurs during parsing.
	 */

	public void parse(String line) throws ParseException {

		//Tests that the input is actually some kind of an expression
		if (line.equals("")) {
			throw new ParseException("Empty expression, can not parse", 0);
		}

		//parse the expression and set the root
		root = parsePostfix(line);
	}

	/**
	 * Shunting yard algorithm code to convert the infix to postfix
	 *
	 * @param line - String containing the expression
	 * @return Q - The queue which has the postfix expression stored
	 * @throws ParseException - For any errors during parsing
	 */

	private Queue<String> infixToPostfix(String line) throws ParseException {

		//Creates tokens for each string separated by a space
		StringTokenizer E = new StringTokenizer(line);

		//Initialize stack
		Stack<String> S = new Stack<>();

		//Initialize queue
		Queue<String> Q = new LinkedList<String>();

		//Runs while the expression has not fully been iterated through yet
		while (E.hasMoreTokens()) {
			String t = E.nextToken();

			//Checks if the next token is an integer
			if (isInt(t)) {
				Q.add(t);
			}

			/*
			Checks if the next token is an operator and runs a while loop if the stack is not empty and if the top of
			the stack has >= precedence over the token by comparing the integer returned from the precedence() method
			*/
			if(isOper(t)) {
				while (!S.empty() && (isOper(S.peek())) &&
						(precedence(S.peek()) >= precedence(t))) {
					String result = S.pop();
					Q.add(result);
				}
				S.push(t);
			}

			//Checks for brackets
			if (t.equals("(")) {
				S.push(t);
			}
			if (t.equals(")")) {
				while (!S.peek().equals("(")) {
					String result = S.pop();
					Q.add(result);
				}
				S.pop();
			}
		}
		//Adds whatever is left in the stack to the queue once the expression has been iterated through
		while(!S.empty()) {
			String result = S.pop();
			Q.add(result);
		}
		//Makes sure the postfix expression does not have any brackets left
		if (Q.contains("(") || Q.contains(")")) {
			throw new ParseException("Postfix expression should not contain any brackets",0);
		}
		return Q;
	}

	/**
	 * Takes the postfix expression that was obtained from infixToPostFix() and parses it into an expression tree
	 *
	 * @param line - String containing the expression
	 * @return root - Sets the root for when we have to evaluate the expressions
	 * @throws ParseException - For any errors during parsing
	 */

	private ExpressionTreeNode parsePostfix(String line) throws ParseException {

		//Takes the linked list queue from infixToPostfix() and converts it into an array
		String[] temp = infixToPostfix(line).toArray(new String[0]);

		//String builder converts the list to a string
		StringBuilder a = new StringBuilder();
		for(String s : temp) {
			a.append(s + " ");
		}

		//Trims the string to remove beginning and end white spaces, creating the postfix expression string
		String expr = a.toString().trim();

		//Sets new string expr to tokens
		StringTokenizer E = new StringTokenizer(expr);

		//Initialize st
		Stack<ExpressionTreeNode> S = new Stack<ExpressionTreeNode>();

		//Runs while the expression has not fully been iterated through yet
		while (E.hasMoreTokens()) {
			String t = E.nextToken();

			//Checks if the next token is an integer
			if (isInt(t)) {
				ExpressionTreeNode T = new ExpressionTreeNode(t, null, null);
				S.push(T);
			}
			//Anything else is an operator
			else {
				if (S.size() < 2) {
					throw new ParseException("Stack size needs to be at least 2",0);
				}
				else {
					ExpressionTreeNode T2 = S.pop();
					ExpressionTreeNode T1 = S.pop();
					ExpressionTreeNode T = new ExpressionTreeNode(t,T1,T2);
					S.push(T);
				}
			}
		}
		//Sets the root for the expression tree
		if (S.size() == 1) {
			ExpressionTreeNode T = S.pop();
			ExpressionTreeNode root = T;
			return root;
		}
		else {
			throw new ParseException("Error: Stack is empty", 0);
		}
	}

	/**
	 * Evaluates the expression tree and gives an integer value
	 *
	 * @return eval(root) - The integer value of the expression tree from the eval() method
	 */

	public int evaluate() {
	  return eval(root);
  }

	/**
	 * Evaluates the expression tree. Returns 0 if the tree is empty.
	 *
	 * @param node - The node in the expression tree currently being evaluated
	 * @return 0 - if the tree is empty
	 * @return A value representing an operation that was done (addition, subtraction, multiplication, division)
	 */

	public int eval(ExpressionTreeNode node) {

		//Checks if tree is empty
		if (node == null) {
			return 0;
		}
		//Only returns the root if there is no left and right child
		if (node.left == null && node.right == null) {
			return Integer.parseInt(node.el);
		}
		//Variables for left and right child
		int leftValue = eval(node.left);
		int rightValue = eval(node.right);

		//Checks for addition
		if (node.el.equals("+")) {
			return leftValue + rightValue;
		}
		//Checks for subtraction
		else if (node.el.equals("-")) {
			return leftValue - rightValue;
		}
		//Checks for multiplication
		else if (node.el.equals("*")) {
			return leftValue * rightValue;
		}
		//Checks for division
		else {
			return leftValue / rightValue;
		}
	}
  
  /**
   * Simplifies the tree to a specified height h (h >= 0). After simplification, the tree has a height of h.
   * Any subtree rooted at a height of h is replaced by a leaf node containing the evaluated result of that subtree.
   * If the height of the tree is already less than the specified height, the tree is unaffected.
   *
   * @param h - The height to simplify the tree to.
   */

  public void simplify( int h ) { simplifyNode(root, h); }

	/**
	 * Simplifies the expression tree and is then called to the simplify() method
	 *
	 * @param node - The node in the expression tree
	 * @param h - The height to simplify the tree to
	 */

  public void simplifyNode(ExpressionTreeNode node, int h) {

  	//Checks if the tree is not an empty tree
  	if (node == null) {
  		return;
  	}
  	//Sets the root to the fully evaluated expression tree because there should only be one node
  	if (h == 0) {
  		node.el = eval(node) + " ";
  		node.right = node.left = null;
  	}
  	//Continues simplifying the tree until the required height h
  	if (h > 0) {
  		simplifyNode(node.left, h-1);
  		simplifyNode(node.right, h-1);
  	}
  }
	
  /**
   * Returns a parentheses-free postfix representation of the expression. Tokens 
   * are separated by whitespace. An empty tree returns a zero length string.
   *
   * @return postOrder(root,"") - the post order expression of the expression tree
   */

	public String postfix() { return postOrder(root,""); }

	/**
	 * Evaluates the expression tree to make a post order expression
	 *
	 * @param node - The node from the expression tree
	 * @param str - A starting string for the post order expression, it is given as "" from the postfix() method so
	 *            that it starts as a blank and is then made into its post order expression
	 * @return "" - if there is an empty tree
	 * @return value - The String that represents the post order expression
	 */

	private String postOrder(ExpressionTreeNode node, String str) {
		if (root == null) {
			return "";
		}
		if (node != null) {
			str = postOrder(node.left, str);
			str = postOrder(node.right, str);
			str = str.concat(node.el + " ");
		}
		return str;
	}
  
  /**
   * Returns a parentheses-free prefix representation of the expression. Tokens are 
   * separated by whitespace. An empty tree returns a zero length string.
   *
   * @return preOrder(root,"") - the pre order expression of the expression tree
   */

  public String prefix() { return preOrder(root,""); }

	/**
	 * Evaluates the expression tree to make a pre order expression
	 *
	 * @param node - The node from the expression tree
	 * @param str - A starting string for the pre order expression, it is given as "" from the prefix() method so
	 *            that it starts as a blank and is then made into its pre order expression
	 * @return "" - if there is an empty tree
	 * @return value - The String that represents the pre order expression
	 */

	private String preOrder(ExpressionTreeNode node, String str) {
		if (root == null) {
			return "";
		}
		if (node != null) {
			str = str.concat(node.el + " ");
			str = preOrder(node.left, str);
			str = preOrder(node.right, str);
		}
		return str;
	}

	/**
	 * Helper method to test if strings in expressions are integers
	 *
	 * @param s - The string to be evaluated as an integer
	 * @return true or false - depending on if the string is an integer or not
	 */

	public boolean isInt(String s) {
		try {
			Integer.parseInt(s);
		}
		//If the string is not an integer
		catch (NumberFormatException e) {
			return false;
		}
		return true;
	}

	/**
	 * Helper method to test is a string is an operator
	 *
	 * @param s - The string to be evaluated as an operator
	 * @return true or false - depending on if the string is an operator or not
	 */

	public boolean isOper(String s) {
  		if (s.equals("+") || s.equals("-") || s.equals("*") || s.equals("/")) {
  			return true;
		}
  		else {
  			return false;
		}
	}

	/**
	 * Helper method for testing precedence of operators
	 *
	 * @param s - The string being tested for precedence
	 * @return 0 - if lower precedence
	 * @return 1 - if higher precedence
	 */
	
	public int precedence(String s) {
		//Lowest precedence
  		if (s.equals("+") || s.equals("-")) {
  			return 0;
		}
  		//Otherwise the string is a * or / so it has higher precedence
  		else {
  			return 1;
		}
	}
}