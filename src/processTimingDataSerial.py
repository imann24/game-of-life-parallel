# processTimingDataSerial.py
# D. Thiebaut
# edited by Muriel Brunet, Isaiah Mann
# CSC 352, Spring 17

from __future__ import print_function

def processData(filename):

    file = open( filename, "r" ) # computing for 10 generation data
    lines = file.readlines()
    file.close()
    
    # initializing avergae time 
    time = 0

    # parse lines of text
    for line in lines:
        line.split("\n")
        if len(line) < 2: # as in its the line holding the time
            continue
        else:
            newTime = line.replace( 'm', ' ' ).replace( 's', '' ).split()[-1]
            newTime = float( newTime )
            time += newTime
            
    # compute averages and print them
    if time != 0:
        print( "Average time:", time/10.0 ) 


def main():
    print("FOR SERIAL PROGRAM")
    print("For 10 generations...")
    processData("timingSerial10.data")
    print()

    print("For 1000 generations...")
    processData("timingSerial1000.data")
    print()

    print("For 10000 generations...")
    processData("timingSerial10000.data")
    
main()
