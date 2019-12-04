import org.jacop.constraints.*;
import org.jacop.core.*;
import org.jacop.search.*;
import org.jacop.constraints.Constraint;
import org.jacop.constraints.netflow.*;
import org.jacop.constraints.netflow.simplex.*;
import java.util.Arrays;

public class Logistics{



  public static void main(String[] args) {

    Logistics l = new Logistics();
    int graph_size = 6;
    int start = 1;
    int n_dests = 1;
    int[] dest = {6};
    int n_edges = 7;
    int[] from = {1,1,2,2,3,4,4};
    int[] to = {2,3,3,4,5,5,6};
    int[] cost = {4,2,5,10,3,4,11};
    //l.route(graph_size,start,n_dests,dest,n_edges,from,to,cost);

    int graph_size2 = 6;
    int start2 = 1;
    int n_dests2 = 2;
    int[] dest2 = {5,6};
    int n_edges2 = 7;
    int[] from2 = {1,1,2, 2,3,4, 4};
    int[] to2 = {2,3,3, 4,5,5, 6};
    int[] cost2 = {4,2,5,10,3,4,11};

    l.route(graph_size2,start2,n_dests2,dest2,n_edges2,from2,to2,cost2);

    int graph_size3 = 6;
    int start3 = 1;
    int n_dests3 = 2;
    int[] dest3 = {5,6};
    int n_edges3 = 9;
    int[] from3 = {1,1,1,2,2,3,3,3,4};
    int[] to3 = {2,3,4,3,5,4,5,6,6};
    int[] cost3 = {6,1,5,5,3,5,6,4,2};


  }


  public void route(int graph_size,
  int start,
  int n_dests,
  int[] dest,
  int n_edges,
  int[] from,
  int[] to,
  int[] cost){
    Store store = new Store();

    IntVar[] x = new IntVar[n_edges*2+1+n_dests];
    IntVar[] ue = new IntVar[n_edges];

    NetworkBuilder net = new NetworkBuilder();
    Node source = net.addNode("source", n_dests); //Node 1
    Node sink = net.addNode("sink", -n_dests); //Node 6


    Node[] nodes = new Node[graph_size];
    for(int i = 0; i<graph_size; i++){
        nodes[i] = net.addNode(Integer.toString(i), 0);
    }

    x[0] = new IntVar(store, "source ->" + Integer.toString(start), 0, n_dests);
    net.addArc(source, nodes[start-1], 0, x[0]);


    for(int i = 0; i<n_edges;i++) {
      x[i+1] = new IntVar(store, Integer.toString(from[i]) + " -> " + Integer.toString(to[i]), 0, n_dests);
      x[i+n_edges+1] = new IntVar(store, Integer.toString(to[i]) + " -> " + Integer.toString(from[i]), 0, n_dests);

      net.addArc(nodes[from[i]-1], nodes[to[i]-1], cost[i], x[i+1]);
      net.addArc(nodes[to[i]-1], nodes[from[i]-1], cost[i], x[i+n_edges+1]);
      store.impose(new Reified(new XgtC(x[i+1], 0), ue[i]));


    }

      for(int i = 0; i<n_dests; i++){
        x[n_edges*2+1+i] = new IntVar(store, Integer.toString(dest[i]) + " -> sink", 0, 1);
        net.addArc(nodes[dest[i]-1], sink, 0, 1);
      }




    //IntVar cost1 = new IntVar(store, "cost", 0, 1000);
    IntVar cost1 = new IntVar(store, "cost", 0, 1000);


    net.setCostVariable(cost1);

   Search<IntVar> label = new DepthFirstSearch<IntVar>();

      SelectChoicePoint<IntVar> select = new SimpleSelect<IntVar>(x,
                                          null,
                                          new IndomainMin<IntVar>());



      boolean result = label.labeling(store, select, cost1);


    System.out.println(result);

  }

}
