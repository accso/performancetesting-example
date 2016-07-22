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
import scala.util.Random

/**
 * Legt Kunden an, sowie einen Favoriten für jeden Kunden
 */
class TestCreateCustomer extends Simulation {

  val httpProtocol = http
    .baseURL("http://localhost:8080")

  val numberOfcustomers = 888

  // Szenario definieren  
  var scn = scenario("CreateSomeCustomers")

  val customerCreator = repeat(numberOfcustomers, "customerNo")(
    // Kunden anlegen
    exec(http("Neuen Kunden anlegen")
      .post("/customers")
      .formParam("firstName", "Vorname_${customerNo}")
      .formParam("lastName", "Nachname_${customerNo}"))
  )

  scn = scn.exec(customerCreator)

  // Favoriten anlegen. Hier benutzen wir eine For-Schleife, um flexibel einen Favoriten "berrechnen" zu können 
  // Die Berechnung machen wir in der Funktion addRandomFavorite
  for (i <- 1 to numberOfcustomers) {
    scn = scn.exec(addRandomFavorite(i))
  }
  // ------------------------------------------------------------------------------------
  // helpers
  // ------------------------------------------------------------------------------------


  def addRandomFavorite(customer: Int) = {
    val r = Random


    exec(http("Favoriten hinzufuegen")
      .post("/customer/" + customer + " /favorites")
      .formParam("productId", r.nextInt(3) + 1))
  }

  // ------------------------------------------------------------------------------------
  // Simulation starten
  // ------------------------------------------------------------------------------------

  setUp(scn.inject(atOnceUsers(1))).protocols(httpProtocol)
}