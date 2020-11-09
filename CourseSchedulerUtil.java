
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.Stack;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

/**
 * Filename:   CourseSchedulerUtil.java
 * Project:    p4
 * Authors:    William Miller, Ellie Beres, Lecture 002
 *
 * Semester:   Fall 2018
 * Course:     CS400
 * 
 * Due Date:   First Due Date: November 19, 2018 at 10:00 PM
 * Version:    1.0
 * 
 * Credits:   
 * 
 * Bugs:       No known bugs. 
 */

/**
 * 
 * @author William Miller, Ellie Beres 
 * 
 *         The CourseSchedulerUtil.java class creates a graph, fills it, and uses the 
 *         graph to solve a set of problems. These problems include listing all the available 
 *         courses, determining if the courses can be completed, finding an order in which 
 *         the classes can be completed and finding the minimum number of classes one must
 *         take to complete a class. 
 *
 * @param <T>
 */
public class CourseSchedulerUtil<T> {

	/**
	 * Graph object
	 */
	private GraphImpl<T> graphImpl;
	private Entity<T>[] entityArray;

	/**
	 * constructor to initialize a graph object
	 */
	public CourseSchedulerUtil() {
		this.graphImpl = new GraphImpl<T>();
	}

	/**
	 * createEntity method is for parsing the input json file
	 * 
	 * @return array of Entity object which stores information about a single course
	 *         including its name and its prerequisites
	 * @throws Exception like FileNotFound, JsonParseException
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public Entity[] createEntity(String fileName) throws Exception {
		JSONParser parser = new JSONParser();
		Object obj = parser.parse(new FileReader(fileName));
		JSONObject jsonObject = (JSONObject) obj;

		// extract "entity objects"
		JSONArray courses = (JSONArray) jsonObject.get("courses");
		int entityLength = courses.size();
		entityArray = new Entity[entityLength];

		// set each element of entity array to entity object
		for (int i = 0; i < entityLength; ++i) {
			JSONObject entityObj = (JSONObject) courses.get(i);
			T courseName = (T) entityObj.get("name"); // extract name from entity object

			// extract prereqs from JSONArray and put into array of type T
			JSONArray jsonArray = (JSONArray) entityObj.get("prerequisites");
			Object[] objectArray = new Object[jsonArray.size()];
			T[] prereqArray = (T[]) objectArray;
			for (int k = 0; k < jsonArray.size(); k++) {
				Object prereq = (Object) jsonArray.get(k);
				prereqArray[k] = (T) prereq;
			}
			// create and set the correct data of entity object
			Entity<T> entity = new Entity<>();
			entity.setName(courseName);
			entity.setPrerequisites(prereqArray);
			entityArray[i] = entity;
		}
		return entityArray;
	}

	/**
	 * Construct a directed graph from the created entity object
	 * 
	 * @param entities which has information about a single course including its
	 *                 name and its prerequisites
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void constructGraph(Entity[] entities) {
		graphImpl = new GraphImpl<>();
		for (int i = 0; i < entities.length; i++) {
			T rootVert = (T) entities[i].getName();
			graphImpl.addVertex(rootVert);
			for (int j = 0; j < entities[i].getPrerequisites().length; j++) {
				T prereqVert = (T) entities[i].getPrerequisites()[j];
				graphImpl.addVertex(prereqVert);
				// add edge between prereq and class
				graphImpl.addEdge(rootVert, prereqVert);
			}
		}
//		graphImpl.printGraph();
//		Set<T> allVerts = graphImpl.getAllVertices();
//		for (T vertex : allVerts) {
//			List<T> adjacentVerts = graphImpl.getAdjacentVerticesOf(vertex);
//			System.out.println(vertex + ": " + adjacentVerts);

	}

	/**
	 * Returns all the unique available courses
	 * 
	 * @return the sorted list of all available courses
	 */
	public Set<T> getAllCourses() {
		return graphImpl.getAllVertices();
	}

	/**
	 * To check whether all given courses can be completed or not. If a cycle exists
	 * in the course path, then the classes can not be completed.
	 * 
	 * @return boolean true if all given courses can be completed, otherwise false
	 * @throws Exception
	 */
	public boolean canCoursesBeCompleted() throws Exception {
		// if course path has a cycle, then the courses cannot be completed
		try {
			// stack to be used in helper method
			Set<T> stack = new HashSet<T>();
			// get all courses to be checked and store
			Set<T> courses = this.getAllCourses();

			for (T course : courses) {
				// if cycle exists in course path
				if (isCoursePathCyclic(course, stack)) {
					return false;
				}
			}
			return true;

		} catch (Exception e) {
			return false;
		}
	}

	/**
	 * Recursive helper method to check if a cycle exists in the course path.
	 * 
	 * @return boolean true if courses contain a cycle, false otherwise.
	 */
	private boolean isCoursePathCyclic(T course, Set<T> stack) {

		// part of recursion stack
		if (stack.contains(course))
			return true;

		stack.add(course);
		List<T> adjCourses = graphImpl.getAdjacentVerticesOf(course);

		for (T adjCourse : adjCourses) {
			// recursively check if adjacent course paths contain a cycle
			if (isCoursePathCyclic(adjCourse, stack))
				return true;
		}
		stack.remove(course);

		return false;
	}

	/**
	 * The order of courses in which the courses has to be taken
	 * 
	 * @return the list of courses in the order it has to be taken
	 * @throws Exception when courses can't be completed in any order
	 */
	public List<T> getSubjectOrder() throws Exception {
		List<T> visited = new ArrayList<>(); // arrayList containing visited vertices
		List<T> subjectOrder = new ArrayList<>(); // arrayList of sorted class order
		Queue<T> queue = new LinkedList<>();
		Set<T> vertices = graphImpl.getAllVertices(); // set of all vertices
		boolean hasParent = false;

		if (canCoursesBeCompleted()) {
			// find vertices with no predecessors to first put in queue
			for (T vertex : vertices) {
				hasParent = false;
				for (T neighbor : vertices) {
					if (graphImpl.getAdjacentVerticesOf(neighbor).contains(vertex)) {
						hasParent = true;
					}
				}
				if (hasParent == false) {
					queue.add(vertex);
				}
			}

			// iteratively add children of parent vertices to queue and then to arrayList
			while (!queue.isEmpty()) {
				T current = queue.remove();
				subjectOrder.add(current);
				visited.add(current);
				List<T> neighbors = graphImpl.getAdjacentVerticesOf(current);
				for (T vertex : neighbors) {
					if (!visited.contains(vertex)) {
						queue.add(vertex);

					}
				}
			}

			Collections.reverse(subjectOrder); // reverse order of arrayList

			return subjectOrder;
		} else {
			throw new Exception("Exception Occured.");
		}
	}

	/**
	 * The minimum course required to be taken for a given course
	 * 
	 * @param courseName
	 * @return the number of minimum courses needed for a given course
	 */
	public int getMinimalCourseCompletion(T courseName) throws Exception {
		// get all all prerequisite courses for courseName and store
		List<T> prereqs = graphImpl.getAdjacentVerticesOf(courseName);
		// keeps track of amount of courses needed
		int totalCourses = 0;

		// if cycle does not exist and courses can be completed
		if (this.canCoursesBeCompleted()) {
			// if course has only one prerequisite
			if (prereqs.size() == 1) {
				// recursively check prerequisite course and increment totalCourses
				totalCourses += 1 + getMinimalCourseCompletion(prereqs.get(0));
			}
			// if course has multiple prerequisites
			if (prereqs.size() > 1) {
				for (int i = 0; i < prereqs.size(); i++) {
					// recursively check prerequisite courses and increment totalCourses
					totalCourses += 1 + getMinimalCourseCompletion(prereqs.get(i));
				}
			}
			return totalCourses;
		}
		return -1;

	}

}
