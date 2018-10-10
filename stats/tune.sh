#!/bin/bash
#set -x

echo "Tournament Size (min.)"
read TSmin
echo "Tournament Size (max.) (step = 1)"
read TSmax

for ts in `seq $TSmin $TSmax`;
do
	for runs in `seq 1 10`;
	do
		java -Djava.library.path=/root/UvA/EC/EvComp.Group46/files -Dmc=0.5 -Dcc=0.7 -Dts=$ts -Dlr=0.5 -Dfile.encoding=UTF-8 -jar /root/UvA/EC/EvComp.Group46/files/testrun.jar -submission=main.Player46 -evaluation=BentCigarFunction -nosec -seed=2  | head -n 1 | sed 's/[^0-9.]*//g'
	done
done
