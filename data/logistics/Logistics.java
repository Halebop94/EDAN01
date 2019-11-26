import org.jacop.constraints.*;
import org.jacop.core.*;
import org.jacop.search.*;
import org.jacop.constraints.Constraint;
import org.jacop.constraints.netflow.*;
import org.jacop.constraints.netflow.simplex.*;
import java.util.Arrays;

public class Logistics{
  int graph_size = 6;
  int start = 1;
  int n_dests = 1;
  int[] dest = {6};
  int n_edges = 7;
  int[] from = {1,1,2,2,3,4,4};
  int[] to = {2,3,3,4,5,5,6};
  int[] cost = {4,2,5,10,3,4,11};


  public static void main(String[] args) {

    Logistics l = new Logistics();
  l.route();
  }


  public void route(){
    Store store = new Store();

    IntVar[] x = new IntVar[n_edges+2];

    NetworkBuilder net = new NetworkBuilder();
    Node source = net.addNode("source", n_dests); //Node 1
    Node sink = net.addNode("sink", -1); //Node 6

    Node[] nodes = new Node[graph_size];
    for(int i = 0; i<graph_size; i++){
        nodes[i] = net.addNode(Integer.toString(i), 0);
    }

    x[0] = new IntVar(store, "source ->" + Integer.toString(start), 0, n_dests);
    net.addArc(source, nodes[start-1], 0, x[0]);


    for(int i = 0; i<n_edges;i++) {
      System.out.println(from[i]);
      System.out.println(to[i]);
      x[i+1] = new IntVar(store, Integer.toString(from[i]) + " -> " + Integer.toString(to[i]), 0, n_dests);
      net.addArc(nodes[from[i]-1], nodes[to[i]-1], cost[i], x[i+1]);
    }

      x[n_edges+1] = new IntVar(store, Integer.toString(dest[0]) + " -> sink", 0, n_dests);
      net.addArc(nodes[dest[0]-1], sink, 0, 1);



    IntVar cost = new IntVar(store, "cost", 0, 26);
    net.setCostVariable(cost);

    store.impose(new NetworkFlow(net));

    Arc[] arcs = new Arc[1];
      arcs[0] = net.addArc(source, nodes[0], 0, x[0]);

      IntVar s = new IntVar(store, "s", 0, 1);
      Domain[] domCond = new IntDomain[1];
      domCond[0] = new IntervalDomain(0, 0);

      net.handlerList.add(new DomainStructure(s,
                          Arrays.asList(domCond),
                          Arrays.asList(arcs)));


   Search<IntVar> label = new DepthFirstSearch<IntVar>();
      SelectChoicePoint<IntVar> select = new SimpleSelect<IntVar>(x,
                                          null,
                                          new IndomainMin<IntVar>());


      boolean result = label.labeling(store, select, cost);


    System.out.println(result);

  }

}
