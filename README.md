# Nub: A Tiny Programming Language

This repository is for [a programming language hands on](https://connpass.com/event/41184/).
Nub means **a small piece** in English.  Nub grammer is written in [ANTLR4](http://www.antlr.org/) and
nub interpreter is written in Java.

Maven 3 is required to compile nub.  ANTLR4 is not required since Maven provides ANTLR4 plugin.

* `src/main/antl4/com/github/kmizu/nub/Nub.g`: ANTLR grammar definition of Nub programming language
* `src/main/java/...`: Java source code
* `pom.xml`: For maven

## Integer Literal

```java
1
5
10
100
1000
...
```

## Arithmetic Expression

```java
1 + 2
1 + 2 * 3
(1 + 2) * 3
(1 + 2) * 3 / 4
```

## Variable Declaration & Assignment & Reference

Note that semicolon is required at the end of line

```java
let x = 1;
let y = 2;
print(x + y); //3
x = 2;
print(x + y); //4
// let x = 1; is not allowed since x is already defined
```

## Comparison Expression

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

## Print

### Syntax

```java
print(expression);
```

Currently, only printing integer is allowed.  In the future, this restriction should be relaxed.

## Conditional Expression

### Syntax

```
if (condition) {
  expression
  ...
} (else {
  expression
  ...
})?
```

### Example

```
if(1 < 2) {
  print(1);
}else {
  print(2);
}
```

## Loop Expression

### Syntax

```
while(condition) {
  expression
  ...
}
```

### Example

```
let i = 0;
while(i < 10) {
  print(i);
  let i = i + 1;
}
```

Note that `let` should be used only when variable declaration.  Assignment operator should be introduced in the future.

## Comment

Line comments are supported.  For example,

```
// abcdef
1 + 2; // 3
```

The above `//abcdef` is ignored.
