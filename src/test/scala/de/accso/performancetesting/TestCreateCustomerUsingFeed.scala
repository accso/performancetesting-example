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
import io.gatling.jdbc.Predef._

/**
 * Legt Kunden mit Hilfe eines Feeds an
 */
class TestCreateCustomerUsingFeed extends Simulation {

  val httpProtocol = http
    .baseURL("http://localhost:8080")

  // Szenario definieren  
  var scn = scenario("CreateSomeCustomers")

  // csv feeder. Achtung: "," als Trenner im csv-File benutzen!
  // circular bedeutet: Fängt wieder von vorne an, wenn alle Datensätze "verbraucht" sind.
  val feeder = csv("customers.csv").circular

  // Mit einem repeat viele Kunden anlegen.
  val customerCreator = repeat(17)(
    // Kunden anlegen
    feed(feeder)
      .exec(http("Neuen Kunden anlegen")
        .post("/customers")
        .formParam("firstName", "${firstName}")
        .formParam("lastName", "${lastName}"))
  )

  scn = scn.exec(customerCreator)

  // ------------------------------------------------------------------------------------
  // Simulation starten
  // ------------------------------------------------------------------------------------

  setUp(scn.inject(atOnceUsers(1))).protocols(httpProtocol)
}