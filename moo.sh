#!/bin/bash

EXPECTED_ARGS=1
E_BADARGS=65

if [ $# -ne $EXPECTED_ARGS ]
then
  echo "Usage: `basename $0` {source file path}"
  exit $E_BADARGS
fi


echo "##### SOURCE CODE #####" > $1.out
cat $1 >> $1.out
echo "###### SOURCE CODE WITH FOURTUPLES #####" >> $1.out
java -jar build/jar/mooIng.jar $1 >> $1.out
echo "##### CREATED ASSEMBLY CODE WITH FOURTUPLE COMMENTS #####" >> $1.out
cat mooIng.s >> $1.out
gcc -m32 -g -c mooIng.s -o mooIng.o
gcc -m32 -g mooIng.o -o mooIng
echo "##### OUTPUT OF DEMONSTRATION PROGRAM #####" >> $1.out
./mooIng >> $1.out
cat cow.txt
./mooIng
