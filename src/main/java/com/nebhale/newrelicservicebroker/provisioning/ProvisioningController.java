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

package com.nebhale.newrelicservicebroker.provisioning;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.Map;

@RestController
final class ProvisioningController {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @RequestMapping(method = RequestMethod.PUT, value = "/v2/service_instances/*")
    ProvisioningResponse create(@RequestBody ProvisioningRequest provisioningRequest) {
        this.logger.info("Provisioning Request Received: {}", provisioningRequest);
        return new ProvisioningResponse(null);
    }

    @RequestMapping(method = RequestMethod.DELETE, value = "/v2/service_instances/*")
    Map<?, ?> delete(@RequestParam("service_id") String serviceId, @RequestParam("plan_id") String planId) {
        this.logger.info("De-provisioning Request Received: service_id: {}, plan_id: {}", serviceId, planId);
        return Collections.emptyMap();
    }

}
