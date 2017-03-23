#!/bin/bash
# runGameOfLifeSerial.sh
# Muriel Brunet, Isaiah Mann
# CSC 352, Spring 17
# Run for 10 generations
# chmod a+x runGameOfLifeSerial.sh
# ./runGameOfLifeSerial.sh
# ./runGameOfLifeSerial.sh 2>&1 | grep "real" > timingSerialGENNUM.data 
# don't forget to make a data set for 10, 1000, 10000 generations

javac GameOfLifeSerial.java # compile all the files
for j in 1 2 3 4 5 6 7 8 9 10 ; do # number of times to run the program
    echo "...........................RUN" $j
    time java GameOfLifeSerial 10000 # !!!! CHANGE THIS FOR 10, 1000, or 10000 GENERATIONS !!!!
done
