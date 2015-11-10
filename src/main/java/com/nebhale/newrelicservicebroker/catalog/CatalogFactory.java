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

import java.io.PrintStream;
import java.net.URI;
import java.util.UUID;
import java.util.List;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.type.TypeFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class CatalogFactory {

  public static List<NewRelicPlan> plans = null;

  public static NewRelicPlan getAssociatedPlanById(String nrPlanId) {
    if (plans == null) {
      return null;
    }
    for (NewRelicPlan p : plans) {
      System.out.println("\t\tNewRelicPlan: " + p + ", checking for planId: " + nrPlanId);
      if (p.getPlanId().equals(nrPlanId)) {
        return p;
      }
    }
    return null;
  }

  public static NewRelicPlan getAssociatedPlanByName(String nrPlanName) {
    if (plans == null) {
      return null;
    }
    for (NewRelicPlan p : plans) {
      System.out.println("\t\tNewRelicPlan: " + p + ", checking for planname: " + nrPlanName);
      if (p.getPlanName().equals(nrPlanName)) {
        return p;
      }
    }
    return null;
  }

    /*
    @Bean
    Catalog catalog(@Value("${serviceBroker.serviceId}") String serviceId,
                    @Value("${serviceBroker.planId}") String planId) throws ParseException{
        // @formatter:off
        return new Catalog()
            .service()
                .id(UUID.fromString(serviceId))
                .name("newrelic")
                .description("Manage and monitor your apps")
                .bindable(true)
                .tags("newrelic", "management", "monitoring", "apm", "analytics")
                .metadata()
                    .displayName("New Relic")
                    .imageUrl(URI.create("https://newrelic.com/images/logo/logo-newrelic-white.png"))
                    .longDescription("New Relic is the all-in-one web app performance tool that lets you see " +
                                     "performance from the end user experience, through servers, and down to the " +
                                     "line of code.")
                    .providerDisplayName("New Relic, Inc.")
                    .documentationUrl(URI.create("https://docs.newrelic.com"))
                    .supportUrl(URI.create("https://support.newrelic.com/home"))
                    .and()
                .addAllPlans(System.getenv("NRPLANS"))
                .and();
        // @formatter:on
    }
    */

  @Bean
  Catalog catalog(@Value("${NRPLANS}") String nrPlans)
    throws Exception
  {
    System.out.println("NRPLANS set to: " + nrPlans);
    String serviceId = UUID.nameUUIDFromBytes("NewRelic_ServiceId_v1".getBytes()).toString();

    ObjectMapper objectMapper = new ObjectMapper();
    objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
    plans = (List)objectMapper.readValue(nrPlans, objectMapper.getTypeFactory().constructCollectionType(List.class, NewRelicPlan.class));


    for (NewRelicPlan p : plans) {
      p.setPlanId();
      System.out.println("\t\tNewRelicPlan: " + p);
    }

    return new Catalog()
                    .service()
                          .id(UUID.fromString(serviceId))
                          .name("newrelic")
                          .description("Manage and monitor your apps")
                          .bindable(Boolean.valueOf(true))
                          .tags(new String[] { "newrelic", "management", "monitoring", "apm", "analytics" })
                          .metadata()
                              .displayName("New Relic")
                              .imageUrl(URI.create("http://storefront.nr-assets.net/assets/newrelic/source/NewRelic-logo-square.png"))
                              .longDescription("New Relic is the all-in-one web app performance tool " +
                                   "that lets you see performance from the end user experience, " +
                                   "through servers, and down to the line of code.")
                              .providerDisplayName("New Relic, Inc.")
                              .documentationUrl(URI.create("https://docs.newrelic.com"))
                              .supportUrl(URI.create("https://support.newrelic.com/home"))
                              .and()
                          .addAllPlans(plans)
                          .and();
  }
}
