package hw4.student;

import java.io.FileNotFoundException;

public interface GridGraphInterface {

  /**
   * Builds a grid graph from a specified file. It is assumed
   * that the input file is formatted correctly.
   *
   * @param filename
   */
  public static void buildGraph(String filename) throws FileNotFoundException {

  }

  /**
   * Finds the shortest path between a source vertex and a target vertex
   * using Dijkstra's algorithm. 
   * @param s Source vertex (one based index)
   * @param t Target vertex (one based index) 
   * @param weighted Whether edge weights should be used or not.
   * @return A String encoding the shortest path. Vertices are
   *         separated by whitespace.  
   */
  public String findShortestPath( int s, int t, boolean weighted ) throws FileNotFoundException;

}
