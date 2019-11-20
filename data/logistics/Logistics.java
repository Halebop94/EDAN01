import org.jacop.conatraints.*;
import org.jacop.core.*;
import org.jacop.search.*;

public class Logistics{
  Store store;

  int graph_size = 6;
  int start = 1;
  int n_dests = 1;
  int[] dest = {6};
  int n_edges = 7;
  int[] from = {1,1,2,2,3,4,4};
  int[] to = {2,3,3,4,5,5,6};
  int[] cost = {4,2,5,10,3,4,11};

  public static void main(String[] args) {

    Logistics log = new Logistics();
    log.route();
  }


  public void route(){
    store = new Store();

  }
}
