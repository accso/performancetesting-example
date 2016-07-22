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

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import de.accso.performancetesting.domain.Product;
import de.accso.performancetesting.domain.ProductRepository;

@Component
public class ProductServiceImpl implements ProductService {

    @Autowired
    ProductRepository productRepository;

    @Override
    public Iterable<Product> findAll() {
        return productRepository.findAll();
    }

    @Override
    public Product findById(Long productId) {
        return productRepository.findOne(productId);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Iterable<Product> findComplement(List<Product> productsToBeComplemented) {

        Iterable<Product> allProducts = findAll();
        List<Product> complement = new ArrayList<Product>();
        for (Product product : allProducts) {
            if (!productsToBeComplemented.contains(product)) {
                complement.add(product);
            }
        }
        return complement;
    }

}
