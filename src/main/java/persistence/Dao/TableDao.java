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
// import com.codahale.metrics.*;

import java.sql.SQLException;
import java.util.ArrayList;

//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Component;


/**
 * Created by Henry Hottelet
 */
public interface TableDao {


    public String setTables(String keySpace, String tableId, String restaurantId, String seatNumber) throws SQLException, Exception, CustomException;

    public ArrayList<String> getTables(ResultSet results);

    public String getTables(String keySpace, String tableId) throws SQLException, Exception, CustomException;

}
