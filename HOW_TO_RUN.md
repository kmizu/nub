# IntelliJ IDEAを使う場合

* IntelliJ IDEAの導入
  * https://www.jetbrains.com/idea/download/
  * OSによってリンク先の指示に従ってインストールする
* Mavenプラグインの導入
  * インストールの設定次第ではすでに導入済み
  1. ツールバー[ファイル]→[設定]→[Plugins]
     * OS X: [IntelliJ IDEA]→[設定]→[Plugins].
  2. 検索欄から「Maven Integration」にチェックを入れて[OK]で閉じる
  3. IntelliJ IDEを再起動する
* Mavenを実行する
  * プロジェクトを右クリック
  * [Maven]→[Generate Sources and Update Folders]
  * `target/generated-sources/antlr4`以下が生成される
* 実行する
  * ツールバー[実行]→[実行]
  * 以下のような結果が得られる
  
     ```
     "java" ...
     1
     2
     3
     4
     5
     6
     7
     8
     9

     Process finished with exit code 0
   ```

# 端末を使う場合

TODO
