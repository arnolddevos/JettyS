package au.com.langdale
package webserver
import javax.servlet.http.{HttpServletRequest}
import scala.collection.mutable.{Map}
import scala.collection.JavaConversions._

object Requests {
  def toLong(x: String) = try { Some(x.toLong) } catch { case _:NumberFormatException => None }  
  def toDouble(x: String) = try { Some(x.toDouble) } catch { case _:NumberFormatException => None }  

  type Parameters = Map[String, Array[String]]
  
  object BBox {
    def unapply(params: Parameters): Option[(Double, Double, Double, Double)] = {
      params get "BBOX" flatMap (_ firstOption) flatMap { _ split ',' flatMap toDouble match { case Array(w, s, e, n) => Some((w, s, e, n)) case _ => None }}
    }
  }
  
  case class StringSeqParam(name: String) {
    def unapplySeq(params: Parameters): Option[Seq[Seq[String]]] = {
      params get name map { as: Array[String] =>
        val ss:Seq[String] = as 
        ss map { s: String => 
          val ps = s.split( ',' )
          val sp: Seq[String] =ps 
          sp
        }
      }
    } 
  } 
  
  case class StringParam(name: String) {
    def unapplySeq(params: Parameters): Option[Seq[String]] = params get name map { ss => ss: Seq[String] }
  } 
  
  case class DoubleParam(name: String) {
    def unapplySeq(params: Parameters): Option[Seq[Double]] = params get name map (_ flatMap toDouble)
  } 
  
  case class LongParam(name: String) {
    def unapplySeq(params: Parameters): Option[Seq[Long]] = params get name map (_ flatMap toLong)
  } 
  
  object Params {
    def unapply(request: HttpServletRequest) = Some(request.getParameterMap.asInstanceOf[java.util.Map[String, Array[String]]]: Parameters)
  }
  
  object PathInfo {
    def unapply(request: HttpServletRequest) = {
      val info = request.getPathInfo
      if( info == null ) None else Some(info)
    }
  }
  
  object & {
    def unapply[A](a: A): Option[(A, A)] = Some(a, a)
  }
  
  class RichRequest(inner: HttpServletRequest) {
    def apply(name: String): Option[String] = { val value = inner.getParameter(name); if(value == null) None else Some(value) }
    def params: Parameters = inner.getParameterMap.asInstanceOf[java.util.Map[String, Array[String]]]
  }
  
  implicit def toRichRequest(inner: HttpServletRequest) = new RichRequest(inner)
}
