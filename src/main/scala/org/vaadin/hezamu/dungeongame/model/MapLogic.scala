package org.vaadin.hezamu.dungeongame.model

object MapLogic {
  // Return cells in a rectangle with given center and radius
  def area(center: Cell, radius: Int): Seq[Cell] = for {
    x <- center.x - radius to center.x + radius
    y <- center.y - radius to center.y + radius
  } yield Cell(x, y)

  // Call the area function with a single tuple containing the center and radius
  def area(circle: Tuple2[Cell, Int]): Seq[Cell] = area(circle._1, circle._2)

  import Math._
  def distance(a: Cell, b: Cell) = sqrt(pow(a.x - b.x, 2) + pow(a.y - b.y, 2))
}
