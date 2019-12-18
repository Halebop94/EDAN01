include "globals.mzn";
int:n;
int:n_commercial;
int:n_residential;
array[0..n] of int :point_distribution;

int: n_resi;
var 0..100: cost;
array[1..n, 1..n] of var 0..1: grid;
cost = sum(i in 1..n)(forall(index in 1..n)(
        
            grid[index,i]
         
          )
);

