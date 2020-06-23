# Black Rook Small Freemarker View Driver

Copyright (c) 2020 Black Rook Software.  
[https://github.com/BlackRookSoftware/Small-Freemarker](https://github.com/BlackRookSoftware/Small-Freemarker)

[Latest Release](https://github.com/BlackRookSoftware/Small-Freemarker/releases/latest)    
[Online Javadoc](https://blackrooksoftware.github.io/Small-Freemarker/javadoc/)


### NOTICE

This library is currently in **EXPERIMENTAL** status. This library's API
may change many times in different ways over the course of its development!


### Required Libraries

Any servlet 2.X+ container implementation (javax.servlet-api.jar).  
[Black Rook Small 0.1.0+](https://blackrooksoftware.github.io/Small)  
[Apache Freemarker](https://freemarker.apache.org/freemarkerdownload.html)  


### Required Java Modules

[java.xml](https://docs.oracle.com/en/java/javase/11/docs/api/java.xml/module-summary.html)  
* [java.base](https://docs.oracle.com/en/java/javase/11/docs/api/java.base/module-summary.html)  


### Introduction

This library contains classes for J2EE application creation and management.


### Why?

For adding Freemarker easily to Small as a model-view renderer.


### Library

Contained in this release is a series of classes that facilitate adding Freemarker support
to a Small application.


### Compiling with Ant

To download dependencies for this project, type (`build.properties` will also be altered/created):

	ant dependencies

To compile this library with Apache Ant, type:

	ant compile

To make Maven-compatible JARs of this library (placed in the *build/jar* directory), type:

	ant jar

To make Javadocs (placed in the *build/docs* directory):

	ant javadoc

To compile main and test code and run tests (if any):

	ant test

To make Zip archives of everything (main src/resources, bin, javadocs, placed in the *build/zip* directory):

	ant zip

To compile, JAR, test, and Zip up everything:

	ant release

To clean up everything:

	ant clean
	

### Javadocs

Online Javadocs can be found at: [https://blackrooksoftware.github.io/Small-Jetty/javadoc/](https://blackrooksoftware.github.io/Small-Jetty/javadoc/)

### Other

This program and the accompanying materials are made available under the 
terms of the LGPL v2.1 License which accompanies this distribution.

A copy of the LGPL v2.1 License should have been included in this release (LICENSE.txt).
If it was not, please contact us for a copy, or to notify us of a distribution
that has not included it. 

This contains code copied from Black Rook Base, under the terms of the MIT License (docs/LICENSE-BlackRookBase.txt).
