export DISPLAY=:1

build:
	javac -cp jaylib-ffm.jar Main.java

run: build
	java -cp jaylib-ffm.jar:. Main
