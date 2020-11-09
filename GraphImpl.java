import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;


/**
 * Filename:   GraphImpl.java
 * Project:    p4
 * Course:     cs400 
 * Authors:    William Miller, Ellie Beres
 * Due Date:   
 * 
 * T is the label of a vertex, and List<T> is a list of
 * adjacent vertices for that vertex.
 *
 * Additional credits: 
 *
 * Bugs or other notes: 
 *
 * @param <T> type of a vertex
 */

/**
 * 
 * @author William Miller, Ellie Beres 
 * 
 *         The GraphImpl.java class implements generic Graph in java through the use of 
 *         methods that add and remove edges and vertices. In addition, the class determines the order, 
 *         size, and adjacent vertices of a single vertex. Finally, the class prints the graph.
 *
 * @param <T>
 */
public class GraphImpl<T> implements GraphADT<T> {

    // YOU MAY ADD ADDITIONAL private members
    // YOU MAY NOT ADD ADDITIONAL public members

    /**
     * Store the vertices and the vertice's adjacent vertices
     */
    private Map<T, List<T>> verticesMap; 
    
    
    /**
     * Construct and initialize and empty Graph
     */ 
    public GraphImpl() {
        verticesMap = new HashMap<T, List<T>>();
        // you may initialize additional data members here
    }
    
    /**
     * Add new vertex to the graph.
     *
     * If vertex is null or already exists,
     * method ends without adding a vertex or 
     * throwing an exception.
     * 
     * Valid argument conditions:
     * 1. vertex is non-null
     * 2. vertex is not already in the graph 
     * 
     * @param vertex the vertex to be added
     */
    public void addVertex(T vertex) {
    	if(verticesMap == null) {
			verticesMap = new HashMap<T, List<T>>();
		}
    	if (!(vertex == null) && !verticesMap.containsKey(vertex)) {
			verticesMap.put(vertex, new ArrayList<T>());
		}
    }
    
    /**
     * Remove a vertex and all associated 
     * edges from the graph.
     * 
     * If vertex is null or does not exist,
     * method ends without removing a vertex, edges, 
     * or throwing an exception.
     * 
     * Valid argument conditions:
     * 1. vertex is non-null
     * 2. vertex is not already in the graph 
     *  
     * @param vertex the vertex to be removed
     */
    public void removeVertex(T vertex) {
    	/* delete instance of the vertex contained in the 
    	 * adjacency list's keys
    	 */
    	if(!(vertex == null) && verticesMap.containsKey(vertex)) {
    		verticesMap.remove(vertex);
    		/* delete all instances of the vertex contained in the 
    		 * adjacency list's values
    		 */
    		for (T V : getAllVertices()) {
    			if (getAdjacentVerticesOf(V).contains(vertex)) {
    				getAdjacentVerticesOf(V).remove(vertex);
    			}
    		}
    	}
    }
    
    /**
     * Add the edge from vertex1 to vertex2
     * to this graph.  (edge is directed and unweighted)
     * If either vertex does not exist,
     * no edge is added and no exception is thrown.
     * If the edge exists in the graph,
     * no edge is added and no exception is thrown.
     * 
     * Valid argument conditions:
     * 1. neither vertex is null
     * 2. both vertices are in the graph 
     * 3. the edge is not in the graph
     *  
     * @param vertex1 the first vertex (src)
     * @param vertex2 the second vertex (dst)
     */
    public void addEdge(T vertex1, T vertex2) {
    	if (hasVertex(vertex1) && hasVertex(vertex2) && !(vertex1 == null) && !(vertex2 == null)
    			&& !(getAdjacentVerticesOf(vertex1).contains(vertex2)) && !(vertex1.equals(vertex2))) {
    		getAdjacentVerticesOf(vertex1).add(vertex2);
    	}
    }
    
    /**
     * Remove the edge from vertex1 to vertex2
     * from this graph.  (edge is directed and unweighted)
     * If either vertex does not exist,
     * or if an edge from vertex1 to vertex2 does not exist,
     * no edge is removed and no exception is thrown.
     * 
     * Valid argument conditions:
     * 1. neither vertex is null
     * 2. both vertices are in the graph 
     * 3. the edge from vertex1 to vertex2 is in the graph
     *  
     * @param vertex1 the first vertex
     * @param vertex2 the second vertex
     */
    public void removeEdge(T vertex1, T vertex2) {
    	if (!(vertex1 == null) && !(vertex2 == null) && hasVertex(vertex1) 
    			&& hasVertex(vertex2) && getAdjacentVerticesOf(vertex1).contains(vertex2)) {
    		verticesMap.get(vertex1).remove(vertex2);
    	}
    }    
    
    /**
     * Returns a Set that contains all the vertices
     * 
     * @return a java.util.Set<T> where T represents the vertex type
     */
    public Set<T> getAllVertices() {
        return verticesMap.keySet();
    }
    
    /**
     * Get all the neighbor (adjacent) vertices of a vertex
     * 
     * @param vertex the specified vertex
     * @return an List<T> of all the adjacent vertices for specified vertex
     */
    public List<T> getAdjacentVerticesOf(T vertex) {
    	if (!(vertex == null) && hasVertex(vertex)) {
    		return verticesMap.get(vertex);
    	}
    	return null;
    }
    
    /**
     * Checks if vertex exists in graph
     * 
     * @return True if in graph and False otherwise
     */
    public boolean hasVertex(T vertex) {
        return verticesMap.containsKey(vertex);
    }
    
    /**
     * Returns the number of vertices in this graph.
     * 
     * @return number of vertices in graph.
     */
    public int order() {
        return getAllVertices().size();
    }
    
    /**
     * Returns the number of edges in this graph.
     * 
     * @return number of edges in the graph.
     */
    public int size() {
        return verticesMap.size();
    }
    
    /**
     * Prints the graph for the reference
     * DO NOT EDIT THIS FUNCTION
     * DO ENSURE THAT YOUR verticesMap is being used 
     * to represent the vertices and edges of this graph.
     */
    public void printGraph() {

        for ( T vertex : verticesMap.keySet() ) {
            if ( verticesMap.get(vertex).size() != 0) {
                for (T edges : verticesMap.get(vertex)) {
                    System.out.println(vertex + " -> " + edges + " ");
                }
            } else {
                System.out.println(vertex + " -> " + " " );
            }
        }
    }
}

