# mycat_server
javaでtomcatを作ってみる


## ビルド
> ./gradlew build

## ビルド(プロダクションモードでクリーンビルド)
> ./gradlew -Penv=prod clean build 

## 起動
> ./gradlew bootRun

## jarを起動
> java -jar build/libs/mycat_server-0.0.1-SNAPSHOT.jar