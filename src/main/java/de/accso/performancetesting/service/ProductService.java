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

import de.accso.performancetesting.domain.Product;
import java.util.List;

public interface ProductService {

    Iterable<Product> findAll();

    Product findById(Long productId);

    /**
     * Returns all products contained in the data base that are not contained in productsToBeComplemented
     * @param productsToBeComplemented
     * @return
     */
    Iterable<Product> findComplement(List<Product> productsToBeComplemented);
    
}
