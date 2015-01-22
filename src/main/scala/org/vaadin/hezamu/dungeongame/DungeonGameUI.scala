package org.vaadin.hezamu.dungeongame

import rx.lang.scala.Observable
import rx.lang.scala.Observer
import vaadin.scala._
import vaadin.scala.server.FontAwesome
import org.vaadin.hezamu.dungeongame.model._
import Implicits._

class DungeonGameUI extends UI(title = "Dungeon Game", theme = "dungeongame") {
  content = new VerticalLayout {
    margin = true

    val board = add(new GameBoard, ratio = 1, alignment = Alignment.TopCenter)

    val up = add(new Button {
      icon = FontAwesome.ArrowUp
      clickShortcut = KeyShortcut(KeyCode.ArrowUp)
    }, alignment = Alignment.BottomCenter)

    val left = add(new Button {
      icon = FontAwesome.ArrowLeft
      clickShortcut = KeyShortcut(KeyCode.ArrowLeft)
    })

    val right = add(new Button {
      icon = FontAwesome.ArrowRight
      clickShortcut = KeyShortcut(KeyCode.ArrowRight)
    })

    add(new HorizontalLayout {
      add(left)
      add(right)
    }, alignment = Alignment.MiddleCenter)

    val down = add(new Button {
      icon = FontAwesome.ArrowDown
      clickShortcut = KeyShortcut(KeyCode.ArrowDown)
    }, alignment = Alignment.TopCenter)

    setupUILogic(board, up, down, left, right)
  }

  def setupUILogic(board: GameBoard, up: Button, down: Button, left: Button, right: Button) = {
    def tryMove(delta: Cell) = board.dungeon.playerPosition map { cell =>
      cell + delta
    } filter board.dungeon.canMoveTo

    val movesColl = Vector(
      up.clickEvents map { e => tryMove(Cell(0, -1)) },
      down.clickEvents map { e => tryMove(Cell(0, 1)) },
      left.clickEvents map { e => tryMove(Cell(-1, 0)) },
      right.clickEvents map { e => tryMove(Cell(1, 0)) })

    // Emit a Option[Cell] every time player tries to move
    val moves = Observable from movesColl flatten

    // Map a legal destination cell to the set of visible cells after the move
    val visibleCells = moves collect {
      case Some(cell) =>
        board.dungeon.playerOpt foreach { board.dungeon.put(_, cell) }
        board.dungeon.visibleIlluminatedCells
    }

    // Subscribe the game board instance to the stream of legal moves.
    // This will call board.onNext() with a set of visible cells whenever
    // player performs a legal move.
    visibleCells subscribe board

    // Subscribe to the illegal move stream to show a notification every
    // time player tries an illegal move.
    moves collect { case None => None } subscribe { none =>
      Notification.show("That direction is blocked", Notification.Type.Tray)
    }
  }
}

class GameBoard extends GridLayout with Observer[Set[Cell]] {
  val dungeon = new Dungeon {
    floors = (for {
      x <- 1 to 15
      y <- 1 to 15
    } yield Cell(x, y)).toSet

    entities = entities.updated(new Player, randomFreeCell)

    0 to 6 foreach { i => entities = entities.updated(new NPC, randomFreeCell) }
  }

  onNext(dungeon.visibleIlluminatedCells) // Initial draw

  override def onNext(cells: Set[Cell]) {
    removeAllComponents

    rows = 17
    columns = 17

    drawBorders

    cells foreach { cell =>
      val style = dungeon.entityAt(cell) map {
        _._1 match {
          case _: Player => "player"
          case _: NPC => "monster"
        }
      } getOrElse {
        "floor"
      }

      add(new Label {
        styleName = s"dungeoncell $style"
      }, cell.x, cell.y, alignment = Alignment.MiddleCenter)
    }
  }

  private def drawBorders {
    val horizBorders = for (c <- 0 until columns; r <- List(0, rows - 1)) yield (c, r)
    val vertBorders = for (r <- 1 until rows - 1; c <- List(0, columns - 1)) yield (c, r)

    (horizBorders ++ vertBorders) foreach {
      case (c, r) => add(new Label {
        styleName = "dungeoncell wall"
      }, c, r, alignment = Alignment.MiddleCenter)
    }
  }
}
