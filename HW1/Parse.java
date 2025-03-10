package HW1;


/*

https://www.cs.colostate.edu/~pouchet/classes/CS453/hw1/hw1.html

Final Grammar
goal    ::=     <expr>
expr 	::= 	<Term><ex1>
ex1     ::=     <binop><Term><ex1> | e
Term    ::=     <incrop><expr> | < D >
D       ::=     <Value> <E'>
E'      ::=     <incrop><E'>  | e
Value   ::=     <num> | (expr<Close> | lvalue
lvalue 	::= 	$expr
incrop 	::= 	++ | --
binop 	::= 	+ | - |
num 	::= 	0 | 1 | 2 | 3 | 4 | 5 | 6 | 7 | 8 | 9
Close   ::=     )
*/


//initialize nullable, first, follow arrays for all x (Parse table)

//For each terminal symbol z
    //Make: First[Z] = {Z}
//do
    //for each production ()
        // if nullable set case nullable
        //for 
//while nullable, first, follow are updated