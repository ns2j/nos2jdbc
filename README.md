# nos2jdbc
## No Seasar2 Container's S2JDBC

S2JDBCからSeasar2 Containerへの依存をとりのぞいたものです。  
Maven Projectです。  
Maven Central Repositoryにも登録しました。  
[チュートリアルを用意しました。(nos2jdbc-tutorial)](https://github.com/ns2j/nos2jdbc-tutorial)   

## 追加した機能
### jsonのループ回避
 jsonでループしないように、getSingleResultWithoutInverseField()とgetResultListWithoutInverseField()を加えました。  
### sqlのselectをエンティティにマッピング
 SqlSelectでエンティティへマッピングできるようにしました。
* エンティティに@NonAutoをつけ、nos2jdbc-genの管轄外のパッケージに置くか、@DisableGenアノテーションを付けて下さい。
* カラム名は重複しないようにして下さい。   
* mappedByを指定していない側のリレーションは@NoFkアノテーションを付けて下さい。
* rollupなど使いたかったので、idがnullの場合もエンティティのオブジェクトを作るようにしました。
