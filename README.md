# OSSW
2022-2 Konkuk Univ. INTRODUCTION TO OPEN SOURCE SOFTWARE

## Compile
```
javac -cp jars/jsoup-1.14.3.jar:jars/kkma-2.1.jar src/scripts/*.java -d bin -encoding UTF8
```
## Run
#### 1. makeCollection (2nd week)
```
java -cp jars/jsoup-1.14.3.jar:jars/kkma-2.1.jar:bin scripts.main -c src/data/html
```
#### 2. makeKeyword (3rd week)
```
java -cp jars/jsoup-1.14.3.jar:jars/kkma-2.1.jar:bin scripts.main -k src/data/collection.xml
```
