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
package de.accso.performancetesting;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import de.accso.performancetesting.domain.CustomerRepository;
import de.accso.performancetesting.domain.Product;
import de.accso.performancetesting.domain.ProductRepository;

@SpringBootApplication
public class Application implements CommandLineRunner {

    @Autowired
    CustomerRepository customerRepository;

    @Autowired
    ProductRepository productRepository;

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        List<Product> products = new ArrayList<Product>();
        Product product1 = new Product("Kaffee ganze Bohne mild", "Ein wunderbar milder Kaffee");
        products.add(product1);
        Product product2 = new Product("Kaffee gemahlen", "Ein kräftiger Kaffee gemahlen");
        products.add(product2);
        Product product3 = new Product("Espresso", "extra stark");
        products.add(product3);
        Product product4 = new Product("Kaffeefilter", "Größe 4");
        products.add(product4);
        productRepository.save(products);
    }

}
