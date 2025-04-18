export VERSION=5.5.0-2

all:
	make build/Main.class

build/Main.class: src/* makefile
	javac -g -Xlint -d build -cp build/jaylib-ffm.jar src/*

run: build/Main.class
	java -cp build/jaylib-ffm.jar:build Main

setup:
	if [ ! -e build/ ]; then mkdir build; fi
	if [ ! -e build/jaylib-ffm-$(VERSION).jar  ]; then cd build; wget https://github.com/electronstudio/jaylib-ffm/releases/download/v$(VERSION)/jaylib-ffm-$(VERSION).jar; fi
	if [ ! -e build/jaylib-ffm.jar ]; then cd build; ln -s jaylib-ffm-$(VERSION).jar jaylib-ffm.jar; fi

debug:
	jdb -sourcepath src -classpath build/jaylib-ffm.jar:build Main
