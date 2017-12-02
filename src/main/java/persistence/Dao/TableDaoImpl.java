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

package persistence.Dao;

import persistence.Util.CustomException;
import com.datastax.driver.core.*;
import com.datastax.driver.core.querybuilder.Insert;
import com.datastax.driver.core.querybuilder.QueryBuilder;
import com.datastax.driver.core.querybuilder.Delete;
// import com.codahale.metrics.*;

import java.sql.SQLException;
import java.util.ArrayList;

import org.springframework.stereotype.Component;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Created by Henry Hottelet
 */
@Component
public class TableDaoImpl implements TableDao {

    private static final Logger logger = LoggerFactory.getLogger(TableDaoImpl.class);

    public Cluster cluster;
   // private Metadata metadata;
 //   private ArrayList<String> queryResults = new ArrayList<String>();

    public void customException(String text, Exception e) throws CustomException {

        logger.error(text);
        throw new CustomException(text, e);
    }

    public void createTable(String keySpace) throws SQLException, CustomException {

        try {

            Session session = cluster.connect(keySpace);

            session.execute("CREATE TABLE tables (" +
                    "restaurantId text," +
                    "seatNumber text," +
                    "tableId text PRIMARY KEY," +
                    ")WITH COMPACT STORAGE;");

            logger.info("Connected to cluster: %s\n" +
                    cluster.getClusterName());
        } catch (Exception e) {
            customException("Orm::createTable exception: ", e);
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

    @Override
       public String setTables(String keySpace, String tableId, String restaurantId, String seatNumber) throws SQLException, Exception, CustomException {

        try {
            Session session = cluster.connect(keySpace);

            Insert insert = QueryBuilder
                    .insertInto("tables")
                    .value("restaurantId", restaurantId)
                    .value("seatNumber", seatNumber)
                    .value("tableId", tableId);

            logger.debug(insert.getQueryString());

            ResultSet results = session.execute(insert);

            return "200/Success";

        } catch (Exception e) {
            customException("Orm::setTables exception: ", e);
        }

        return "500/Error: Orm::setTables Exception";
    }

    @Override
    public ArrayList<String> getTables(ResultSet results) {



          ArrayList<String> queryResults = new ArrayList<String>();

          for (Row row : results) {

              queryResults.add(String.format("%-20s\t%-20s\t%-20s\n",
                      row.getString("restaurantId"),
                      row.getString("seatNumber"),
                      row.getString("tableId")));
          }

          return queryResults;

    }

    @Override
    public String getTables(String keySpace, String tableId) throws SQLException, Exception, CustomException {

        try {
            Session session = cluster.connect(keySpace);

            Delete delete = QueryBuilder
                    .delete()
                    .from("tables", "tableId");
                   // .where(eq("tableId", tableId));

            logger.debug(delete.getQueryString());

            ResultSet results = session.execute(delete);

            return "200/Success";

        } catch (Exception e) {
            customException("Orm::getTables exception: ", e);
        }

        return "500/Error: Orm::getTables Exception";
    }

}
