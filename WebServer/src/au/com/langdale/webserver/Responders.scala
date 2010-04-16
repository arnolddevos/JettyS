package au.com.langdale.webserver

import scala.xml.Node
import javax.servlet.http.{HttpServletResponse}

object Responders {
  type Responder = HttpServletResponse => Unit
  
  case class ContentType(mime: String)
  
  object ContentType { implicit val defaultValue = ContentType("application/xhtml+xml") }
  
  implicit def toXMLResponse(content: Node)(implicit contentType: ContentType): Responder = {
    response =>
      response.setContentType(contentType.mime)
      response.getWriter.write(content.toString)
  }
  
  implicit def toPlainTextResponse(content: String): Responder = {
    response =>
      response.setContentType("text/plain")
      response.getWriter.write(content)
  }
  
  implicit def toErrorResponse(code: Int): Responder = { _.sendError(code) }
  
  def redirection(url: String): Responder = { 
    response => response.sendRedirect(response.encodeRedirectURL(url))  
  }
}
