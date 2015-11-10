/*
 * Copyright 2014 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.nebhale.newrelicservicebroker.catalog;

import org.springframework.util.Assert;

import java.util.HashMap;
import java.util.Map;

final class Cost {

    private final PlanMetadata planMetadata;

    private final Object monitor = new Object();

    private volatile Map<String, Double> amount;

    private volatile String unit;

    Cost(PlanMetadata planMetadata) {
        this.planMetadata = planMetadata;
    }

    Map<String, Double> getAmount() {
        synchronized (this.monitor) {
            Assert.notEmpty(this.amount, "Costs must specify at least one amount");
            return this.amount;
        }
    }

    String getUnit() {
        synchronized (this.monitor) {
            Assert.notNull(this.unit, "Costs must specify a unit");
            return this.unit;
        }
    }

    PlanMetadata and() {
        synchronized (this.monitor) {
            return this.planMetadata;
        }
    }

    Cost amount(String currency, Double value) {
        synchronized (this.monitor) {
            if (this.amount == null) {
                this.amount = new HashMap<>();
            }

            this.amount.put(currency, value);
            return this;
        }
    }

    Cost unit(String unit) {
        synchronized (this.monitor) {
            this.unit = unit;
            return this;
        }
    }

}
