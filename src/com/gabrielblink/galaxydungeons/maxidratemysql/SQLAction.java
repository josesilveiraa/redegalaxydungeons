package com.gabrielblink.galaxydungeons.maxidratemysql;

import java.sql.ResultSet;
import java.sql.SQLException;

public interface SQLAction {

	public void process(ResultSet result) throws SQLException;

}
