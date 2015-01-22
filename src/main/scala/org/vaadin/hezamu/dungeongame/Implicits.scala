package org.vaadin.hezamu.dungeongame

import vaadin.scala._
import rx.lang.scala.Observable
import rx.lang.scala.Subscription

object Implicits {
  // Monkey patch Button to add function to get the click event stream
  implicit class Button2Observable(val b: Button) extends Button {
    def clickEvents: Observable[Button.ClickEvent] = Observable(b.clickListeners += _.onNext _)
  }
}
