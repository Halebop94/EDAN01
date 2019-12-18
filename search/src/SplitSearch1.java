import org.jacop.constraints.Not;
import org.jacop.constraints.PrimitiveConstraint;
import org.jacop.constraints.XlteqC;
import org.jacop.core.FailException;
import org.jacop.core.IntDomain;
import org.jacop.core.IntVar;
import org.jacop.core.Store;

/**
 * Implements Simple Depth First Search .
 *
 * @author Krzysztof Kuchcinski
 * @version 4.1
 */

public class SplitSearch1  {

    boolean trace = false;
    int count = 0;
    int wrong = 0;

    /**
     * Store used in search
     */
    Store store;

    /**
     * Defines varibales to be printed when solution is found
     */
    IntVar[] variablesToReport;

    /**
     * It represents current depth of store used in search.
     */
    int depth = 0;

    /**
     * It represents the cost value of currently best solution for FloatVar cost.
     */
    public int costValue = IntDomain.MaxInt;

    /**
     * It represents the cost variable.
     */
    public IntVar costVariable = null;

    public SplitSearch1(Store s) {
        store = s;
    }


    /**
     * This function is called recursively to assign variables one by one.
     */
    public boolean label(IntVar[] vars) {


        //System.out.println("ct " + vars.length);
        count++;
        if (trace) {
            for (int i = 0; i < vars.length; i++)
                System.out.print (vars[i] + " ");
            System.out.println ();
        }

        ChoicePoint choice = null;
        boolean consistent;

        // Instead of imposing constraint just restrict bounds
        // -1 since costValue is the cost of last solution
        if (costVariable != null) {
            try {
                if (costVariable.min() <= costValue - 1)
                    costVariable.domain.in(store.level, costVariable, costVariable.min(), costValue - 1);
                else
                    return false;
            } catch (FailException f) {
                return false;
            }
        }

        consistent = store.consistency();

        if (!consistent) {
            // Failed leaf of the search tree
            return false;
        } else { // consistent

            if (vars.length == 0) {
                // solution found; no more variables to label

                // update cost if minimization
                if (costVariable != null)
                    costValue = costVariable.min();

                reportSolution();

                return costVariable == null; // true is satisfiability search and false if minimization
            }

            choice = new ChoicePoint(vars);

            levelUp();

            store.impose(choice.getConstraint());

            // choice point imposed.

            consistent = label(choice.getSearchVariables());

            if (consistent) {
                levelDown();
                return true;
            } else {

                restoreLevel();
                store.impose(new Not(choice.getConstraint()));



                // negated choice point imposed.

                consistent = label(vars);

                levelDown();

                if (consistent) {
                    return true;
                } else {
                    return false;
                }
            }
        }
    }

    void levelDown() {
        store.removeLevel(depth);
        store.setLevel(--depth);
    }

    void levelUp() {
        store.setLevel(++depth);
    }

    void restoreLevel() {
        store.removeLevel(depth);
        store.setLevel(store.level);
        wrong++;

    }

    public void reportSolution() {

        if (costVariable != null)
            System.out.println ("Cost is " + costVariable);
        System.out.println ("number of nodes: " + count);

        System.out.println(wrong + " wrong dec");

        for (int i = 0; i < variablesToReport.length; i++)
            System.out.print (variablesToReport[i] + " ");
        System.out.println ("\n---------------");
    }

    public void setVariablesToReport(IntVar[] v) {
        variablesToReport = v;
    }

    public void setCostVariable(IntVar v) {
        costVariable = v;
    }

    public class ChoicePoint {

        IntVar var;
        IntVar[] searchVariables;
        int value;

        public ChoicePoint (IntVar[] v) {
            var = selectVariable(v);
            value = selectValue(var);
        }

        public IntVar[] getSearchVariables() {
            return searchVariables;
        }

        /**
         * example variable selection; input order
         */
        IntVar selectVariable(IntVar[] v) {

            if (v.length != 0) {
                int index = 0;
                int smallestDomain = Integer.MAX_VALUE;
                for(int i = 0; i< v.length-1; i++){
                    if(v[i].domain.getSize()<smallestDomain){
                        index = i;
                        smallestDomain = v[i].domain.getSize();
                    }

                }
                if(v[index].min() == v[index].max()){

                    searchVariables = new IntVar[v.length-1];
                    for (int i = 0; i < v.length-1; i++) {
                        if(i<index){
                            searchVariables[i] = v[i];
                        }
                        else searchVariables[i] = v[i+1];

                    }
                }
                else{
                    searchVariables = new IntVar[v.length];
                    for(int i = 0; i < v.length; i++){
                        searchVariables[i] = v[i];
                    }
                }
                return v[index];

            }
            else {
                System.err.println("Zero length list of variables for labeling");
                return new IntVar(store);
            }
        }

        /**
         * example value selection; indomain_min
         */
        int selectValue(IntVar v) {
            int c = (v.max() + v.min())/2;
            return c;
        }

        /**
         * example constraint assigning a selected value
         */
        public PrimitiveConstraint getConstraint() {
            return new XlteqC(var, value);
        }
    }
}
