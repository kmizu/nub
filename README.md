# Nub: A Tiny Programming Language

このレポジトリは[プログラミング言語作成ハンズオン](https://connpass.com/event/41184/)向けのレポジトリです。
Nubとは英語で**小さな塊や隆起**を意味する言葉です。
Nubの文法は[ANTLR4](http://www.antlr.org/)を使って書かれています。
NubのインタプリタはJavaを使って書かれています。

NubのコンパイルにはMaven 3が必要です。
MavenがANTLR4プラグインを提供するため、ANTLR4は必要ありません。

* `src/main/antl4/com/github/kmizu/nub/Nub.g`: ANTLRによるNubの文法定義
* `src/main/java/...`: Javaソースコード
* `pom.xml`: Maven用コンフィグ

## 開発者向けドキュメント

[How To Run](HOW_TO_RUN.md)

## 整数リテラル

```java
1
5
10
100
1000
...
```

## 四則演算式

```java
1 + 2
1 + 2 * 3
(1 + 2) * 3
(1 + 2) * 3 / 4
```

## 値宣言・代入・呼出

各プログラムの行の最後にはセミコロンが必要です。

```java
let x = 1;
let y = 2;
print(x + y); //3
x = 2;
print(x + y); //4
// let x = 1; is not allowed since x is already defined
```

## 比較演算式

```java
1 < 2
3 < 4
3 <= 3
6 > 5
7 > 5
7 >= 7
1 == 1
5 == 5
1 != 2
3 != 2
```

## プリント

### 文法

```java
print(expression);
```

初期は整数値の表示のみ対応しています。
将来的に整数値以外にも対応する予定です。

## 条件式

### 文法

```
if (condition) {
  expression
  ...
} (else {
  expression
  ...
})?
```

### 例

```
if(1 < 2) {
  print(1);
}else {
  print(2);
}
```

## ループ式

### 文法

```
while(condition) {
  expression
  ...
}
```

### 例

```
let i = 0;
while(i < 10) {
  print(i);
  i = i + 1;
}
```

値宣言には`let`を使います。代入演算sもサポートされています。

## コメント

行コメントをサポートします。
例えば、

```
// abcdef
1 + 2; // 3
```

上記の`//abcdef`は無視されます。

## 関数宣言・呼出

```
def printRange(from, to) {
  let i = from;
  while(i <= to) {
    println(i);
    i += 1;
  }
}
printRange(1, 10); // from 1 to 10 are printed
```
