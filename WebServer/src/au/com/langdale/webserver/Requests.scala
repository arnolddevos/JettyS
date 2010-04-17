package au.com.langdale.webserver
import javax.servlet.http.{HttpServletRequest}
import scala.collection.{Map, jcl}

object Requests {
  def toLong(x: String) = try { Some(x.toLong) } catch { case _:NumberFormatException => None }  
  def toDouble(x: String) = try { Some(x.toDouble) } catch { case _:NumberFormatException => None }  
  
  type Parameters = Map[String,Seq[String]]
  
  object BBox {
    def unapply(params: Parameters): Option[(Double, Double, Double, Double)] = {
      params get "BBOX" flatMap (_ firstOption) flatMap { _ split ',' flatMap toDouble match { case Seq(w, s, e, n) => Some((w, s, e, n)) case _ => None }}
    }
  }
  
  case class StringParam(name: String) {
    def unapplySeq(params: Parameters) = params get name 
  } 
  
  case class DoubleParam(name: String) {
    def unapplySeq(params: Parameters) = params get name map (_ flatMap toDouble)
  } 
  
  case class LongParam(name: String) {
    def unapplySeq(params: Parameters) = params get name map (_ flatMap toLong)
  } 
  
  object Params {
    def unapply(request: HttpServletRequest) = Some(jcl.Map(request.getParameterMap).asInstanceOf[Parameters])
  }
  
  object PathInfo {
    def unapply(request: HttpServletRequest) = {
      val info = request.getPathInfo
      if( info == null ) None else Some(info)
    }
  }
  
  object & {
    def unapply(params: Parameters): Option[(Parameters, Parameters)] = Some(params, params)
  }
  
  class RichRequest(inner: HttpServletRequest) {
    def apply(name: String): Option[String] = { val value = inner.getParameter(name); if(value == null) None else Some(value) }
    def params = jcl.Map(inner.getParameterMap).asInstanceOf[Parameters]
  }
  
  implicit def toRichRequest(inner: HttpServletRequest) = new RichRequest(inner)
}
