import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

/**
 * This is a placeholder for the fully working Graph that you will develop
 * in a future week and then integrated with your role code.
 * It is designed to help develop and test the functionality
 * of your own Frontend role code this week.
 */
public class Graph_Placeholder implements GraphADT<String, Double> {

  protected List<String> path;

  public Graph_Placeholder() {
    path = new ArrayList<>();
    path.add("Union South");
    path.add("Computer Sciences and Statistics");
    path.add("Atmospheric, Oceanic and Space Sciences");
  }

  public boolean insertNode(String data) {
    if(path.size() < 4) { path.add(data); return true; }
    else return false;
  }

  public boolean removeNode(String data) {
    if(path.size() > 3 && path.contains(data)) {
	path.remove(data);
	return true;
    }
    else return false;
  }

  public boolean containsNode(String data) {
    return path.contains(data);
  }

  public List<String> getAllNodes() {
    return path;
  }
    
  public int getNodeCount() {
    return path.size();
  }

  public boolean insertEdge(String pred, String succ, Double weight) {
    return false;
  }

  public boolean removeEdge(String pred, String succ) {
    return false;
  }

  public boolean containsEdge(String pred, String succ) {
    for(int i=1;i<path.size();i++)
      if(path.get(i-1).equals(pred) && path.get(i).equals(succ)) return true;
    return false;
  }

  public Double getEdge(String pred, String succ) {
    for(int i=1;i<path.size();i++)
      if(path.get(i-1).equals(pred) && path.get(i).equals(succ))
	  return (double)i;
    throw new NoSuchElementException();
  }

  public int getEdgeCount() {
    return path.size() - 1;
  }

  public List<String> shortestPathData(String start, String end) {
    boolean capture = false;
    ArrayList<String> list = new ArrayList<>();
    for(int i=0;i<path.size();i++) {
      if(path.get(i).equals(start)) capture=true;	
      if(capture == true) list.add(path.get(i));
      if(path.get(i).equals(end)) capture=false;
    }
    return list;
  }

  public double shortestPathCost(String start, String end) {
    boolean capture = false;
    double sum = 0;
    for(int i=0;i<path.size();i++) {
      if(capture == true) sum += i;
      if(path.get(i).equals(start)) capture=true;
      else if(path.get(i).equals(end)) capture=false;
    }
    return sum;
  }

}
