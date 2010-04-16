package au.com.langdale.webserver

import org.mortbay._
import jetty.nio.BlockingChannelConnector

object Connectors {
  def listen(port: Int) {
    val connect = new BlockingChannelConnector
    connect.setPort(port)
    Driver.server.addConnector(connect)
  }
  
  def listen(host: String, port: Int) {
    val connect = new BlockingChannelConnector
    connect.setPort(port)
    connect.setHost(host)
    Driver.server.addConnector(connect)
    
  }
}
