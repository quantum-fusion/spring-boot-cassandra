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

package persistence.Connect;

import persistence.Util.CustomException;
import com.datastax.driver.core.*;
// import com.codahale.metrics.*;

import java.sql.SQLException;

import org.springframework.stereotype.Component;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Created by Henry Hottelet
 */
@Component
public class SessionUtil {

    //  private static final Logger logger = Logger.getLogger(Orm.class);
    private static final Logger logger = LoggerFactory.getLogger(SessionUtil.class);

    public Cluster cluster;
    // private Metadata metadata;
    //   private ArrayList<String> queryResults = new ArrayList<String>();




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

    public void createIndex(String keySpace) throws SQLException, CustomException {

        try {

            Session session = cluster.connect(keySpace);

            // Index for roomlocation Table
            //    session.execute("CREATE INDEX list_roomname ON accounts.roomlocation (roomname);");


            logger.info("Connected to cluster: %s\n" +
                    cluster.getClusterName());
        }
        catch (Exception e) {
            customException("Orm::createTable exception: ", e);
        }
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

            customException("Orm::setupPooling exception: ", e);
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

            customException("Orm::createSchema exception: ", e);
        }
    }

    public void deleteSchema(String keySpace) throws SQLException, CustomException {

        try {
            Session session = cluster.connect();

            session.execute("DROP KEYSPACE " + keySpace);

            logger.info("Connected to cluster: %s\n" +
                    cluster.getClusterName());
        } catch (Exception e) {
            customException("Orm::deleteSchema exception: ", e);
        }
    }


}
