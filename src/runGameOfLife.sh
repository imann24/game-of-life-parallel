#!/bin/bash
# runGameOfLife.sh
# Muriel Brunet, Isaiah Mann
# CSC 352, Spring 17

javac *.java # compile all the files
for j in 10 1000 10000 ; do #number of generations
    for i in 2 4 8 12 16 20 32 64 ; do #number of threadsx
        time java GameOfLifeApplication "dish.txt" $i $j false
    done
done
