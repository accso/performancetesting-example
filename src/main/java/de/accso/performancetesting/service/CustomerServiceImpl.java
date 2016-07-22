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
package de.accso.performancetesting.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import de.accso.performancetesting.domain.Customer;
import de.accso.performancetesting.domain.CustomerRepository;

@Component
public class CustomerServiceImpl implements CustomerService {

    @Autowired
    private CustomerRepository repo;

    @Override
    public Customer create(Customer customer) {
        return repo.save(customer);
    }

    @Override
    public Customer findById(Long customerId) {
        return repo.findOne(customerId);
    }

    @Override
    public Iterable<Customer> findAll() {
        return repo.findAll();
    }

    @Override
    public Customer update(Customer customer) {
        return repo.save(customer);
    }
}
