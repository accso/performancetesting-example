/*
 * Copyright 2016 Accso - Accelerated Solutions GmbH
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package de.accso.performancetesting

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import scala.concurrent.duration._

/**
 * Lasttest zum Kunden lesen.
 */
class TestReadCustomers extends Simulation {

  val httpProtocol = http
    .baseURL("http://localhost:8080")

  // Szenario definieren  
  val readCustomersScn = scenario("ReadCustomers").repeat(200)(
    exec(http("Kundenübersicht lesen")
      .get("/customers"))
  )

  // bestimmte Kunden anzeigen
  val readCustomerDetailsScn = scenario("readCustomerDetails").repeat(500)(
    exec(http("Kundendetails lesen")
      .get("/customer/1"))
      .randomSwitch(
        30.0 -> exec(http("Kundendetails lesen").get("/customer/1")),
        70.0 -> exec(http("Kundendetails lesen").get("/customer/2"))
      )
  )

  // ------------------------------------------------------------------------------------
  // Assertions
  // ------------------------------------------------------------------------------------
  val successfulRequests99P = global.successfulRequests.percent.greaterThan(99)
  val responseTimeLessThan1000 = forAll.responseTime.max.lessThan(1000)

  // ------------------------------------------------------------------------------------
  // Simulation starten
  // ------------------------------------------------------------------------------------

  setUp(
    readCustomersScn.inject(atOnceUsers(7)),
    readCustomerDetailsScn.inject(rampUsersPerSec(10) to 20 during (10 seconds) randomized)
  )
    .protocols(httpProtocol)
    .assertions(
      successfulRequests99P,
      responseTimeLessThan1000
    )
    .maxDuration(1 minute)
}