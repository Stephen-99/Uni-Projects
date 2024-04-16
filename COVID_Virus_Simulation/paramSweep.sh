#!/bin/bash

#adapted from my submission for the FOP assignment last semester

dir=paramSweeps

mkdir $dir
cp *.py $dir
cp RandNames.csv $dir
cd $dir


#1000 causes seggfault trying to pickle the graph
numPpl=750     

lowInf=5         #0.005
highInf=320    
multiInf=4

lowTrans=0.1
highTrans=1 
stepTrans=0.25

lowRecov=0.1
highRecov=1  
stepRecov=0.25

lowDeath=0.1
highDeath=1   
stepDeath=0.25

#carrier rate
lowCar=0.1
highCar=1  
stepCar=0.25

#chance of a carrier becoming infected, 50% chance for a fortnight
    #I didn't wanna make this only 1 set val, but i also can't have 4^7 iterations, 
        #but i did have 5 * 4^5...............
car_inf=0.5

lowGovPrec=0
highGovPrec=2
stepGovPrec=0.5



for ((inf=$lowInf; inf<=$highInf; inf*=$multiInf))
do
    i=$(bc <<<"scale=3;$inf/1000")
    i="0${i}"
    #creating a file to re-use for succssive iterations, this significantly speeds up the process
    python3 CreateGraphFile.py tempGraph $numPpl $i

    #using seq even tho outdated, cause it supports decimals
    for trans in `seq $lowTrans $stepTrans $highTrans`;
    do
        for recov in `seq $lowRecov $stepRecov $highRecov`;
        do
            for death in `seq $lowDeath $stepDeath $highDeath`;
            do
                for car in `seq $lowCar $stepCar $highCar`;
                do
                    for govPrec in `seq $lowGovPrec $stepGovPrec $highGovPrec`;
                    do
                        python3 HealthSim.py -s tempGraph $trans $recov $death $car $car_inf $govPrec
                    done
                done
            done
        done
    done
done
