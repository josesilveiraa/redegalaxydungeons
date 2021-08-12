package com.gabrielblink.galaxydungeons.maxidratemysql;

import java.sql.CallableStatement;
import java.sql.SQLException;

public interface SQLCall {

	public void process(CallableStatement statement) throws SQLException;

}
