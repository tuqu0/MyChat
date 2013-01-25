CC=javac
FILES= ./src/ServeurChat.java ./src/ServiceChat.java
DIR=MyChat
DIST= puydoyeux_vincent-MyChat_v0

CalcRPL:
	$(CC) $(FILES) -d ./bin
clean:
	rm -rf ./src/*.class
	rm -f ./bin/*.class	
dist: clean
	rm -rf ../$(DIST) 2>/dev/null
	rm -rf ../$(DIST).tar.gz 2>/dev/null
	mkdir ../$(DIST)
	cp -r ../$(DIR)/*  ../$(DIST)
	zip -9 -r ../$(DIST).zip ../$(DIST) 
	rm -rf ../$(DIST)
	md5sum ../$(DIST).zip

