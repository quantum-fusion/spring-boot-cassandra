/*
 * Copyright 2015-2018 the original author or authors.
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

/*
 * @author Henry Hottelet
 */

package server;

import com.persistence.pojo.*;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.HttpStatus;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Qualifier;

import org.springframework.stereotype.Service;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.*;

@RestController
@ComponentScan("server, com.persistence")
public class ServletController {
    private static final Logger logger = LoggerFactory.getLogger(ServletController.class);

    @Autowired
    private com.persistence.pojo.Orm p;

    ServletController() {
    }

    @Autowired
    ServletController(Orm s) {

        this.p = s;

        s.connect("127.0.0.1");

        try {
            s.createSchema("accounts");
            s.createTable("accounts");

        } catch (Exception e) {
            logger.error("ServletController::ServletController(): Here is some ERROR: " + e);
        }

    }

    @RequestMapping("/")
    public String index() {

        return "Greetings from Rest Server!";
    }

    @RequestMapping("/helloworld")
    public String helloworld() {

        return "helloworld!";
    }

    @ResponseStatus(HttpStatus.CREATED)
    @RequestMapping(value = "/postMessage", method = RequestMethod.POST)

    public String postMessage(@RequestBody String jsonRequest) {

        logger.info("jsonRequest:" + jsonRequest);

        return "200/OK";

    }

    @RequestMapping("/greeting")
    public String greeting(@RequestParam(value = "name", defaultValue = "World") String name) {

        logger.info("greeting info message");

        return "greeting info message";
    }

    @RequestMapping("/getRestaurants")
    public ArrayList<String> getRestaurants() {

        ArrayList<String> restaurantList = new ArrayList<String>();

        //ToDo (Query) need to check if user account, and token is registered yet, if not, need to register as a new customer
        String cql = "SELECT * FROM accounts.restaurants allow filtering;";
        logger.debug("cql: " + cql);
        try {
            restaurantList = p.getRestaurants(p.querySchema("accounts", cql));
        } catch (Exception e) {
            e.printStackTrace();
        }
        logger.debug("restaurantList: " + restaurantList);

        return restaurantList;
    }

    /*

    Check availability for an optimal table, for a given customer group size and a choice of cuisine.
     */
    @ResponseStatus(HttpStatus.CREATED)
    @RequestMapping(value = "/getTables", method = RequestMethod.POST)
    public ArrayList<String> getTables() {

        ArrayList<String> tableList = new ArrayList<String>();

        //ToDo (Query) need to check if user account, and token is registered yet, if not, need to register as a new customer
        String cql = "SELECT * FROM accounts.restaurants allow filtering;";
        logger.debug("cql: " + cql);

        try {
            // tableList = p.getTables(p.querySchema("accounts", cql));
        } catch (Exception e) {
            e.printStackTrace();
        }
        logger.debug("tableList: " + tableList);

        return tableList;
    }

    /*

    Reserve a table for a group of customers

     If a group size is between 7 and 10, multiple tables need to be joined.
     It should reserve the most optimal table (e.g. a group of 3 should be seated in a table of 4, if available

     */
    @ResponseStatus(HttpStatus.CREATED)
    @RequestMapping(value = "/reserveTable", method = RequestMethod.POST)
    public ArrayList<String> reserveTable() {

        ArrayList<String> tableList = new ArrayList<String>();

        //ToDo (Query) need to check if user account, and token is registered yet, if not, need to register as a new customer
        String cql = "SELECT * FROM accounts.restaurants allow filtering;";
        logger.debug("cql: " + cql);

        try {
            // tableList = p.getTables(p.querySchema("accounts", cql));
        } catch (Exception e) {
            e.printStackTrace();
        }
        logger.debug("tableList: " + tableList);

        return tableList;
    }


    /*

    Free-up (unreserve a table after the customers leave the restaurant

     */
    @ResponseStatus(HttpStatus.CREATED)
    @RequestMapping(value = "/leaveTable", method = RequestMethod.POST)
    public String leaveTable() {

        return "200/OK";
    }

}