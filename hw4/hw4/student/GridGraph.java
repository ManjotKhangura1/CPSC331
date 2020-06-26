package hw4.student;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Class to represent a Weighted Grid Graph. Methods are 
 * provided to build a graph from a file and to find 
 * shortest paths.
 * 
 */

public class GridGraph implements GridGraphInterface {

  /**
   * Initializes the arraylist for when the lines in the input files are being read
   */
  private ArrayList<String> graph = new ArrayList<>();

  /**
   * Initializes the adjacency matrix which will be used to find the shortest path
   */
  private int[][] adjMatrix;

  /**
   * Initializes the length of one side the graph being used from the input file
   */
  private int size;

  /**
   * Initializes the total number of vertices in the adjacency matrix which is also it's total length
   */
  private int matrixVertices;

  /**
   * Default constructor
   */
  public GridGraph() {
  }

  /**
   * Builds a grid graph from a specified file. It is assumed that the input file is formatted correctly.
   * The input file is read by a BufferReader, each line is made into an index of an Arraylist, and then an adjacency
   * matrix is made which holds all the weights of edges of the input graph.
   *
   * @param filename - The input file representing the graph
   * @throws FileNotFoundException - Throws exception if there is no file found
   */
  public void buildGraph(String filename) throws FileNotFoundException {
    try {

      BufferedReader reader = new BufferedReader(new FileReader(filename));

      //Each line in the input file
      String line;

      //Adds all the lines into an ArrayList
      while ((line = reader.readLine()) != null) {
        graph.add(line.trim());
      }
      reader.close();

      //First line in the file is assumed to be the length of one side of the graph
      size = Integer.parseInt(graph.get(0));

      //One side of the matrix has to represent all the vertices
      matrixVertices = size*size;

      //Creates the adjacency matrix
      adjMatrix = new int[matrixVertices][matrixVertices];

      /*
      Connects and vertex which has a weight > 0 associated with it by using the ArrayList. Each line, except the
      first, has 3 integers. The first integer becomes the first vertex. The second number is the vertex attached
      to the first vertex. The third integer is the weight associated between them. This creates the edges and
      weights in the adjacency matrix.
      */
      for (int k = 1; k < graph.size(); k++) {
        String[] temp = graph.get(k).split("\t");
        adjMatrix[Integer.parseInt(temp[0]) - 1][Integer.parseInt(temp[1]) - 1] = Integer.parseInt(temp[2]);
      }

    }
    catch (IOException e) {
      throw new FileNotFoundException("File " + filename + " not found");
    }

  }


  /**
   * If the boolean weighted is false, then it takes all the non-zero values in the adjacency matrix and replaces
   * them with 1's. Then it finds the shortest path between a source vertex and a target vertex using
   * Dijkstra's algorithm.
   *
   * @param s - Source vertex (one based index)
   * @param t - Target vertex (one based index)
   * @param weighted - Whether edge weights should be used or not.
   * @return - A String encoding the shortest path. Vertices are
   * separated by whitespace.
   */
  public String findShortestPath(int s, int t, boolean weighted) {

    int counter = 0;
    int counter2 = 0;

    //Checks if the weighted boolean is false to replace weights with 1's
    if(!weighted) {
      while (counter < adjMatrix.length) {
        while (counter2 < adjMatrix.length) {
          if (adjMatrix[counter][counter2] != 0) {
            adjMatrix[counter][counter2] = 1;
          }
          counter2++;
        }
        counter++;
        counter2 = 0;
      }
    }

    //If the source and target vertex are the same, it returns a blank string
    if (s == t) {
      return "";
    }

    String shortestDist = "";

    //Checks if a vertex has been visited or not
    boolean[] visited = new boolean[matrixVertices];

    //Keeps track of the distance up to a certain vertex in the path
    int[] distance = new int[matrixVertices];

    //Predecessor of a vertex in the shortest path
    int[] predecessor = new int[matrixVertices];

    //Sets all the vertex distances, except for the vertex we start at, to infinity
    for (int i = 0; i < matrixVertices; i++) {
      if (i == s - 1) {
        distance[s - 1] = 0;
      } else {
        distance[i] = Integer.MAX_VALUE;
      }
    }

    //Finds the shortest path and records all the predecessors that were found as well as the shortest distance
    for (int i = 1; i < matrixVertices; i++) {
      int min = minVertex(distance, visited);
      visited[min] = true;
      for (int j = 0; j < matrixVertices; j++) {
        if (!visited[j] && (distance[min] < Integer.MAX_VALUE) && (adjMatrix[min][j] > 0)) {
          if (distance[j] > (distance[min] + adjMatrix[min][j])) {
            distance[j] = (distance[min] + adjMatrix[min][j]);
            /*
            Puts all the predecessors into the list. It's min + 1 because we start the graph at vertex 1 but the indices
            of any list start at 0.
            */
            predecessor[j] = min + 1;
          }
        }
      }
    }

    /*
    Creates the string from the predecessors. If there is an index error it catches it and returns a blank
    string, this indicates that there is no shortest path from the source to target vertices
    */
    try {

      shortestDist = shortestDist.concat(t + " ");
      t -= 1;
      while (predecessor[t] != s) {
        shortestDist = shortestDist.concat(predecessor[t] + " ");
        t = predecessor[t] - 1;
      }
      shortestDist = shortestDist.concat(s + "");

      //Reverses the string since the earlier string is from the target vertex to the source vertex
      String shortestDistReverse = "";
      String[] temp = shortestDist.split(" ");

      for (int i = temp.length - 1; i >= 0; i--) {
        shortestDistReverse = shortestDistReverse.concat(temp[i] + " ");
      }
      return shortestDistReverse.trim();

    } catch (ArrayIndexOutOfBoundsException e) {
      return "";
    }
  }

  /**
   * Helper method to find the next vertex with the minimum distance
   *
   * @param distance - The distance of the vertex from the start
   * @param visited - Boolean to make sure we don't go back to visited vertices
   * @return - The minimum vertex with the shortest distance
   */
  private static int minVertex(int[] distance, boolean[] visited) {

    //key starts out as infinity but it will decrease to the next lowest distance every iteration
    int key = Integer.MAX_VALUE;

    //Starts it as the lowest possible value because we have not reached any vertex yet
    int minV = Integer.MIN_VALUE;

    for (int i = 0; i < distance.length; i++) {
      if (!visited[i] && (minV == Integer.MIN_VALUE || distance[i] < key)) {
        key = distance[i];
        minV = i;
      }
    }
    return minV;
  }
}
