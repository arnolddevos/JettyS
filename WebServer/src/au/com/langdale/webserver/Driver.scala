package au.com.langdale.webserver

import org.mortbay._
import jetty.Server
import jetty.handler.{HandlerCollection, DefaultHandler, AbstractHandler, ContextHandlerCollection}
import javax.servlet.http.{HttpServletRequest, HttpServletResponse}
import log.Log

object Driver {
  val server = new Server
  val contexts = new ContextHandlerCollection
  val handlers = new HandlerCollection
  handlers setHandlers Array(requestLogger, contexts, new DefaultHandler)
  server setHandler handlers
  
  def start = server.start
  def join = server.join
  def stop = server.stop
  
  object requestLogger extends AbstractHandler {
    def handle( target: String, request: HttpServletRequest, response: HttpServletResponse, kind: Int) {
      Log info request.getMethod + " " + target
    }
  }
}
