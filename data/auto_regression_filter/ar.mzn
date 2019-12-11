include "globals.mzn";
include "jacop.mzn";

int : number_add;
int : number_mul;
int : del_add;
int : del_mul;
int : n;
set of int : last;
set of int : add;
set of int : mul;
array[1..n] of set of int : dependencies;
int : maxTime = 1000;
var 0..maxTime : end;

array[1..n] of var 1..100 : starttime;
array[1..n] of var 1..2: duration;


 
constraint forall(index in 1..n)(
      forall(dep in dependencies[index])(
          starttime[index] + duration[index] =< starttime[dep]
    )
);
constraint forall(adder in add)(
  duration[adder] = del_add
);
constraint forall(muller in mul)(
  duration[muller] = mul_add
);

array[1..card(add)] of var 0..100: add_start;

constraint
	add_start = [start_time[i] | i in 1..n where i in add];

array[1..card(mul)] of var 0..100: mul_start;
constraint
	mul_start = [start_time[i] | i in 1..n where i in mul];

array[1..card(add)] of var del_add..del_add: d_add;
array[1..card(mul)] of var del_mul..del_mul: d_mul;

array[1..card(add)] of var 1..number_add: r_add;
array[1..card(mul)] of var 1..number_mul: r_mul;

array[1..n] of var int: r = r_add ++ r_mul;

constraint
	cumulative(add_start, d_add, r_add, number_add);


constraint
	cumulative(mul_start, d_mul, r_mul, number_mul);

var 0..100: cost;

constraint
	cost = max([start_time[l] + duration[l] | l in last]);

solve :: seq_search ([
        int_search(start_time, smallest_max, indomain_min, complete),
        int_search(r, input_order, indomain_min, complete)])
    minimize cost;
output[show(cost)];




