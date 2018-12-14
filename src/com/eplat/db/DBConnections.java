package com.eplat.db;

import java.util.ArrayList;

/**
 * 用于管理多个连接放入线程变量
 * 
 * @author Administrator
 * 
 */
public class DBConnections {
	public ArrayList<DBConnection> connections = new ArrayList<DBConnection>();

	public void setConnection(DBConnection dbConnection) {
		this.connections.add(dbConnection);

	}

	public DBConnection getConnection(String dbid) throws Exception {
		DBConnection connection = null;
		for (int i = 0; i < connections.size(); i++) {
			if (dbid.equalsIgnoreCase(connections.get(i).getConfiguration()
					.getAssistInfo("dbid"))) {
				connection = connections.get(i);
				break;
			}
		}
		return connection;
	}

	public void clear() throws Exception {
		for (int i = 0; i < connections.size(); i++) {
			connections.get(i).closeConnection();
			connections.set(i, null);
		}
		connections.clear();
		connections = null;

	}

}
