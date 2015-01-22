package org.vaadin.hezamu.dungeongame.model

trait Entity {
  val illuminationRadius: Int
}

class NPC(val illuminationRadius: Int = 1) extends Entity

class Player(val illuminationRadius: Int = 0) extends Entity {
  val sightRange = 8
}
