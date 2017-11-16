import _root_.io.gatling.core.scenario.Simulation
import ch.qos.logback.classic.{Level, LoggerContext}
import io.gatling.core.Predef._
import io.gatling.http.Predef._
import org.slf4j.LoggerFactory

import scala.concurrent.duration._

/**
 * Performance test for the Employee entity.
 */
class EmployeeGatlingTest extends Simulation {

    val context: LoggerContext = LoggerFactory.getILoggerFactory.asInstanceOf[LoggerContext]
    // Log all HTTP requests
    //context.getLogger("io.gatling.http").setLevel(Level.valueOf("TRACE"))
    // Log failed HTTP requests
    //context.getLogger("io.gatling.http").setLevel(Level.valueOf("DEBUG"))

    val baseURL = Option(System.getProperty("baseURL")) getOrElse """http://127.0.0.1:8080"""

    val httpConf = http
        .baseURL(baseURL)
        .inferHtmlResources()
        .acceptHeader("*/*")
        .acceptEncodingHeader("gzip, deflate")
        .acceptLanguageHeader("fr,fr-fr;q=0.8,en-us;q=0.5,en;q=0.3")
        .connection("keep-alive")
        .userAgentHeader("Mozilla/5.0 (Macintosh; Intel Mac OS X 10.10; rv:33.0) Gecko/20100101 Firefox/33.0")

    val headers_http = Map(
        "Accept" -> """application/json"""
    )

    val headers_http_authentication = Map(
        "Content-Type" -> """application/json""",
        "Accept" -> """application/json"""
    )

    val headers_http_authenticated = Map(
        "Accept" -> """application/json""",
        "Authorization" -> "${access_token}"
    )

    val scn = scenario("Test the Employee entity")
        .exec(http("First unauthenticated request")
        .get("/api/account")
        .headers(headers_http)
        .check(status.is(401))).exitHereIfFailed
        .pause(10)
        .exec(http("Authentication")
        .post("/api/authenticate")
        .headers(headers_http_authentication)
        .body(StringBody("""{"username":"admin", "password":"admin"}""")).asJSON
        .check(header.get("Authorization").saveAs("access_token"))).exitHereIfFailed
        .pause(1)
        .exec(http("Authenticated request")
        .get("/api/account")
        .headers(headers_http_authenticated)
        .check(status.is(200)))
        .pause(10)
        .repeat(2) {
            exec(http("Get all employees")
            .get("/api/employees")
            .headers(headers_http_authenticated)
            .check(status.is(200)))
            .pause(10 seconds, 20 seconds)
            .exec(http("Create new employee")
            .post("/api/employees")
            .headers(headers_http_authenticated)
            .body(StringBody("""{"id":null, "name":"SAMPLE_TEXT", "gender":null, "age":"0", "education":null, "empNumber":"SAMPLE_TEXT", "nativePlace":"SAMPLE_TEXT", "nationality":"SAMPLE_TEXT", "idNumber":"SAMPLE_TEXT", "basicInfo":"SAMPLE_TEXT", "hireDate":null, "probationSalary":null, "salary":null, "jobPositionStatus":null, "jobPositionInfo":"SAMPLE_TEXT", "birthDate":null, "marriage":"0", "childbearing":"0", "hukou":"SAMPLE_TEXT", "pensionAccount":"SAMPLE_TEXT", "houseFoundAccount":"SAMPLE_TEXT", "hasDriverLicense":"0", "cellphone":"SAMPLE_TEXT", "mail":"SAMPLE_TEXT", "qq":"SAMPLE_TEXT", "workQq":"SAMPLE_TEXT", "workQqPassword":"SAMPLE_TEXT", "wechart":"SAMPLE_TEXT", "address":"SAMPLE_TEXT", "tel":"SAMPLE_TEXT", "emergencyTel":"SAMPLE_TEXT", "emergencyContact":"SAMPLE_TEXT", "relationshipWithEmergencyContact":"SAMPLE_TEXT", "additionalInfo":"SAMPLE_TEXT", "computerLevel":"SAMPLE_TEXT", "foreignLanguageLevel":"SAMPLE_TEXT", "probationDeduction":null, "employeeDeduction":null}""")).asJSON
            .check(status.is(201))
            .check(headerRegex("Location", "(.*)").saveAs("new_employee_url"))).exitHereIfFailed
            .pause(10)
            .repeat(5) {
                exec(http("Get created employee")
                .get("${new_employee_url}")
                .headers(headers_http_authenticated))
                .pause(10)
            }
            .exec(http("Delete created employee")
            .delete("${new_employee_url}")
            .headers(headers_http_authenticated))
            .pause(10)
        }

    val users = scenario("Users").exec(scn)

    setUp(
        users.inject(rampUsers(100) over (1 minutes))
    ).protocols(httpConf)
}
