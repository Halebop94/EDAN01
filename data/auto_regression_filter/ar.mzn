include "globals.mzn";
include "jacop.mzn";
include "cumulative.mzn";

int : number_add;
int : number_mul;
int : del_add;
int : del_mul;
int : n;
set of int : last;
set of int : add;
set of int : mul;
array[1..n] of set of int : dependencies;
int : maxTime = 100;
var 0..maxTime : end;

array[1..n] of var 1..100 : starttime;
array[1..n] of var 1..2: duration;



constraint forall(index in 1..n)(
      forall(dep in dependencies[index])(
          starttime[index] + duration[index] <= starttime[dep]
    )
);
constraint forall(adder in add)(
  duration[adder] = del_add
);
constraint forall(muller in mul)(
  duration[muller] = del_mul
);

array[1..card(add)] of var 0..100: add_start;

constraint
	add_start = [starttime[i] | i in 1..n where i in add];

array[1..card(mul)] of var 0..100: mul_start;
constraint
	mul_start = [starttime[i] | i in 1..n where i in mul];

array[1..card(add)] of var del_add..del_add: d_add;
array[1..card(mul)] of var del_mul..del_mul: d_mul;

array[1..card(add)] of var 1..number_add: r_add;
array[1..card(mul)] of var 1..number_mul: r_mul;

array[1..card(add)] of var 1..1: r2_add;
array[1..card(mul)] of var 1..1: r2_mul;

array[1..n] of var int: r = r_add ++ r_mul;

constraint cumulative(add_start, d_add, r2_add, number_add);

constraint diff2(mul_start, r_mul, d_mul, [1 | i in 1..card(mul)]);
constraint diff2(add_start, r_add, d_add, [1 | i in 1..card(add)]);


constraint cumulative(mul_start, d_mul, r2_mul, number_mul);

var 0..100: cost;

constraint
	cost = max([starttime[l] + duration[l] | l in last]);

solve :: seq_search ([
        int_search(starttime, smallest_max, indomain_min, complete),
        int_search(r, input_order, indomain_min, complete)])
    minimize cost;
output[show(cost)];
output[show(r)];
output[show(add_start)];
output[show(mul_start)];
