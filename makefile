export VERSION=5.5.0-2

build/Main.class: src/Main.java makefile
	javac -Xlint -d build -cp build/jaylib-ffm.jar src/Main.java

run: build
	java -cp jaylib-ffm.jar:. Main
