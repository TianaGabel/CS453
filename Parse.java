

/*

https://www.cs.colostate.edu/~pouchet/classes/CS453/hw1/hw1.html

start ::= ex1 (binop) start | ex1
ex1   ::= ++ex2| ex2
ex2   ::= ex3++ | ex3
ex3   ::= ()|$ex4|num


goal    ::=     <expr>
expr 	::= 	<Term><ex1>
ex1     ::=     (binop)<expr> | e
Term    ::=     <factor>< Term2 > | (incrop) < expr >
Term2   ::=     (incrop) < Term2 > | e
factor  ::=     <lvalue> | (expr) | num
lvalue 	::= 	$expr
incrop 	::= 	++ | --
binop 	::= 	+ | - |
num 	::= 	0 | 1 | 2 | 3 | 4 | 5 | 6 | 7 | 8 | 9


*/


//initialize nullable, first, follow arrays for all x (Parse table)

//For each terminal symbol z
    //Make: First[Z] = {Z}
//do
    //for each production ()
        // if nullable set case nullable
        //for 
//while nullable, first, follow are updated