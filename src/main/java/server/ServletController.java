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

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
// import org.springframework.ui.Model;
import persistence.Connect.*;
import Model.*;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.http.HttpStatus;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;

import org.codehaus.jackson.map.ObjectMapper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.*;

@RestController
//@RequestMapping("/product")
//@Api(value="onlinestore", description="Operations pertaining to Rest API Server")
@ComponentScan("server, persistence")
public class ServletController {
    private static final Logger logger = LoggerFactory.getLogger(ServletController.class);

    @Autowired
    private persistence.Connect.SessionUtil p;

    ServletController() {
    }

    @Autowired
    ServletController(SessionUtil s) {

        this.p = s;

        try {

            s.connect("127.0.0.1");
          //  s.setupPooling("127.0.0.1");

     //       s.createSchema("accounts");
     //       s.createTable("accounts");
     //       s.createIndex("accounts");

        } catch (Exception e) {
            logger.error("ServletController::ServletController(): Here is some ERROR: " + e);
        }

    }

    @ApiOperation(value = "View a list of available products",response = Iterable.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully retrieved list"),
            @ApiResponse(code = 401, message = "You are not authorized to view the resource"),
            @ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
            @ApiResponse(code = 404, message = "The resource you were trying to reach is not found")
    }
    )
    @RequestMapping(value = "/list", method= RequestMethod.GET, produces = "application/json")
    public String list(){
        //Iterable<Product> productList = productService.listAllProducts();
        return "";
    }

    @ApiOperation(value = "Greetings from Rest Server")
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

    @ResponseStatus(HttpStatus.CREATED)
    @RequestMapping(value = "/addRestaurant", method = RequestMethod.POST)
    public String addRestaurant(@RequestBody String json) {

        logger.info("json:" + json);

        try {

            ObjectMapper mapper = new ObjectMapper();
            Model.Restaurant value = mapper.readValue(json, Model.Restaurant.class);

   //ToDo         p.setRestaurants("accounts",value.getRestaurantId(), value.getCuisine(), value.getSeating());

            return "200/OK";

        }
        catch (Exception e)
        {

            logger.error("ServletController::addRestaurant" + e);
        }

        logger.error("500/Error");
        return "500/Error";

    }

    @ResponseStatus(HttpStatus.CREATED)
    @RequestMapping(value = "/addTable", method = RequestMethod.POST)
    public String addTable(@RequestBody String json) {

        logger.info("json:" + json);

        try {

            ObjectMapper mapper = new ObjectMapper();
            Model.Table value = mapper.readValue(json, Model.Table.class);

 //ToDo           p.setTables("accounts", value.getTableId(),value.getRestaurantId(),value.getSeatNumber());

            return "200/OK";

        }
        catch (Exception e)
        {

            logger.error("ServletController::addTable" + e);
        }

        logger.error("500/Error");
        return "500/Error";

    }

    @RequestMapping("/getRestaurants")
    public ArrayList<String> getRestaurants() {

        ArrayList<String> restaurantList = new ArrayList<String>();

        String cql = "SELECT * FROM accounts.restaurants allow filtering;";
        logger.debug("cql: " + cql);
        try {
//ToDo            restaurantList = p.getRestaurants(p.querySchema("accounts", cql));
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
    public ArrayList<String> getTables(@RequestBody String json) {

        logger.info("json:" + json);

        ArrayList<String> tableList = new ArrayList<String>();

        String cql = "SELECT * FROM accounts.restaurants WHERE cuisine =\" + \"'\" + cuisine + \"'\" +  \" AND seats=\" + \"'\" + seats + \"'\" + allow filtering;";
        logger.debug("cql: " + cql);

        try {
 //ToDo            tableList = p.getTables(p.querySchema("accounts", cql));
        } catch (Exception e) {
            e.printStackTrace();
        }
        logger.debug("tableList: " + tableList);

        return tableList;
    }

    /*

    Feature A. Reserve a table for a group of customers, by looking up the table by TableId.

     suggesting NapSack algorithm

    Added feature B, is to lookup by group size, if a group size is between 7 and 10, multiple tables need to be joined.
     It should reserve the most optimal table (e.g. a group of 3 should be seated in a table of 4, if available

     */
    @ResponseStatus(HttpStatus.CREATED)
    @RequestMapping(value = "/reserveTable", method = RequestMethod.POST)
    public String reserveTable(@RequestBody String json) {

        logger.info("json:" + json);

        ArrayList<String> tableList = new ArrayList<String>();

        String cql = "SELECT * FROM accounts.restaurants WHERE cuisine =\" + \"'\" + cuisine + \"'\" +  \" AND seats=\" + \"'\" + seats + \"'\" + allow filtering;";
        logger.debug("cql: " + cql);


        try {

            ObjectMapper mapper = new ObjectMapper();
            Model.Table value = mapper.readValue(json, Model.Table.class);

            // split order of 10 seats into two orders with 7 and 3.

            // tableList = p.getTables(("accounts", cql));
//            if(value.getSeatNumber())
//            {
//                Integer quantityremainder = 0;
//                // first order for 7, returns remainder.
//                quantityremainder = p.getTables("accounts", value.getTableId(), value.getSeatNumber());
//
//                // second order for 3.
//                p.getTables("accounts", value.getTableId(), quantityremainder);
//            }



        } catch (Exception e) {
            e.printStackTrace();
        }


        return "200/OK";
    }


    /*

    Free-up (unreserve a table after the customers leave the restaurant, by adding back the TableId to the Table inventory.

     */
    @ResponseStatus(HttpStatus.CREATED)
    @RequestMapping(value = "/leaveTable", method = RequestMethod.POST)
    public String leaveTable(@RequestBody String json) {

        logger.info("json:" + json);

        try {

            ObjectMapper mapper = new ObjectMapper();
            Model.Table value = mapper.readValue(json, Model.Table.class);

//ToDo            p.setTables("accounts", value.getTableId(),value.getRestaurantId(),value.getSeatNumber());

        } catch (Exception e) {
            e.printStackTrace();
        }

        return "200/OK";
    }

}