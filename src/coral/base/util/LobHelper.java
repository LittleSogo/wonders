/**
 * Project: Coral
 * Source file: LobHelper.java
 * Create At 2014-1-17 下午05:10:52
 * Create By 龚云
 */
package coral.base.util;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import wfc.service.database.BlobHelper;
import wfc.service.database.DB;
import wfc.service.exception.InnerException;
import wfc.service.log.Log;

/**
 * @author 龚云
 * 
 */
public class LobHelper {

	public static int setBlob(Connection con, String strTable, String strField,
			String strCondition, InputStream is, long size, Object[] obj) {
		PreparedStatement ps = null;
		try {
			String strSQL = "UPDATE " + strTable + " SET " + strField
					+ "=? WHERE " + strCondition;
			DB.printSQL(strSQL, obj, BlobHelper.class);
			ps = con.prepareStatement(strSQL);
			if (is == null) {
				ps.setBinaryStream(1, null, 0);
			} else {
				ps.setBinaryStream(1, is, size);
			}
			DB.setFieldValue(ps, 2, obj);
			return ps.executeUpdate();
		} catch (SQLException ex) {
			throw new InnerException(ex);
		} finally {
			try {
				if (ps != null) {
					ps.close();
				}
			} catch (SQLException ex) {
				Log.error(ex);
			}
		}
	}

}
