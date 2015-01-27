package org.vaadin.hezamu.dungeongame.model

import scala.util.Random

case class Cell(x: Int, y: Int) {
  def +(other: Cell) = Cell(x + other.x, y + other.y)
}

class Dungeon {
  var floors: Set[Cell] = Set()
  var entities: Map[Entity, Cell] = Map()

  def visibleIlluminatedCells = {
    val lightSources = entities map {
      case (entity, cell) => (cell, entity.illuminationRadius)
    }

    val visibles = lightSources flatMap MapLogic.area filter inPlayerSightRange

    floors intersect visibles.toSet
  }

  def playerOpt = entities.keys collect { case p: Player => p } headOption

  def playerPosition = playerOpt map entities

  def playerSightRange = playerOpt map { _.sightRange } getOrElse 0

  def inPlayerSightRange(cell: Cell) = playerPosition map { playerPos =>
    MapLogic.distance(playerPos, cell) <= playerSightRange
  } getOrElse false

  def isOccupied(cell: Cell) = entities.values exists { _ == cell }

  def canMoveTo(cell: Cell) = floors.contains(cell) && !isOccupied(cell)

  def randomFreeCell = Random.shuffle(floors.toSeq) filter canMoveTo head

  def put(entity: Entity, cell: Cell) {
    if (canMoveTo(cell)) entities = entities.updated(entity, cell)
  }

  def entityAt(cell: Cell) = entities find { _._2 == cell }
}
