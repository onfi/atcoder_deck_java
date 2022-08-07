sed $1/Main.java -z -e "s/.*\/\/common/\/\/common/" > tmp/Common
find . -name 'Main.java' | xargs -I FILE sh -c "sed FILE -i -z -e 's/\n\/\/common.*/\n/' && cat tmp/Common >> FILE"