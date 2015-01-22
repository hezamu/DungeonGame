# Dungeon Game

This is a demo app for my talk [Functional and Reactive UIs](http://www.slideshare.net/hezamu/functional-and-reactive-u-is-gwtcreate-2015). It shows how to apply the functional principles in your business logic, and how to use RxJava Observables to define UI logic as a mapping from the model state to what the user is seeing.

It's built with [Scala](http://scala-lang.org) as the language, [RxScala](https://github.com/ReactiveX/RxScala) for RxJava Observables, and [Scaladin](https://github.com/henrikerola/scaladin) for UI.

The project is built with [SBT](http://www.scala-sbt.org/). After cloning the project you can say

    sbt container:start

to start it. After a while the app should be available at [localhost](http://localhost:8080).

Eclipse project files can be created with

    sbt eclipse

Note that you should have [Scala IDE](http://scala-ide.org/) plugin installed.
