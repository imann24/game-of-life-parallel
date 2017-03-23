# processTimingData.py
# D. Thiebaut
# edited by Muriel Brunet, Isaiah Mann
# CSC 352, Spring 17

from __future__ import print_function

def processData(filename):

    file = open( filename, "r" ) # computing for 10 generation data
    lines = file.readlines()
    file.close()
    
    # create array of time averages
    times = [0]*100 # 0-10, hence 11
    
    # parse lines of text
    for line in lines:
        line.split("\n")
        if len(line) < 2: # as in its the line holding the time
            continue
        if "threads" in line:
            n = int(line.split()[-2])
        else:
            time = line.replace( 'm', ' ' ).replace( 's', '' ).split()[-1]
            time = float( time )
            times[n] += time
            
    # compute averages and print them
    for i in range( len( times ) ):
        if times[i] != 0:
            print( i, "threads:", times[i]/10.0 ) 


def main():
    print("For 10 generations...")
    processData("timing10gen.data")
    
    print()
    print("For 1000 generations...")
    processData("timing1000gen.data")
    
    print()
    print("For 10000 generations...")
    processData("timing10000gen.data")


main()
