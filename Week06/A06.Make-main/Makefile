run: App.class
	java App
Mercury:
	@echo "Hello Mercury"
App.class: App.java
	javac App.java
test: TestApp.class
	java -cp ../junit5.jar:. org.junit.platform.console.ConsoleLauncher \
	 --class-path . --scan-class-path
TestApp.class: App.class TestApp.java
	javac -cp ../junit5.jar:. TestApp.java
