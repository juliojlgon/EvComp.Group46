JC = javac
JFLAGS = -g
JAR = jar
JARFLAGS = cmf
JAVA = java
CONTEST = -cp contest.jar

sphere: 
	$(JAVA) -$(JAR) testrun.jar -submission=main.Player46 -evaluation=SphereEvaluation -seed=1

cigar :
	$(JAVA) -$(JAR) testrun.jar -submission=main.Player46 -evaluation=BentCigarFunction -seed=1

kat: 
	$(JAVA) -$(JAR) testrun.jar -submission=main.Player46 -evaluation=KatsuuraEvaluation -seed=1

schaffers:
	$(JAVA) -$(JAR) testrun.jar -submission=main.Player46 -evaluation=SchaffersEvaluation -seed=1

tests: sphere cigar schaffers kat
