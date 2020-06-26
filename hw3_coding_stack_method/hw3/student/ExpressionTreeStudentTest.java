package hw3.student;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;

import java.text.ParseException;

import static org.hamcrest.Matchers.equalToIgnoringWhiteSpace;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

//import hw3.student.ExpressionTreeInterface;

/**
 * Tester class for implementation of ExpressionTree class.
 * 
 * @author Usman Alim
 */

public class ExpressionTreeStudentTest {

  private ExpressionTreeInterface tree;

  /**
   * @throws java.lang.Exception
   */
  @Before
  public void setupUnit() throws Exception {
    this.tree = new ExpressionTree();
  }

  @Test
  public void basicTests() throws ParseException {
    String in = "331";
    int outVal = 331;
    String _message;
    
    _message = message(in, "evaluate()", "" + outVal );    
    System.out.println(_message);
    tree.parse( in );
    assertEquals( _message, outVal, tree.evaluate() );

    in = "331";
    String out = "331";
    _message = message(in, "prefix()", out );
    System.out.println(_message);
    tree.parse( in );
    assertTrue( _message, equalToIgnoringWhiteSpace(out.trim()).matches( tree.prefix().trim() ) );

    in = "331";
    out = "331";
    _message = message(in, "postfix()", out );
    System.out.println(_message);
    tree.parse( in );
    assertTrue( _message, equalToIgnoringWhiteSpace(out.trim()).matches( tree.postfix().trim() ) );

  }

  @Test
  public void precedenceTest() throws ParseException {
    String in = "9 - 5 * 2";
    int outVal = -1;
    String _message;
    
    _message = message(in, "evaluate()", "" + outVal );
    System.out.println( _message );
    tree.parse( in );
    assertEquals( _message, outVal, tree.evaluate() );

    String out = "- 9 * 5 2";
    _message = message(in, "prefix()", out );
    System.out.println( _message );
    tree.parse( in );
    assertTrue( _message, equalToIgnoringWhiteSpace(out.trim()).matches( tree.prefix().trim() ) );

    out = "9 5 2 * -";
    _message = message(in, "postfix()", out );
    System.out.println(_message);
    tree.parse( in );
    assertTrue( _message, equalToIgnoringWhiteSpace(out.trim()).matches( tree.postfix().trim() ) );
  }

  @Test
  public void longExpressionTest() throws ParseException {
    String in = "-1 * ( 2 + 2 * ( 7 + 2 * ( 11 * 2 - 4 ) ) ) / ( 1 + ( 2 - 3 ) * 2 )";
    int outVal = 88;
    String _message;
    
    _message = message(in, "evaluate()", "" + outVal );
    System.out.println(_message);
    tree.parse( in );
    assertEquals( _message, outVal, tree.evaluate() );

    String out = "/ * -1 + 2 * 2 + 7 * 2 - * 11 2 4 + 1 * - 2 3 2";
    _message = message(in, "prefix()", out );
    System.out.println(_message);
    tree.parse( in );
    assertTrue( _message, equalToIgnoringWhiteSpace(out.trim()).matches( tree.prefix().trim() ) );

    out = "-1 2 2 7 2 11 2 * 4 - * + * + * 1 2 3 - 2 * + /";
    _message = message(in, "postfix()", out );
    System.out.println(_message);
    tree.parse( in );
    assertTrue( _message, equalToIgnoringWhiteSpace(out.trim()).matches( tree.postfix().trim() ) );
  }

  @Test
  public void simplifyTest() throws ParseException {
    String in = "( 2 + 2 * ( 7 + 2 * ( 11 * 2 - 4 ) ) ) / ( 1 + ( 2 - 3 ) * 2 )";
    String out = "-88";
    String _message;
    
    _message = message(in, "simplify(0); prefix()", out );
    System.out.println(_message);
    tree.parse( in );
    tree.simplify(0);
    assertTrue( _message, equalToIgnoringWhiteSpace(out.trim()).matches( tree.prefix().trim() ) );

    out = "2 2 7 36 + * + 1 2 3 - 2 * + /";
    _message = message(in, "simplify(4); postfix()", out );
    System.out.println(_message);
    tree.parse( in );
    tree.simplify(4);
    assertTrue( _message, equalToIgnoringWhiteSpace(out.trim()).matches( tree.postfix().trim() ) );

    out = "/ + 2 * 2 + 7 * 2 - * 11 2 4 + 1 * - 2 3 2";
    _message = message(in, "simplify(10); prefix()", out );
    System.out.println(_message);
    tree.parse( in );
    tree.simplify(10);
    assertTrue( _message, equalToIgnoringWhiteSpace(out.trim()).matches( tree.prefix().trim() ) );

  }

  @Test(expected = ParseException.class)
  public void exceptionTestDelimiterMismatch() throws ParseException {
    String in = "9 +  ( ( 5 * 2 ) 3 ";
    String _message = message(in, "parse()", "should throw ParseException");
    System.out.println(_message);
    tree.parse( in );
  }

  @Test(expected = ParseException.class)
  public void exceptionTestIncompleteExpression() throws ParseException {
    String in = "( 2 + 3 ) * ( 2 - 3 ) + ";
    String _message = message(in, "parse()", "should throw ParseException");
    System.out.println(_message);
    tree.parse( in );
  }

  @Test(expected = ParseException.class)
  public void exceptionTestUnexpectedCharacter() throws ParseException {
    String in = "( a + b ) * ( a - b ) ";
    String _message = message(in, "parse()", "should throw ParseException");
    System.out.println(_message);
    tree.parse( in );
  }

  private String message( String input, String test, String output ) {
    return "\n" +
        "Input Expression: " + input + "\n" +
        "Tested Method: " + test + "\n" +
        "Expected Output: " + output + "\n";
  }

  public static void main( String[] args ) {
    Result result = JUnitCore.runClasses( ExpressionTreeStudentTest.class );
    for (Failure failure : result.getFailures()) {
      System.out.println(failure.toString());
    }

  }

}
