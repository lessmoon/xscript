XScript
======

A c-like script language

This is a script compiler and runtime environment.Please read [this document](doc/xsbrief.md) for more information about this language grammar.

The language has a c-like way to prommgram,and if you have exprience in C/C++, Java or C#,you will be familiar to it.

**XScript** is:

1. a **strongly-typed** language.It has a very complete type check and conversion ruler.
2. a **simple** language.
3. an **extensible** language.You can extend the functions by using extending Java functions,which means you can call the **native** Java functions.
4. a **powerful** language.You can finish almost everything by using it.This language supports for `function` and `struct`.
5. a **convenient** language.It has a lot of built-in types:`int`,`char`,`bool`,`string`,`real`,and even `bigint`,`bigreal`.
6. an **object oriented** language.It supports for inheriting.You can write RTTI codes-because it supports operator `instanceof`.
7. a **script** language.And this means its GC system is based on JVM.
8. a language supporting for **multi-thread**,**anonymous nested struct** and **lambda expression**

**Dependency** :

1. JDK 8.0+(Lambda expression required.If you need run it on older java version(without lambda supported),please check out [v1.5](https://github.com/lessmoon/script/tree/v1.5) of this repo.)
2. All `.cmd` files is for Windows,but you can write similar script on other OS.

If you have any advices,you can fork it and raise a pull request.And also,extension to other language is welcome.
