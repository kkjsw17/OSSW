# OpenSW
Konkuk Univ. 2022-1 INTRODUCTION TO OPEN SOURCE SOFTWARE practices

## Compile
```
javac -cp jars/jsoup-1.14.3.jar:jars/kkma-2.1.jar:jars/json-simple-1.1.1.jar src/scripts/*.java -d bin -encoding UTF8
```
## Run
#### 1. makeCollection (2nd week)
```
java -cp jars/jsoup-1.14.3.jar:jars/kkma-2.1.jar:bin scripts.main -c ./data
```
#### 2. makeKeyword (3rd week)
```
java -cp jars/kkma-2.1.jar:bin scripts.main -k ./collection.xml
```
#### 3. indexer (4th week)
```
java -cp jars/kkma-2.1.jar:bin scripts.main -i ./index.xml
```
#### 4. searcher (5th week)
```
java -cp jars/kkma-2.1.jar:bin scripts.main -s ./index.post -q "크림 파스타는 분말 가루를 뭉쳐 향신료가 들어간 크림을 뿌린다."
```
#### 5. moveiSearcher (15th week)
```
java -cp jars/json-simple-1.1.1.jar:bin scripts.main -ms 
```