#### сборка проекта

требуются
```
javac 1.8.0_201
openjdk version "1.8.0_201"
Apache Maven 3.5
```

`$ mvn package`

```
... 
[INFO] Building jar: ~/DEV/fs/FS6926/target/mergesorting-3-jar-with-dependencies.jar
[INFO] ------------------------------------------------------------------------
[INFO] BUILD SUCCESS
...
```

`target/mergesorting-3-jar-with-dependencies.jar` используется в запускающем скрипте `sort.sh`

#### параметры запуска

```
$ ./sort.sh 

usage: java -jar mergesorting-3-jar-with-dependencies.jar [OPTIONS] output.file input.files...
output.file  Required file name with the sorting result.
input.files  One or more input files.
 -a          Sort in ascending order. It is applied by default 
             if -a or -d is missing.
 -d          Sort in descending order. The option is optional, as is -a.
 -i          The files contain integers. Mandatory, mutually exclusive with -s.
 -s          The files contain strings. Mandatory, mutually exclusive with -i.
```