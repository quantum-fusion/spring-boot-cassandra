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

package com.persistence.dao;

import com.persistence.pojo.*;
import com.persistence.CustomException;
// import com.datastax.driver.core.*;
import com.datastax.driver.core.querybuilder.Insert;
import com.datastax.driver.core.querybuilder.QueryBuilder;
// import com.codahale.metrics.*;

import java.util.ArrayList;
import org.junit.Test;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PropertiesDriverITest {

    private String propertiesIpaddress = "127.0.0.1";
    private Orm p = new Orm();

   private static final Logger logger = LoggerFactory.getLogger(PropertiesDriverITest.class);

    @Test
    public void testSingleConnection() {

        String ipAddress = propertiesIpaddress;

        try {
            p.connect(propertiesIpaddress);
          // p.setupPooling(propertiesIpaddress);
        } catch (Exception e) {
            e.printStackTrace();
        }

        p.close();

    }

    @Test
    public void createSchemaTest() {

        String keySpaceName = "accounts";

        try {
            //p.connect(propertiesIpaddress);
            p.setupPooling(propertiesIpaddress);
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            p.createSchema(keySpaceName);
            p.createTable(keySpaceName);

            p.close();
        } catch (Exception e) {
            e.printStackTrace();
             logger.info("CQLDriverITest::testMain; threw a SQL Exception");
        }

    }


}