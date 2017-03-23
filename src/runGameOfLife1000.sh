#!/bin/bash
# runGameOfLife1000.sh
# Muriel Brunet, Isaiah Mann
# CSC 352, Spring 17
# Run for 1000 generations
# chmod a+x runGameOfLife1000.sh
# ./runGameOfLife1000.sh
# ./runGameOfLife1000.sh 2>&1 | grep "with\|real" > timing1000gen.data 

javac *.java # compile all the files
for i in 2 4 8 12 16 20 32 64 ; do # number of threads
    printf "\n"
    printf "\n"
    echo "..................................... with" $i "threads"
    for j in 1 2 3 4 5 6 7 8 9 10 ; do # number of times to run the program
	echo "RUN" $j
	time java GameOfLifeApplication "dish.txt" $i 1000 false
    done
done