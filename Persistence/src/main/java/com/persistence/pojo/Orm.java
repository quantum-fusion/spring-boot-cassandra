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

package com.persistence.pojo;

import com.persistence.CustomException;
import com.datastax.driver.core.*;
import com.datastax.driver.core.querybuilder.Insert;
import com.datastax.driver.core.querybuilder.QueryBuilder;
import com.datastax.driver.core.Metadata;
// import com.codahale.metrics.*;

import java.sql.SQLException;
import java.util.ArrayList;
import java.sql.Blob;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Created by Henry Hottelet
 */
@Component
public class Orm {

   //  private static final Logger logger = Logger.getLogger(Properties.class);
    private static final Logger logger = LoggerFactory.getLogger(Orm.class);

    private String text;

    public Cluster cluster;
   // private Metadata metadata;
 //   private ArrayList<String> queryResults = new ArrayList<String>();

    public void setText(String text) {

        this.text = text;
    }

    public String getText() {

        return this.text;
    }


    public void customException(String text, Exception e) throws CustomException {

        logger.error(text);
        throw new CustomException(text, e);
    }

    public void connect(String node) throws CustomException {

        try {
            cluster = Cluster.builder()
                    .addContactPoint(node)
                    .build();

            // metadata = cluster.getMetadata();
       //     logger.info("Connected to cluster:" + metadata.getClusterName() + "\n");
//            for (Host host : metadata.getAllHosts()) {
//                System.out.printf("Datacenter: %s; Host: %s; Rack: %s\n",
//                        host.getDatacenter(), host.getAddress(), host.getRack());
//            }

            System.out.printf("Connected to cluster: %s\n", cluster.getClusterName());

        } catch (Exception e) {

            customException("Failed to connect", e);
        }
    }

    public void close() {
        cluster.close();

        logger.info("Disconnected from cluster: %s\n" +
                cluster.getClusterName());
    }

    public void setupPooling(String ipAddress) throws CustomException {
        //TODO need to determine these configurable settings for connection pooling and read/write performance latency
        //cassandra.read.consistency.level=ONE
        //cassandra.write.consistency.level=ONE
        //cassandra.retryDownedHostsDelayInSeconds=10
        //cassandra.retryDownedHosts=true
        //cassandra.autoDiscoverHosts=true

        try {
            PoolingOptions pools = new PoolingOptions();
            pools.setCoreConnectionsPerHost(HostDistance.LOCAL, 2);
            pools.setMaxConnectionsPerHost(HostDistance.LOCAL, 8);

            pools.setCoreConnectionsPerHost(HostDistance.REMOTE, 1);
            pools.setMaxConnectionsPerHost(HostDistance.REMOTE, 2);

            String cassandraNodes[] = new String[]{
                    ipAddress
            };

            final Cluster.Builder builder = new Cluster.Builder().addContactPoints(cassandraNodes).withPoolingOptions(pools);

            cluster = builder.build();
           // metadata = cluster.getMetadata();

            System.out.println("Connected to cluster:" + cluster.getClusterName());

            logger.info("Connected to cluster: %s\n" +
                    cluster.getClusterName());
        } catch (Exception e) {

            customException("Properties::setupPooling exception: ", e);
        }
    }


    public void createSchema(String keySpace) throws SQLException, CustomException {

        try {
            Session session = cluster.connect();

            session.execute("CREATE KEYSPACE " + keySpace + " WITH replication" +
                    "= {'class':'SimpleStrategy', 'replication_factor':3};");

            logger.info("Connected to cluster: %s\n" +
                   cluster.getClusterName());
        } catch (Exception e) {

            customException("Properties::createSchema exception: ", e);
        }
    }

    public void deleteSchema(String keySpace) throws SQLException, CustomException {

        try {
            Session session = cluster.connect();

            session.execute("DROP KEYSPACE " + keySpace);

            logger.info("Connected to cluster: %s\n" +
                    cluster.getClusterName());
        } catch (Exception e) {
            customException("Properties::deleteSchema exception: ", e);
        }
    }




    public void createTable(String keySpace) throws SQLException, CustomException {

        try {

            Session session = cluster.connect(keySpace);

            session.execute("CREATE TABLE restaurants (" +
                    "cuisine text," +
                    "seating text," +
                    "restaurant text PRIMARY KEY," +
                    ")WITH COMPACT STORAGE;");

//            session.execute("CREATE TABLE user (" +
//                    "mdn text," +
//                    "firstName text," +
//                    "lastName text," +
//                    "email text PRIMARY KEY," +
//                    ")WITH COMPACT STORAGE;");

            logger.info("Connected to cluster: %s\n" +
                    cluster.getClusterName());
        } catch (Exception e) {
            customException("Properties::createTable exception: ", e);
        }
    }

    public ResultSet querySchema(String keySpace, String cql) throws SQLException, CustomException {
        ResultSet r = null;

        try {
            Session session = cluster.connect(keySpace);
            r = session.execute(cql);
        }
        catch (Exception e)
        {
            customException("querySchema exception: ", e);
        }

        logger.debug("queryschema results:" + r);
        return r;
    }

    public ArrayList<String> getRestaurants(ResultSet results) throws SQLException, Exception, CustomException {
        ArrayList<String> queryResults = new ArrayList<String>();

        for (Row row : results) {

            queryResults.add(String.format("%-20s\t%-20s\t%-20s\t%-20s\t%-20s\n",
                    row.getString("restaurant"),
                    row.getString("cuisine"),
                    row.getString("seating")));
        }

        return queryResults;
    }

}
