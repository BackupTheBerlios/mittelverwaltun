/*
 * Created on 01.09.2004
 *
 /*
 * Created on 01.09.2004
 *
 */
package applicationServer;

import java.io.InputStream;
import java.io.Reader;
import java.math.BigDecimal;
import java.net.URL;
import java.sql.Array;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.Date;
import java.sql.ParameterMetaData;
import java.sql.PreparedStatement;
import java.sql.Ref;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.SQLWarning;
import java.sql.Time;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.Calendar;

/**
 * @author Mario
 *
 */
public class PreparedStatementWrapper implements PreparedStatement{

	private PreparedStatement stmt;
	private int[] paraTypes;

/**********************************************************/
/* Kostrunktor(en)                                        */
/**********************************************************/
public PreparedStatementWrapper (PreparedStatement stmt, int[] paraTypes){
	this.stmt = stmt;
	this.paraTypes = paraTypes;
}

public PreparedStatementWrapper (PreparedStatement stmt){
	this.stmt = stmt;
	this.paraTypes = null;
}

/**********************************************************/
/* Zusätzliche Methoden                                   */
/**********************************************************/
public ResultSet executeQuery (Object[] parameters) throws SQLException{
	
	if ((parameters != null) && (this.paraTypes != null) && (parameters.length == this.paraTypes.length)){
		
		stmt.clearParameters();
		
		for ( int i=0 ; i<parameters.length; i++){
			switch (paraTypes[i]){
				case Types.ARRAY:
							;
				case Types.BIGINT:
							;
				case Types.BINARY:
							;
				case Types.BIT:
							;
				case Types.BLOB:
							;
				case Types.BOOLEAN:
							;
				case Types.CHAR:
							;
				case Types.CLOB:
							;
				case Types.DATALINK:
							;
				case Types.DATE:
							;
				case Types.DECIMAL:
							;
				case Types.DISTINCT:
							;
				case Types.DOUBLE:
							;
				case Types.FLOAT:
							{
								Float v = (Float)parameters[i];
								stmt.setFloat(i+1, v.floatValue());
								break;
							}
				case Types.INTEGER:
							{
								Integer v = (Integer)parameters[i];
								stmt.setInt(i+1, v.intValue());
								break;
							}
				case Types.JAVA_OBJECT:
							;
				case Types.LONGVARBINARY:
							;
				case Types.LONGVARCHAR:
							;
				case Types.NULL:
							{
								stmt.setNull(i+1, Types.NULL);
								break;
							}
				case Types.NUMERIC:
						;
				case Types.OTHER:
						;
				case Types.REAL:
						;
				case Types.REF:
						;
				case Types.SMALLINT:
						;
				case Types.STRUCT:
						;
				case Types.TIME:
						;
				case Types.TIMESTAMP:
						;
				case Types.TINYINT:
						;
				case Types.VARBINARY:
						;
				case Types.VARCHAR:
						{
							String v = parameters[i].toString();
							stmt.setString(i+1, v);
							break;
						}					
			}
		}
		
		return stmt.executeQuery();
	}else{
		throw new SQLException("Invalid number of parameters");	
	}
}
public int executeUpdate (Object[] parameters) throws SQLException{
	
	if ((parameters != null) && (this.paraTypes != null) && (parameters.length == this.paraTypes.length)){
		
		stmt.clearParameters();
		
		for ( int i=0 ; i<parameters.length; i++){
			switch (paraTypes[i]){
				case Types.ARRAY:
							;
				case Types.BIGINT:
							;
				case Types.BINARY:
							;
				case Types.BIT:
							;
				case Types.BLOB:
							;
				case Types.BOOLEAN:
							;
				case Types.CHAR:
							;
				case Types.CLOB:
							;
				case Types.DATALINK:
							;
				case Types.DATE:
							{
								Date v = (Date)parameters[i];
								stmt.setDate(i+1, v);
								break;
							}
				case Types.DECIMAL:
							;
				case Types.DISTINCT:
							;
				case Types.DOUBLE:
							;
				case Types.FLOAT:
							{
								Float v = (Float)parameters[i];
								stmt.setFloat(i+1, v.floatValue());
								break;
							}
				case Types.INTEGER:
							{
								Integer v = (Integer)parameters[i];
								stmt.setInt(i+1, v.intValue());
								break;
							}
				case Types.JAVA_OBJECT:
							;
				case Types.LONGVARBINARY:
							;
				case Types.LONGVARCHAR:
							;
				case Types.NULL:
							;
				case Types.NUMERIC:
							{
								stmt.setNull(i+1, Types.NULL);	
								break;
							}
				case Types.OTHER:
						;
				case Types.REAL:
						;
				case Types.REF:
						;
				case Types.SMALLINT:
						;
				case Types.STRUCT:
						;
				case Types.TIME:
						;
				case Types.TIMESTAMP:
						;
				case Types.TINYINT:
						;
				case Types.VARBINARY:
						;
				case Types.VARCHAR:
						{
							String v = parameters[i].toString();
							stmt.setString(i+1, v);	
							break;
						}					
			}
		}
		
		return stmt.executeUpdate();
	}else{
		throw new SQLException("Invalid number of parameters: " + this.toString());	
	}
}



/**********************************************************/
/* Wrappermethoden                                        */
/**********************************************************/
public void addBatch() throws SQLException{
	stmt.addBatch();
}

public void addBatch(String sql) throws SQLException{
	stmt.addBatch(sql);
}

public void cancel() throws SQLException{
	stmt.cancel();
}

public void clearBatch() throws SQLException{
	stmt.clearBatch();
}

public void clearParameters() throws SQLException{
	stmt.clearParameters();	 
}

public void clearWarnings() throws SQLException{
	stmt.clearWarnings();	 
}

public void close() throws SQLException{
	stmt.close();
}

public boolean execute() throws SQLException{
	return stmt.execute();	 
}

public boolean execute(String sql) throws SQLException{
	return stmt.execute(sql);	 
}

public boolean execute(String sql, int autoGeneratedKeys) throws SQLException{
	return stmt.execute(sql, autoGeneratedKeys);
}

public boolean execute(String sql, int[] columnIndexes) throws SQLException{
	return stmt.execute(sql, columnIndexes);
}

public boolean execute(String sql, String[] columnNames) throws SQLException{
	return stmt.execute(sql, columnNames);
}

public int[] executeBatch() throws SQLException{
	return stmt.executeBatch();	 
}

public ResultSet executeQuery() throws SQLException{
	return stmt.executeQuery(); 
}
	
public ResultSet executeQuery(String sql) throws SQLException{
	return stmt.executeQuery(sql); 
}

public int executeUpdate() throws SQLException{
	return stmt.executeUpdate();	 
}

public int executeUpdate(String sql) throws SQLException{
	return stmt.executeUpdate(sql);	 
}

public int executeUpdate(String sql, int autoGeneratedKeys) throws SQLException{
	return stmt.executeUpdate(sql, autoGeneratedKeys);
}

public int executeUpdate(String sql, int[] columnIndexes) throws SQLException{
	return stmt.executeUpdate(sql, columnIndexes);
}

public int executeUpdate(String sql, String[] columnNames) throws SQLException{
	return stmt.executeUpdate(sql, columnNames);
}

public Connection getConnection() throws SQLException{
	return stmt.getConnection();
}

public int getFetchDirection() throws SQLException{
	return stmt.getFetchDirection();
}

public int getFetchSize() throws SQLException{
	return stmt.getFetchSize();
}

public ResultSet getGeneratedKeys() throws SQLException{
	return stmt.getGeneratedKeys();
}

public int getMaxFieldSize() throws SQLException{
	return stmt.getMaxFieldSize();
}

public int getMaxRows() throws SQLException{
	return stmt.getMaxRows();
}

public ResultSetMetaData getMetaData() throws SQLException{
	return stmt.getMetaData(); 
}

public boolean getMoreResults() throws SQLException{
	return stmt.getMoreResults();
}

public boolean getMoreResults(int current) throws SQLException{
	return stmt.getMoreResults();
}
		 
public ParameterMetaData getParameterMetaData() throws SQLException{
	return stmt.getParameterMetaData(); 
}

public int getQueryTimeout() throws SQLException{
	return stmt.getQueryTimeout();
}

public ResultSet getResultSet() throws SQLException{
	return stmt.getResultSet();
}

public int getResultSetConcurrency() throws SQLException{
	return stmt.getResultSetConcurrency();
}

public int getResultSetHoldability() throws SQLException{
	return stmt.getResultSetHoldability();
}

public int getResultSetType() throws SQLException {
	return stmt.getResultSetType();
}

public int getUpdateCount() throws SQLException{
	return stmt.getUpdateCount();
}

public SQLWarning getWarnings() throws SQLException{
	return stmt.getWarnings();
}

public void setArray(int i, Array x) throws SQLException{
	stmt.setArray(i, x); 
}

public void setAsciiStream(int parameterIndex, InputStream x, int length) throws SQLException{
	stmt.setAsciiStream(parameterIndex, x, length); 
}

public void setBigDecimal(int parameterIndex, BigDecimal x) throws SQLException{
	stmt.setBigDecimal(parameterIndex, x); 
}

public void setBinaryStream(int parameterIndex, InputStream x, int length) throws SQLException{ 
	stmt.setBinaryStream(parameterIndex, x, length);
}

public void setBlob(int i, Blob x) throws SQLException{
	stmt.setBlob(i, x); 
}

public void setBoolean(int parameterIndex, boolean x) throws SQLException{ 
	stmt.setBoolean(parameterIndex, x);
}

public void setByte(int parameterIndex, byte x) throws SQLException{
	stmt.setByte(parameterIndex, x);
} 

public void setBytes(int parameterIndex, byte[] x) throws SQLException{ 
	stmt.setBytes(parameterIndex, x);
}

public void setCharacterStream(int parameterIndex, Reader reader, int length) throws SQLException{ 
	stmt.setCharacterStream(parameterIndex, reader, length);
}

public void setClob(int i, Clob x) throws SQLException{
	stmt.setClob(i, x);	 
}

public void setCursorName(String name) throws SQLException{
	stmt.setCursorName(name);
}

public void setDate(int parameterIndex, Date x) throws SQLException{
	stmt.setDate(parameterIndex, x); 
}

public void setDate(int parameterIndex, Date x, Calendar cal) throws SQLException{ 
	stmt.setDate(parameterIndex, x, cal);
}

public void setDouble(int parameterIndex, double x) throws SQLException{
	stmt.setDouble(parameterIndex, x);
}

public void setEscapeProcessing(boolean enable) throws SQLException{
	stmt.setEscapeProcessing(enable);
}

public void setFetchDirection(int direction) throws SQLException{
	stmt.setFetchDirection(direction);
}

public void setFetchSize(int rows) throws SQLException{
	stmt.setFetchSize(rows);
}

public void setFloat(int parameterIndex, float x) throws SQLException{ 
	stmt.setFloat(parameterIndex, x);
}

public void setInt(int parameterIndex, int x) throws SQLException{ 
	stmt.setInt(parameterIndex, x);
}

public void setLong(int parameterIndex, long x) throws SQLException{ 
	stmt.setLong(parameterIndex, x);
}

public void setMaxFieldSize(int max) throws SQLException{
	stmt.setMaxFieldSize(max);
}

public void setMaxRows(int max) throws SQLException{
	stmt.setMaxRows(max);
}

public void setNull(int parameterIndex, int sqlType) throws SQLException{ 
	stmt.setNull(parameterIndex,sqlType);
}

public void setNull(int paramIndex, int sqlType, String typeName) throws SQLException{ 
	stmt.setNull(paramIndex, sqlType, typeName);
}

public void setObject(int parameterIndex, Object x) throws SQLException{ 
	stmt.setObject(parameterIndex, x);
}

public void setObject(int parameterIndex, Object x, int targetSqlType) throws SQLException{ 
	stmt.setObject(parameterIndex, x, targetSqlType);
}

public void setObject(int parameterIndex, Object x, int targetSqlType, int scale) throws SQLException{ 
	stmt.setObject(parameterIndex, x, targetSqlType, scale);
}

public void setQueryTimeout(int seconds) throws SQLException {
	stmt.setQueryTimeout(seconds);
}

public void setRef(int i, Ref x) throws SQLException{ 
	stmt.setRef(i, x);
}

public void setShort(int parameterIndex, short x) throws SQLException{ 
	stmt.setShort(parameterIndex, x);
}

public void setString(int parameterIndex, String x) throws SQLException{ 
	stmt.setString(parameterIndex, x);
}

public void setTime(int parameterIndex, Time x) throws SQLException{ 
	stmt.setTime(parameterIndex, x);
}

public void setTime(int parameterIndex, Time x, Calendar cal) throws SQLException{ 
	stmt.setTime(parameterIndex, x, cal);
}

public void setTimestamp(int parameterIndex, Timestamp x) throws SQLException{ 
	stmt.setTimestamp(parameterIndex, x);
}

public void setTimestamp(int parameterIndex, Timestamp x, Calendar cal) throws SQLException{ 
	stmt.setTimestamp(parameterIndex, x, cal);
}

public void setUnicodeStream(int parameterIndex, InputStream x, int length) throws SQLException{ 
	stmt.setUnicodeStream(parameterIndex, x, length);
}

public void setURL(int parameterIndex, URL x) throws SQLException{ 
	stmt.setURL(parameterIndex, x);
}

/**********************************************************/
/* Parametermethoden                                      */
/**********************************************************/
	public int getParameterCount(){
		return paraTypes.length;
	}

	public int getParameterType(int parameter){
		if ( (parameter < 1) || (parameter >= paraTypes.length) )
			return 0;
		else
			return paraTypes[parameter - 1];
	}
/**********************************************************/
/* Nicht implementierte Parametermethoden                 */
/**********************************************************/
	public int isNullable(int param){
		return 0;
	}

	public boolean isSigned(int param){
		return false;
	}

	public int getPrecision(int param){
		return 0;
	}

	public int getScale(int param){
		return 0;
	}

	public String getParameterTypeName(int param){
		return null;
	}

	public String getParameterClassName(int param){
		return null;
	}

	public int getParameterMode(int param){
		return 0;
	}
/**********************************************************/

	public static void main(String[] args) {
	}

}