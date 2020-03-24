/**
 * Project: Coral
 * Source file: ByteComputeUtil.java
 * Create At 2013-12-16 下午03:31:41
 * Create By 龚云
 */
package coral.base.util;

import java.io.UnsupportedEncodingException;
import java.util.Date;

import coral.base.util.BinaryHelper.BinaryUnit;

/**
 * @author 龚云
 * 
 */
public class ObjectSizeUtils {

	public static long sizeofDate(Date date, BinaryUnit unit) {
		long size = date == null ? 0 : sizeofLong(unit);
		return unit.convert(size, unit.getHelper().BIT);
	}

	public static long sizeofLong(BinaryUnit unit) {
		return unit.convert(NumberType.LONG.getSize(), unit.getHelper().BIT);
	}

	public static long sizeofNumber(NumberType type, BinaryUnit unit) {
		return unit.convert(type.getSize(), unit.getHelper().BIT);
	}

	public static long sizeofString(String s, String charset, BinaryUnit unit) {
		try {
			long size = s == null ? 0 : s.getBytes(charset).length * 8;
			return unit.convert(size, unit.getHelper().BIT);
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException(e);
		}
	}

	public static enum NumberType {
		BYTE(Byte.SIZE),

		SHORT(Short.SIZE),

		CHAR(Character.SIZE),

		INT(Integer.SIZE),

		FLOAT(Float.SIZE),

		LONG(Long.SIZE),

		DOUBLE(Double.SIZE),

		// may be 8 bit
		BOOLEAN(Byte.SIZE);

		private NumberType(int size) {
			this.size = size;
		}

		public int getSize() {
			return size;
		}

		private int size;// bit size
	}
}
