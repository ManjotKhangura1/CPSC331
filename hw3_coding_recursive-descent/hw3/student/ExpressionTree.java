package hw3.student;

import java.text.ParseException;
import java.util.ArrayList;

public class ExpressionTree implements ExpressionTreeInterface {

	private ExpressionTreeNode root;

	/**
	 * Constructor to build an empty parse tree.
	 */

	public ExpressionTree() {
		root = null;
	}

	/**
	 * Build a parse tree from an expression.
	 *
	 * @param line String containing the expression
	 * @throws ParseException If an error occurs during parsing.
	 */

	public void parse(String line) throws ParseException {
		//Makes sure the expression is not empty
		if (line.equals("")) {
			throw new ParseException("Empty string, can not parse", 0);
		}
		//Creates an arraylist of the tokens in the expression
		String[] expr = line.split(" ");
		ArrayList<String> ex = new ArrayList<>();
		for (int i=0;i<expr.length;i++) {
			ex.add(expr[i]);
		}

		//Parses the expression
		root = E(ex);
	}

	/**
	 * Method for expression which checks for addition and subtraction
	 *
	 * @param ex - The arraylist of the expression tokens
	 * @return p1 - The new expression tree node for that part of the expression
	 * @throws ParseException - Throws error when parsing. Done for extra operators
	 * and extra brackets in this method
	 */

	public ExpressionTreeNode E(ArrayList<String> ex) throws ParseException {

		//Makes sure there aren't extra operators at the start and end of the expression
		if (isOper(ex.get(0)) || isOper(ex.get(ex.size() - 1))) {
			throw new ParseException("Error: extra operator",0);
		}

		/*
		Makes sure there aren't brackets at the start and end of the expression in ways
		that they shouldn't be
		*/
		if (isBrackets(ex.get(0)) == 2 || isBrackets(ex.get(ex.size() - 1)) == 1) {
			throw new ParseException("Error: Extra bracket",0);
		}

		String oper;
		ExpressionTreeNode p1, p2;

		//Calls method to parse the term for the left side
		p1 = T(ex, 0);

		String nextToken = ex.get(0);
		for(int i = 0; i < ex.size(); i++) {

			//Separates expressions in brackets
			if(nextToken.equals("(")) {
				i = checkBrackets(ex, i);
			}

			if (nextToken.equals("+") || nextToken.equals("-")) {
				oper = nextToken;
				//Need to parse another term (right side)
				p2 = T(ex, i);
				p1 = new ExpressionTreeNode(oper,p1,p2);
			}
			nextToken = ex.get(i);
		}
		return p1;
	}

	/**
	 * Method for each term which checks for multiplication and division. It also
	 * makes sure that there aren't any additions or subtractions left to check
	 * or else it needs to break the loop and move on until they are checked
	 *
	 * @param ex - The arraylist of the expression tokens
	 * @param index - The index of te arraylist that was passed on to be parsed
	 * @return p1 - The new expression tree node for that term
	 * @throws ParseException - Throws error when parsing
	 */

	public ExpressionTreeNode T(ArrayList<String> ex, int index) throws ParseException {
		String oper;
		ExpressionTreeNode p1, p2;

		//Calls method to parse the factor for the left side
		p1 = F(ex, index);

		String nextToken = ex.get(index);
		for(int i = index; i < ex.size(); i++) {

			//Separates terms in brackets
			if(nextToken.equals("(")) {
				i = checkBrackets(ex, i);
			}
			if (nextToken.equals("*") || nextToken.equals("/")) {
				oper = nextToken;
				//Need to parse another factor (right side)
				p2 = F(ex, i);
				p1 = new ExpressionTreeNode(oper,p1,p2);
			}
			if (nextToken.equals("+") || nextToken.equals("-")) {
				break;
			}
			nextToken = ex.get(i);
		}
		return p1;
	}

	/**
	 * Method for every factor which first checks to see if there are opening brackets
	 * and then make pass the subArray within those brackets back to the expression
	 * method E(). Then it checks if the factor being parsed is an integer or not.
	 *
	 * @param ex - The arraylist of the expression tokens
	 * @param index - The index of te arraylist that was passed on to be parsed
	 * @return p1 - The node that is called back to method E() for expressions in
	 * brackets
	 * @throws ParseException - If there is an error checking for if a factor is an
	 * integer
	 */

	public ExpressionTreeNode F(ArrayList<String> ex, int index) throws ParseException {
		ExpressionTreeNode p1;
		String nextToken = ex.get(index);

		//Checks for the opening bracket
		if (nextToken.equals("(")) {
			//Returns the expression in the brackets
			p1 =  E(subArray(ex, index));
		}

		//Looks for each factor to make sure it's an integer;
		else {
			String number = ex.get(index);
			try {
				Integer.parseInt(number);
				return new ExpressionTreeNode(number);
			} catch (NumberFormatException e) {
				throw new ParseException("Not a integer: " + number, 0);
			}
		}
		return p1;
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

	public void simplify(int h) {
		simplifyNode(root, h);
	}

	/**
	 * Simplifies the expression tree and is then called to the simplify() method
	 *
	 * @param node - The node in the expression tree
	 * @param h    - The height to simplify the tree to
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
		else {
			h -= 1;
			simplifyNode(node.left, h);
			simplifyNode(node.right, h);
		}
	}

	/**
	 * Returns a parentheses-free postfix representation of the expression. Tokens
	 * are separated by whitespace. An empty tree returns a zero length string.
	 *
	 * @return postOrder(root, " ") - the post order expression of the expression tree
	 */

	public String postfix() {
		return postOrder(root, "");
	}

	/**
	 * Evaluates the expression tree to make a post order expression
	 *
	 * @param node - The node from the expression tree
	 * @param str  - A starting string for the post order expression, it is given as "" from the postfix() method so
	 *             that it starts as a blank and is then made into its post order expression
	 * @return value - The String that represents the post order expression
	 */

	private String postOrder(ExpressionTreeNode node, String str) {
		//Empty root shouldn't have any expression
		if (root == null) {
			return "";
		}
		//Taken from lecture notes on binary trees
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
	 * @return preOrder(root, " ") - the pre order expression of the expression tree
	 */

	public String prefix() {
		return preOrder(root, "");
	}

	/**
	 * Evaluates the expression tree to make a pre order expression
	 *
	 * @param node - The node from the expression tree
	 * @param str  - A starting string for the pre order expression, it is given as "" from the prefix() method so
	 *             that it starts as a blank and is then made into its pre order expression
	 * @return value - The String that represents the pre order expression
	 */

	private String preOrder(ExpressionTreeNode node, String str) {
		//Empty root shouldn't have any expression
		if (root == null) {
			return "";
		}
		//Taken from lecture notes on binary trees
		if (node != null) {
			str = str.concat(node.el + " ");
			str = preOrder(node.left, str);
			str = preOrder(node.right, str);
		}
		return str;
	}

	/**
	 * Helper method to check if a given String is an operator
	 *
	 * @param s - The string in the arraylist to be checked
	 * @return true if the given string is an operator, false if the given
	 * string is not an operator
	 */

	public boolean isOper(String s) {
		//Initialize an arraylist
		ArrayList<String> tempArray = new ArrayList<String>();
		tempArray.add("+");
		tempArray.add("-");
		tempArray.add("*");
		tempArray.add("/");
		//Check if an operator is in the arraylist
		if (tempArray.contains(s)) {
			return true;
		}
		else {
			return false;
		}
	}

	/**
	 * Checks if a given string is an opening bracket, closing bracket, or neither
	 * @param s - The given string in the arraylist
	 * @return 1 if an opening bracket, 2 if a closing bract, and 0 if neither
	 */

	public int isBrackets(String s) {
		if (s.equals("(")) {
			return 1;
		}
		else if (s.equals(")")) {
			return 2;
		}
		else {
			return 0;
		}
	}

	/**
	 * Checks to see if an opening bracket has an accompanying closing bracket
	 *
	 * @param a - The given arraylist of the expression
	 * @param index - The index of the arraylist of where the opening bracket is
	 * @return - The index of the earliest closing bracket, if none then returns the
	 * index of the end of the list + 1
	 */

	public int checkBrackets(ArrayList<String> a, int index) {
		//Keeps checking until a closing bracket is found
		while (!a.get(index).equals(")")) {
			index += 1;
			if(index == a.size()) {
				break;
			}

			/*
			 * Recursively calls itself if another opening bracket is found. This is so
			 * that it rechecks for another closing bracket and try and match the number
			 * of opening brackets to closing brackets
			 */
			if(a.get(index).equals("(")) {
				index = checkBrackets(a, index + 1) + 1;
			}
		}
		return index;
	}

	/**
	 * Creates a sub array within a pair of brackets. It uses the checkBrackets()
	 * method to find which brackets to start at and to end
	 *
	 * @param a - The given arraylist of the expression
	 * @param index - The index of the arraylist of where the opening bracket is
	 * @return an arraylist called tempArray that is the sub array wihtin a pair
	 * of brackets
	 */
	public ArrayList<String> subArray(ArrayList<String> a, int index) {

		//Takes index of closing bracket from checkBrackets() method
		int index1 = checkBrackets(a, index);

		//Initialize arraylist
		ArrayList<String> tempArray = new ArrayList<String>();
		for(int i = index + 1; i < index1; i++) {
			tempArray.add(a.get(i));
		}
		return tempArray;
	}
}