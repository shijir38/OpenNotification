/*
 * Created on Aug 25, 2004
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package net.reliableresponse.notification.broker.impl.mysql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import net.reliableresponse.notification.broker.BrokerFactory;
import net.reliableresponse.notification.broker.impl.sql.GenericSQLUserMgmtBroker;
import net.reliableresponse.notification.usermgmt.User;

/**
 * @author drig
 *
 * Copyright 2004 - David Rudder
 */
public class MySQLUserMgmtBroker extends GenericSQLUserMgmtBroker {

	/* (non-Javadoc)
	 * @see net.reliableresponse.notification.broker.impl.GenericSQLUserMgmtBroker#getConnection()
	 */
	public Connection getConnection() {

		return BrokerFactory.getDatabaseBroker().getConnection();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see net.reliableresponse.notification.broker.UserMgmtBroker#getUsers(int,
	 *      int, net.reliableresponse.notification.usermgmt.User[])
	 */
	public int getUsers(int pageSize, int pageNum, User[] users) {
		int count = 0;
		if (users.length < pageSize) {
			pageSize = users.length;
		}

		String sql = "SELECT uuid, firstname, lastname, email FROM member WHERE type='1' AND deleted='N' ORDER BY lastName LIMIT ?,?";

		PreparedStatement stmt = null;
		Connection connection = getConnection();
		ResultSet rs = null;

		try {
			stmt = connection.prepareStatement(sql);
			stmt.setInt(1, pageNum * pageSize);
			stmt.setInt(2, pageSize);

			rs = stmt.executeQuery();

			while (rs.next()) {
				User user = new User();
				user.setUuid(rs.getString(1));
				user.setFirstName(rs.getString(2));
				user.setLastName(rs.getString(3));
				user.setEmailAddress(rs.getString(4));
				users[count] = user;
				count++;
			}
		} catch (SQLException e) {
			BrokerFactory.getLoggingBroker().logError(e);
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (stmt != null)
					stmt.close();
			} catch (SQLException e1) {
				BrokerFactory.getLoggingBroker().logError(e1);
			}
		}

		try {
			for (int usernum = 0; usernum < count; usernum++) {
				User user = (User) users[usernum];
				getUserInformation(user, connection);
				getUserDevices(user, connection);
				user.setAutocommit(true);
			}
		} finally {
			try {
				if (connection != null)
					connection.close();
			} catch (SQLException e1) {
				BrokerFactory.getLoggingBroker().logError(e1);
			}
		}
		return pageSize;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see net.reliableresponse.notification.broker.UserMgmtBroker#getUsersLike(int,
	 *      int, java.lang.String,
	 *      net.reliableresponse.notification.usermgmt.User[])
	 */
	public int getUsersLike(int pageSize, int pageNum, String substring,
			User[] users) {
		int count = 0;
		if (users.length < pageSize) {
			pageSize = users.length;
		}

		String sql = "SELECT uuid, firstname, lastname, email FROM member WHERE type='1' AND deleted='N' AND (lower(firstname) like ? OR lower(lastname) like ? or lower(email) like ?) ORDER BY lastName LIMIT ?,?";
		BrokerFactory.getLoggingBroker().logDebug("sql=" + (sql));
		PreparedStatement stmt = null;
		Connection connection = getConnection();
		ResultSet rs = null;

		String search = "%" + substring + "%";
		try {
			stmt = connection.prepareStatement(sql);
			stmt.setString(1, search);
			stmt.setString(2, search);
			stmt.setString(3, search);
			stmt.setInt(4, pageNum * pageSize);
			stmt.setInt(5, pageSize);

			rs = stmt.executeQuery();

			while (rs.next()) {
				User user = new User();
				user.setUuid(rs.getString(1));
				user.setFirstName(rs.getString(2));
				user.setLastName(rs.getString(3));
				users[count] = user;
				count++;

			}
		} catch (SQLException e) {
			BrokerFactory.getLoggingBroker().logError(e);
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (stmt != null)
					stmt.close();
			} catch (SQLException e1) {
				BrokerFactory.getLoggingBroker().logError(e1);
			}
		}

		try {
			for (int usernum = 0; usernum < count; usernum++) {
				User user = (User) users[usernum];

				getUserInformation(user, connection);
				getUserDevices(user, connection);
			}
		} finally {
			try {
				if (connection != null)
					connection.close();
			} catch (SQLException e1) {
				BrokerFactory.getLoggingBroker().logError(e1);
			}
		}
		return pageSize;
	}

	public int getUuids(int pageSize, int pageNum, String[] uuids) {
		int count = 0;
		if (uuids.length < pageSize) {
			pageSize = uuids.length;
		}

		String sql = "SELECT uuid FROM member WHERE type='1' ANd deleted='N' ORDER BY lastName LIMIT ?,?";

		PreparedStatement stmt = null;
		Connection connection = getConnection();
		ResultSet rs = null;

		try {
			stmt = connection.prepareStatement(sql);
			stmt.setInt(1, pageNum * pageSize);
			stmt.setInt(2, pageSize);

			rs = stmt.executeQuery();

			while (rs.next()) {
				User user = new User();
				uuids[count] = rs.getString(1);
				count++;
			}
		} catch (SQLException e) {
			BrokerFactory.getLoggingBroker().logError(e);
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (stmt != null)
					stmt.close();
				if (connection != null)
					connection.close();
			} catch (SQLException e1) {
				BrokerFactory.getLoggingBroker().logError(e1);
			}
		}
		return count;
	}
	public int getUuidsLike(int pageSize, int pageNum, String substring,
			String[] uuids) {
		int count = 0;
		if (uuids.length < pageSize) {
			pageSize = uuids.length;
		}

		String sql = "SELECT uuid FROM member WHERE type='1' AND deleted='N' AND (lower(firstname) like ? OR lower(lastname) like ? or lower(email) like ?) ORDER BY lastName LIMIT ?,?";
		BrokerFactory.getLoggingBroker().logDebug("sql=" + (sql));

		PreparedStatement stmt = null;
		Connection connection = getConnection();
		ResultSet rs = null;

		substring = "%"+substring+"%";
		try {
			stmt = connection.prepareStatement(sql);
			stmt.setString(1, substring);
			stmt.setString(2, substring);
			stmt.setString(3, substring);
			stmt.setInt(4, pageNum * pageSize);
			stmt.setInt(5, pageSize);

			BrokerFactory.getLoggingBroker().logDebug("substring="+substring);
			BrokerFactory.getLoggingBroker().logDebug("limit "+(pageNum * pageSize)+","+pageSize);
			
			rs = stmt.executeQuery();

			while (rs.next()) {
				User user = new User();
				uuids[count] = rs.getString(1);
				count++;
			}
		} catch (SQLException e) {
			BrokerFactory.getLoggingBroker().logError(e);
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (stmt != null)
					stmt.close();
				if (connection != null)
					connection.close();
			} catch (SQLException e1) {
				BrokerFactory.getLoggingBroker().logError(e1);
			}
		}
		return count;
	}

}