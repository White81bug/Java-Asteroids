export VERSION=5.5.0-2

export TAG_NAME=$(shell git describe --tags --abbrev=0)
# export DEBUG=1

all:
	if [ ! -e build/jaylib-ffm.jar ]; then make setup; fi
	make build/Main.class

build/Main.class: src/* makefile
	javac -g -Xlint -d build -cp build/jaylib-ffm.jar src/*

run: build/Main.class
	java -cp build/jaylib-ffm.jar:build Main

make-tag: all
	echo $(TAG_NAME)
	cp build/* .gitlab
	if [ ! -e .gitlab ]; then mkdir .gitlab; fi
	git add .gitlab
	git branch $(TAG_NAME)
	git commit -m "automatic commit for a tag"
	git push origin $(TAG_NAME)

setup:
	if [ ! -e build/ ]; then mkdir build; fi
	if [ ! -e build/jaylib-ffm-$(VERSION).jar  ]; then cd build; wget https://github.com/electronstudio/jaylib-ffm/releases/download/v$(VERSION)/jaylib-ffm-$(VERSION).jar; fi
	if [ ! -e build/jaylib-ffm.jar ]; then cd build; cp jaylib-ffm-$(VERSION).jar jaylib-ffm.jar; fi

debug:
	jdb -sourcepath src -classpath build/jaylib-ffm.jar:build Main
