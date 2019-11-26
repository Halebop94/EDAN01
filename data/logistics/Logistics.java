import javax.swing.SpringLayout.Constraints;

import org.jacop.constraints.*;
import org.jacop.core.*;
import org.jacop.search.*;
import org.jacop.constraints.netflow.*;
import org.jacop.constraints.netflow.simplex.*;

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

  route();
  }


  public static void route(){
    Store store = new Store();

    IntVar[] x = new IntVar[8];

    NetworkBuilder net = new NetworkBuilder();
    Node source = net.addNode("source", 5); //Node 1
    Node sink = net.addNode("sink", -5); //Node 6

    Node A = net.addNode("A", 0);
    Node B = net.addNode("B", 0);
    Node C = net.addNode("C", 0);
    Node D = net.addNode("D", 0);

    x[0] = new IntVar(store, "x_0", 0, 5);
    x[1] = new IntVar(store, "x_1", 0, 5);
    net.addArc(source, A, 0, x[0]);
    net.addArc(source, C, 0, x[1]);

    x[2] = new IntVar(store, "a->b", 0, 5);
    x[3] = new IntVar(store, "a->d", 0, 5);
    x[4] = new IntVar(store, "c->b", 0, 5);
    x[5] = new IntVar(store, "c->d", 0, 5);
    net.addArc(A, B, 3, x[2]);
    net.addArc(A, D, 2, x[3]);
    net.addArc(C, B, 5, x[4]);
    net.addArc(C, D, 6, x[5]);

    x[6] = new IntVar(store, "x_6", 0, 5);
    x[7] = new IntVar(store, "x_7", 0, 5);
    net.addArc(B, sink, 0, x[6]);
    net.addArc(D, sink, 0, x[7]);

    IntVar cost = new IntVar(store, "cost", 0, 1000);
    net.setCostVariable(cost);

    store.impose(new NetworkFlow(net));
  }
}
