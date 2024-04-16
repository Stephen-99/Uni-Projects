#!/bin/bash

dir=paramSweeps

mkdir $dir
cp simBaseSweep.py $dir
cp shrimp.py $dir
cp sysArgs.py $dir
cp -r pics/ $dir
cd $dir

lowMulti=0
hiMulti=2
stepMulti=0.4
lowShrimp=20
hiShrimp=100
stepShrimp=20
lowXmax=600
hiXmax=1600
stepXmax=200
lowYmax=400
hiYmax=950
stepYmax=110

for ii in `seq $lowMulti $stepMulti $hiMulti`;
do
	for jj in `seq $lowShrimp $stepShrimp $hiShrimp`;
	do
		xDim=$lowXmax
		yDim=$lowYmax
		for kk in {1..6}
		do
			python3 simBaseSweep.py $jj $xDim $yDim $ii 10
			((xDim+=stepXmax))
			((yDim+=stepYmax))
		done
	done
done
