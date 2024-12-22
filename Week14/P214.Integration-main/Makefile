runServer:
	javac -cp ../junit5.jar:. *.java
	sudo java WebApp 80
runTests:
	javac -cp ../junit5.jar:. *.java
	java -jar ../junit5.jar --class-path=. --select-class=BackendTests
clean:
	rm *.class
