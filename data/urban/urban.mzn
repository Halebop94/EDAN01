include "globals.mzn";
int:n;
int:n_commercial;
int:n_residential;
array[0..n] of int :point_distribution;
var -100..100: cost;
array[1..n, 1..n] of var 0..1: grid;

array[1..n] of var -n..n: rows;
array[1..n] of var -n..n: cols;

constraint
  forall(i in 1..n)(
    rows[i] == point_distribution[sum(j in 1..n)(grid[i,j])]
);
 
constraint
  forall(i in 1..n) (
    cols[i] == point_distribution[sum(j in 1..n)(grid[j,i])]
);

constraint
  lex2(grid);

constraint sum(i,j in 1..n)(grid[i, j]) == n_residential;

constraint
  cost == sum(rows) + sum(cols);
  
solve maximize cost;

output[show(cost)];