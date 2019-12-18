include "globals.mzn";
int:n;
int:n_commercial;
int:n_residential;
array[0..n] of int :point_distribution;
var -400..400: cost;
array[1..n, 1..n] of var 0..1: grid;


array[1..n] of var 0..n : rows = sum(i, j in 1..n)(
  1
);

cost = sum(i,j in 1..n)(
                    grid[i,j] +
                   point_distribution[i] +
                   point_distribution[j]
        );

constraint sum(i,j in 1..n)(grid[i, j]) == n_residential;
