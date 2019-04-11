package it.polimi.genomics.repository.federated.communication

import com.softwaremill.sttp._
import it.polimi.genomics.repository.Utilities
import it.polimi.genomics.repository.federated.Location
import org.slf4j.{Logger, LoggerFactory}

import scala.concurrent.duration.Duration
import scala.xml.{Elem, XML}

class NameServer {

  val logger: Logger = LoggerFactory.getLogger(Utilities.getClass)

  val AUTH_HEADER_NAME_NS = "Authorization"

  // Parameters initialization

  val NS_ADDRESS =
    if (Utilities().GF_NAMESERVER_ADDRESS.isEmpty ) {
      logger.error("GF_NAMESERVER_ADDRESS is not set in repository.xml. Please provide the NameServer address.")
      ""
    } else
      Utilities().GF_NAMESERVER_ADDRESS.get

  var NS_INSTANCENAME  =
    if (Utilities().GF_INSTANCENAME.isEmpty) {
      logger.error("GF_NAMESPACE is not set in repository.xml. Please provide your institutional namespace, e.g. it.polimi.")
      ""
    } else
      Utilities().GF_INSTANCENAME.get

  var NS_TOKEN      =
    if (Utilities().GF_TOKEN.isEmpty) {
      logger.error("GF_TOKEN is not set in repository.xml. Please provide the token associated to your namespace")
    } else
      Utilities().GF_TOKEN.get


  // Perform a get request to the name server
  def get(URI: String) : Elem = {

    val address = NS_ADDRESS+URI

    logger.info("rest_get->uri " + address)
    logger.info("rest_get->authorization " + AUTH_HEADER_NAME_NS+" Token"+NS_TOKEN)

    val request = sttp.get(uri"$address").readTimeout(Duration.Inf)
      .header("Accept","application/xml")
      .header(AUTH_HEADER_NAME_NS,  "Token "+NS_TOKEN)

    implicit val backend = HttpURLConnectionBackend()
    val response = request.send()

    val responseUnsafeBody = response.unsafeBody
    logger.info("rest_get->response.unsafeBody " + responseUnsafeBody)

    XML.loadString(response.unsafeBody)

  }

  // Perform a post request to the name server
  def post(URI: String, body: Map[String, String]) = {
    val address = NS_ADDRESS+URI
    val request = sttp
      .body(body)
      .post(uri"$address")
      .header("Accept","application/xml")
      .header("Authorization",s"Token $NS_TOKEN")

    implicit val backend = HttpURLConnectionBackend()

    val response = request.send()
    XML.loadString(response.unsafeBody)
  }

  // Ask for a new token to communicate with the target namespace
  def resetToken (target:String) : Token= {

    val URI = "/api/authentication/"
    val body = Map("target"->target)


    val token_xml     = post(URI, body)
    val token_string  = token_xml \ "token" text
    val token_expdate = token_xml \ "expiration" text

    new Token(token_string, token_expdate)

  }

  // Validate token
  def validateToken (from: String, token: String) : Boolean = {

    val URI = "/api/authentication/"+from+"_"+NS_INSTANCENAME+"/"
    println(URI)

    try {
      val tokenServer = get(URI)  \ "token" text

      tokenServer == token
    } catch {
      case _=> false
    }

  }

  // returns address
  def resolveLocation(location_id: String) : Location = {

    val location_xml = get("/api/location/"+location_id)
    new Location(location_xml)

  }

}