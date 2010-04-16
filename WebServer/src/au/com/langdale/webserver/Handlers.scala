package au.com.langdale.webserver

import org.mortbay._
import jetty.handler.{AbstractHandler, ContextHandler}
import jetty.{Handler,HttpConnection}
import javax.servlet.http.{HttpServletRequest, HttpServletResponse}

object Handlers {
  
  def on(method: String, vhost: Option[String], prefix: String)(action: HttpServletRequest => HttpServletResponse => Unit) {
    val context = new ContextHandler(prefix)
    context.setVirtualHosts(vhost.toList.toArray)
    
    context setHandler new AbstractHandler {
      def handle( target: String, request: HttpServletRequest, response: HttpServletResponse, kind: Int) {
        if(request.getMethod == method) {
          HttpConnection.getCurrentConnection.getRequest.setHandled(true)
          action(request)(response)
        }
      }
    }
    
    val handlers = Driver.contexts.getHandlers
    val extra = Array[Handler](context)
    Driver.contexts.setHandlers(if(handlers == null) extra else handlers ++ extra)
  }
  
  def get(prefix: String)(action: HttpServletRequest => HttpServletResponse => Unit) = on("GET", None, prefix)(action)

  def post(prefix: String)(action: HttpServletRequest => HttpServletResponse => Unit) = on("POST", None, prefix)(action)
}
