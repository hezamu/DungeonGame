package org.vaadin.hezamu.dungeongame

import vaadin.scala.server.ScaladinServlet
import javax.servlet.annotation.{ WebInitParam, WebServlet }

@WebServlet(urlPatterns = Array("/*"), initParams = Array(new WebInitParam(name = "ScaladinUI", value = "org.vaadin.hezamu.dungeongame.DungeonGameUI")))
class DungeonGameServlet extends ScaladinServlet
