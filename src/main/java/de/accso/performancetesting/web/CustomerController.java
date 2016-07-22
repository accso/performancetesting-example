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
package de.accso.performancetesting.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import de.accso.performancetesting.domain.Customer;
import de.accso.performancetesting.domain.Product;
import de.accso.performancetesting.service.CustomerService;
import de.accso.performancetesting.service.ProductService;

@Controller
public class CustomerController {

    @Autowired
    CustomerService customerService;

    @Autowired
    ProductService productService;

    @RequestMapping(value = "/customers", method = RequestMethod.GET)
    public String customersList(Model model) {

        Iterable<Customer> myCustomers = customerService.findAll();
        model.addAttribute("customers", myCustomers);

        return "customerOverview";
    }

    @RequestMapping(value = "/customer/{id}", method = RequestMethod.GET)
    public String getCustomer(@PathVariable Long id, Model model) {

        Customer customer = customerService.findById(id);
        model.addAttribute("customer", customer);
        model.addAttribute("products", productService.findComplement(customer.getFavorites()));
        return ("customerDetail");
    }

    @RequestMapping(value = "/customers", method = RequestMethod.POST)
    public String createCustomer(@RequestParam String firstName, @RequestParam String lastName, Model model) {

        Customer customer = new Customer(firstName, lastName);
        Customer newCustomer = customerService.create(customer);

        model.addAttribute("customer", newCustomer);
        return "redirect:/customer/" + newCustomer.getId();
    }

    @RequestMapping(value = "/customer/{customerId}/favorites", method = RequestMethod.POST)
    public String createCustomer(@PathVariable Long customerId, @RequestParam Long productId, Model model) {

        Customer customer = customerService.findById(customerId);
        Product newFavorite = productService.findById(productId);
        customer.addFavorite(newFavorite);
        customerService.update(customer);

        model.addAttribute("customer", customer);
        return "redirect:/customer/" + customer.getId();
    }

}
