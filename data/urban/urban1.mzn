include "globals.mzn";
int:n;
int:n_commercial;
int:n_residential;
array[0..n] of int :point_distribution;
var -100..100: cost;
array[1..n, 1..n] of var 0..1: grid;

array[1..n] of var -n..n: rows;
array[1..n] of var -n..n: cols;

constraint sum(i in 1..n)(rows[i]) == n_residential;

constraint sum(i in 1..n)(cols[i]) == n_residential;

constraint
  forall(i in 1..n)(
    rows[i] = sum([grid[i,j] | j in 1..n]) /\
    cols[i] = sum([grid[j,i] | j in 1..n])
);

constraint
  cost = sum([point_distribution[rows[i]] | i in 1..n]) + sum([point_distribution[cols[i]] | i in 1..n]);
  
constraint
  lex2(grid);
  
solve maximize cost;

output[show(cost)];