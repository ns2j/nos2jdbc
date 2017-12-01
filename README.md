# nos2jdbc
## No Seasar2 Container's S2JDBC
S2JDBCからSeasar2 Containerへの依存をとりのぞいたものです。  
Maven Projectです。  
[チュートリアルを用意しました。(nos2jdbc-tutorial)](https://github.com/ns2j/nos2jdbc-tutorial)   
## Mavenのローカルリポジトリへインストール
`mvn install`   
として下さい。
##追加した機能
jsonでループしないように、getSingleResultWithoutInverseField()とgetResultListWithoutInverseField()を加えています。  
sqlのselectをエンティティにマッピングできるようにしました。
