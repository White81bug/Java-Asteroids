export VERSION=5.5.0-2

build:
	javac -cp jaylib-ffm.jar Main.java

run: build
	java -cp jaylib-ffm.jar:. Main
