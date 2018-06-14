# The MooIng Compiler

## Semester Project 
### CPSC 827 Spring 2013**John E. Ingram**  
**W. Clay Moody**  
Clemson University  
Clemson, SC


### Introduction

This compiler is the final semester project of John Ingram <jei@clemson.edu> and W. Clay Moody <wcm@clemson.edu> for CPSC 827 in the Spring of 2013. This class was taught by Dr. Harold Grossman. The compiler is for an education fictional language developed by Dr. Grossman.

The MooIng compiler was written in Java in a package called mooingCompiler. The main class was mooIng. The compiler was packaged as a Java jar file and takes one command line argument of the source file to be compiled. The output of the compiler is composed of two output streams. The first is to standard output and includes the user feedback as determined by the flags of the grammar. By default flag `1 (print source code)` is always enabled. The second output is an assembly code file named `mooing.s` written in the GAS syntax. This file by default has the four-tuple intermediate codes as comments in the source code. 

A bash script `moo.sh` was crafted to take as a single input the name of the source file. This script will run the `jar` file to create the assembly code, assembly the code, link the code and run the program. This is all done with `gcc` using the 32-bit mode `-m32` flag.  Also, the script will print the MooIng compiler banner indicating success and create the output files for the assignment. The output files consist of the source code, the source code with four-tuples, the assembly code with four-tuple comments and the output of the program. The output file saved in the same location as the source and given the same name as the source with a `.out` extension.

### Installation

```
git clone https://github.com/wcmoody/mooIngCompiler.git
ant
```

Expected output is:

```
Buildfile: ./build.xml

clean:
   [delete] Deleting directory ./build

compile:
    [mkdir] Created dir: ./build/classes
    [javac] ./build.xml:18: warning: 'includeantruntime' was not set, defaulting to build.sysclasspath=last; set to false for repeatable builds
    [javac] Compiling 14 source files to ./build/classes
    [javac] Note: ./src/mooingCompiler/GrammarAnalyzer.java uses or overrides a deprecated API.
    [javac] Note: Recompile with -Xlint:deprecation for details.

jar:
    [mkdir] Created dir: ./build/jar
      [jar] Building jar: ./build/jar/mooIng.jar

run:
     [java] usage: MooIng <source-code>

main:

BUILD SUCCESSFUL
Total time: 2 seconds
```



### Executing

The included `moo.sh` file will create the assembly code based on the specification of the language, assembly and link the code, and run the program. 

```
./moo.sh examples/BubbleSort.txt
```

This will run the below bash script


```bash
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
java -jar mooIng.jar $1 >> $1.out 
echo "##### CREATED ASSEMBLY CODE WITH FOURTUPLE COMMENTS #####" >> $1.out 
cat mooIng.s >> $1.out 
gcc -m32 -g -c mooIng.s -o mooIng.o 
gcc -m32 -g mooIng.o -o mooIng 
echo "##### OUTPUT OF DEMONSTRATION PROGRAM #####" >> $1.out 
./mooIng >> $1.out 
cat cow.txt 
./mooIng
```
