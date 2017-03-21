# Author: Dominique Thiebaut
# Modifications: Isaiah Mann (added escaped quote marks)
# Description: Generates a 128 line dish for Game of Life

minidish = ["\"                                                                                  \"",
            "\"   #                                                                              \"",
            "\" # #                                            ###                               \"",
            "\"  ##                                                                              \"",
            "\"                                                                                  \"",
            "\"                                                      #                           \"",
            "\"                                                    # #                           \"",
            "\"                                                     ##                           \"",
            "\"                                                                                  \"",
            "\"                                                                                  \"" ]

dish = []
while len( dish ) < 128:
    dish = dish + minidish[:]
dish = dish[0:128]

print( "\n".join( [str(k) for k in dish ] ) )
print( len(dish), "lines" )
