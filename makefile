export VERSION=5.5.0-2
export TARGETS+= build/Main.class

all:
	make $(TARGETS)

build/Main.class: src/Main.java makefile
	javac -Xlint -d build -cp build/jaylib-ffm.jar src/Main.java

run: build/Main.class
	java -cp build/jaylib-ffm.jar:build Main

setup:
	if [ ! -e build/ ]; then mkdir build; fi
	if [ ! -e build/jaylib-ffm-$(VERSION).jar  ]; then cd build; wget https://github.com/electronstudio/jaylib-ffm/releases/download/v$(VERSION)/jaylib-ffm-$(VERSION).jar; fi
	if [ ! -e build/jaylib-ffm.jar ]; then cd build; ln -s jaylib-ffm-$(VERSION).jar jaylib-ffm.jar; fi
