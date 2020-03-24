/**
 * Project: Coral
 * Source file: BinaryUnit.java
 * Create At 2013-12-19 上午09:38:10
 * Create By 龚云
 */
package coral.base.util;

import java.math.BigDecimal;
import java.text.DecimalFormat;

/**
 * @author 龚云
 * 
 */
public class BinaryHelper {

	public static BinaryHelper getInstance() {
		return DEFAULT;
	}

	public static BinaryHelper getInstance(int timesUnit) {
		return new BinaryHelper(timesUnit);
	}

	protected void setTimesUnit(int timesUnit) {
		init(timesUnit);
	}

	private BinaryHelper() {
		init(1024);
	}

	private BinaryHelper(int timesUnit) {
		init(timesUnit);
	}

	private void init(int timesUnit) {
		BIT.setTimesUnit(timesUnit);
		BYTE.setTimesUnit(timesUnit);
		KB.setTimesUnit(timesUnit);
		MB.setTimesUnit(timesUnit);
		GB.setTimesUnit(timesUnit);
		TB.setTimesUnit(timesUnit);
		PB.setTimesUnit(timesUnit);
	}

	private static final BinaryHelper DEFAULT = new BinaryHelper();

	public final BinaryUnit BIT = new Bit().setHelper(this);
	public final BinaryUnit BYTE = new Byte().setHelper(this);
	public final BinaryUnit KB = new KByte().setHelper(this);
	public final BinaryUnit MB = new MByte().setHelper(this);
	public final BinaryUnit GB = new GByte().setHelper(this);
	public final BinaryUnit TB = new TByte().setHelper(this);
	public final BinaryUnit PB = new PByte().setHelper(this);

	public static abstract class BinaryUnit {

		abstract public long toBit(long d);

		abstract public long toByte(long d);

		abstract public long toKB(long d);

		abstract public long toMB(long d);

		abstract public long toGB(long d);

		abstract public long toTB(long d);

		abstract public long toPB(long d);

		abstract public long convert(long d, BinaryUnit source);

		abstract public double toBit(double d);

		abstract public double toByte(double d);

		abstract public double toKB(double d);

		abstract public double toMB(double d);

		abstract public double toGB(double d);

		abstract public double toTB(double d);

		abstract public double toPB(double d);

		abstract public double convert(double d, BinaryUnit source);

		abstract public BigDecimal toBit(BigDecimal d);

		abstract public BigDecimal toByte(BigDecimal d);

		abstract public BigDecimal toKB(BigDecimal d);

		abstract public BigDecimal toMB(BigDecimal d);

		abstract public BigDecimal toGB(BigDecimal d);

		abstract public BigDecimal toTB(BigDecimal d);

		abstract public BigDecimal toPB(BigDecimal d);

		abstract public BigDecimal convert(BigDecimal d, BinaryUnit source);

		public BinaryHelper getHelper() {
			return helper;
		}

		public String toBit(double d, String pattern) {
			return new DecimalFormat(pattern).format(toBit(d));
		}

		public String toByte(double d, String pattern) {
			return new DecimalFormat(pattern).format(toByte(d));
		}

		public String toKB(double d, String pattern) {
			return new DecimalFormat(pattern).format(toKB(d));
		}

		public String toMB(double d, String pattern) {
			return new DecimalFormat(pattern).format(toMB(d));
		}

		public String toGB(double d, String pattern) {
			return new DecimalFormat(pattern).format(toGB(d));
		}

		public String toTB(double d, String pattern) {
			return new DecimalFormat(pattern).format(toTB(d));
		}

		public String toPB(double d, String pattern) {
			return new DecimalFormat(pattern).format(toPB(d));
		}

		public String toBit(long d, String pattern) {
			return new DecimalFormat(pattern).format(toBit(d));
		}

		public String toByte(long d, String pattern) {
			return new DecimalFormat(pattern).format(toByte(d));
		}

		public String toKB(long d, String pattern) {
			return new DecimalFormat(pattern).format(toKB(d));
		}

		public String toMB(long d, String pattern) {
			return new DecimalFormat(pattern).format(toMB(d));
		}

		public String toGB(long d, String pattern) {
			return new DecimalFormat(pattern).format(toGB(d));
		}

		public String toTB(long d, String pattern) {
			return new DecimalFormat(pattern).format(toTB(d));
		}

		public String toPB(long d, String pattern) {
			return new DecimalFormat(pattern).format(toPB(d));
		}

		public String toBit(BigDecimal d, String pattern) {
			return new DecimalFormat(pattern).format(toBit(d));
		}

		public String toByte(BigDecimal d, String pattern) {
			return new DecimalFormat(pattern).format(toByte(d));
		}

		public String toKB(BigDecimal d, String pattern) {
			return new DecimalFormat(pattern).format(toKB(d));
		}

		public String toMB(BigDecimal d, String pattern) {
			return new DecimalFormat(pattern).format(toMB(d));
		}

		public String toGB(BigDecimal d, String pattern) {
			return new DecimalFormat(pattern).format(toGB(d));
		}

		public String toTB(BigDecimal d, String pattern) {
			return new DecimalFormat(pattern).format(toTB(d));
		}

		public String toPB(BigDecimal d, String pattern) {
			return new DecimalFormat(pattern).format(toPB(d));
		}

		public String toAutoSize(long d, String pattern) {
			long abs = d < 0 ? -1 * d : d;
			abs = toBit(abs);
			if (abs < _B)
				return abs + "  b";
			if (abs < _KB)
				return toByte(d, pattern) + "  B";
			if (abs < _MB)
				return toKB(d, pattern) + " KB";
			if (abs < _GB)
				return toMB(d, pattern) + " MB";
			if (abs < _TB)
				return toGB(d, pattern) + " GB";
			if (abs < _PB)
				return toTB(d, pattern) + " TB";
			return toPB(d, pattern) + " PB";
		}

		public String toAutoSize(double d, String pattern) {
			double abs = d < 0 ? -1 * d : d;
			abs = toBit(abs);
			if (abs < _B)
				return abs + "  b";
			if (abs < _KB)
				return toByte(d, pattern) + "  B";
			if (abs < _MB)
				return toKB(d, pattern) + " KB";
			if (abs < _GB)
				return toMB(d, pattern) + " MB";
			if (abs < _TB)
				return toGB(d, pattern) + " GB";
			if (abs < _PB)
				return toTB(d, pattern) + " TB";
			return toPB(d, pattern) + " PB";
		}

		public String toAutoSize(BigDecimal d, String pattern) {
			BigDecimal abs = d.compareTo(BigDecimal.ZERO) == -1 ? d.abs() : d;
			abs = toBit(abs);
			if (abs.compareTo(__B) == -1)
				return abs + "  b";
			if (abs.compareTo(__KB) == -1)
				return toByte(d, pattern) + "  B";
			if (abs.compareTo(__MB) == -1)
				return toKB(d, pattern) + " KB";
			if (abs.compareTo(__GB) == -1)
				return toMB(d, pattern) + " MB";
			if (abs.compareTo(__TB) == -1)
				return toGB(d, pattern) + " GB";
			if (abs.compareTo(__PB) == -1)
				return toTB(d, pattern) + " TB";
			return toPB(d, pattern) + " PB";
		}

		protected void setTimesUnit(int timesUnit) {
			_timesUnit = timesUnit;
			__timesUnit = new BigDecimal(timesUnit);

			_KB = _B * _timesUnit;
			_MB = _KB * _timesUnit;
			_GB = _MB * _timesUnit;
			_TB = _GB * _timesUnit;
			_PB = _TB * _timesUnit;

			__KB = __B.multiply(__timesUnit);
			__MB = __KB.multiply(__timesUnit);
			__GB = __MB.multiply(__timesUnit);
			__TB = __GB.multiply(__timesUnit);
			__PB = __TB.multiply(__timesUnit);
		}

		protected BinaryUnit setHelper(BinaryHelper helper) {
			this.helper = helper;
			return this;
		}

		protected int _timesUnit = 1024;
		protected long _b = 1;
		protected long _B = _b * 8L;
		protected long _KB = _B * _timesUnit;
		protected long _MB = _KB * _timesUnit;
		protected long _GB = _MB * _timesUnit;
		protected long _TB = _GB * _timesUnit;
		protected long _PB = _TB * _timesUnit;

		protected BigDecimal _n8 = new BigDecimal(8);
		protected BigDecimal __timesUnit = new BigDecimal(1024);
		protected BigDecimal __b = BigDecimal.ONE;
		protected BigDecimal __B = __b.multiply(_n8);
		protected BigDecimal __KB = __B.multiply(__timesUnit);
		protected BigDecimal __MB = __KB.multiply(__timesUnit);
		protected BigDecimal __GB = __MB.multiply(__timesUnit);
		protected BigDecimal __TB = __GB.multiply(__timesUnit);
		protected BigDecimal __PB = __TB.multiply(__timesUnit);

		private BinaryHelper helper;
	}

	static class Bit extends BinaryUnit {

		@Override
		public long toBit(long d) {
			return d;
		}

		@Override
		public long toByte(long d) {
			return toBit(d) / _B;
		}

		@Override
		public long toKB(long d) {
			return toBit(d) / _KB;
		}

		@Override
		public long toMB(long d) {
			return toBit(d) / _MB;
		}

		@Override
		public long toGB(long d) {
			return toBit(d) / _GB;
		}

		@Override
		public long toTB(long d) {
			return toBit(d) / _TB;
		}

		@Override
		public long toPB(long d) {
			return toBit(d) / _PB;
		}

		@Override
		public double toBit(double d) {
			return d;
		}

		@Override
		public double toByte(double d) {
			return toBit(d) / _B;
		}

		@Override
		public double toKB(double d) {
			return toBit(d) / _KB;
		}

		@Override
		public double toMB(double d) {
			return toBit(d) / _MB;
		}

		@Override
		public double toGB(double d) {
			return toBit(d) / _GB;
		}

		@Override
		public double toTB(double d) {
			return toBit(d) / _TB;
		}

		@Override
		public double toPB(double d) {
			return toBit(d) / _PB;
		}

		@Override
		public BigDecimal toBit(BigDecimal d) {
			return d;
		}

		public BigDecimal toByte(BigDecimal d) {
			return d.divide(__B);
		}

		@Override
		public BigDecimal toKB(BigDecimal d) {
			return d.divide(__KB);
		}

		@Override
		public BigDecimal toMB(BigDecimal d) {
			return d.divide(__MB);
		}

		@Override
		public BigDecimal toGB(BigDecimal d) {
			return d.divide(__GB);
		}

		@Override
		public BigDecimal toTB(BigDecimal d) {
			return d.divide(__TB);
		}

		@Override
		public BigDecimal toPB(BigDecimal d) {
			return d.divide(__PB);
		}

		@Override
		public long convert(long d, BinaryUnit source) {
			return source.toBit(d);
		}

		@Override
		public double convert(double d, BinaryUnit source) {
			return source.toBit(d);
		}

		@Override
		public BigDecimal convert(BigDecimal d, BinaryUnit source) {
			return source.toBit(d);
		}

	}

	static class Byte extends Bit {
		@Override
		public long toBit(long d) {
			return d * _B;
		}

		@Override
		public long toByte(long d) {
			return d;
		}

		@Override
		public double toBit(double d) {
			return d * _B;
		}

		@Override
		public double toByte(double d) {
			return d;
		}

		@Override
		public BigDecimal toBit(BigDecimal d) {
			return d.multiply(__B);
		}

		@Override
		public BigDecimal toByte(BigDecimal d) {
			return d;
		}

		@Override
		public long convert(long d, BinaryUnit source) {
			return source.toByte(d);
		}

		@Override
		public double convert(double d, BinaryUnit source) {
			return source.toByte(d);
		}

		@Override
		public BigDecimal convert(BigDecimal d, BinaryUnit source) {
			return source.toByte(d);
		}
	}

	static class KByte extends Bit {
		@Override
		public long toBit(long d) {
			return d * _KB;
		}

		@Override
		public long toKB(long d) {
			return d;
		}

		@Override
		public double toBit(double d) {
			return d * _KB;
		}

		@Override
		public double toKB(double d) {
			return d;
		}

		@Override
		public BigDecimal toBit(BigDecimal d) {
			return d.multiply(__KB);
		}

		@Override
		public BigDecimal toKB(BigDecimal d) {
			return d;
		}

		@Override
		public long convert(long d, BinaryUnit source) {
			return source.toKB(d);
		}

		@Override
		public double convert(double d, BinaryUnit source) {
			return source.toKB(d);
		}

		@Override
		public BigDecimal convert(BigDecimal d, BinaryUnit source) {
			return source.toKB(d);
		}
	}

	static class MByte extends Bit {
		@Override
		public long toBit(long d) {
			return d * _MB;
		}

		@Override
		public long toMB(long d) {
			return d;
		}

		@Override
		public double toBit(double d) {
			return d * _MB;
		}

		@Override
		public double toMB(double d) {
			return d;
		}

		@Override
		public BigDecimal toBit(BigDecimal d) {
			return d.multiply(__MB);
		}

		@Override
		public BigDecimal toMB(BigDecimal d) {
			return d;
		}

		@Override
		public long convert(long d, BinaryUnit source) {
			return source.toMB(d);
		}

		@Override
		public double convert(double d, BinaryUnit source) {
			return source.toMB(d);
		}

		@Override
		public BigDecimal convert(BigDecimal d, BinaryUnit source) {
			return source.toMB(d);
		}
	}

	static class GByte extends Bit {
		@Override
		public long toBit(long d) {
			return d * _GB;
		}

		@Override
		public long toGB(long d) {
			return d;
		}

		@Override
		public double toBit(double d) {
			return d * _GB;
		}

		@Override
		public double toGB(double d) {
			return d;
		}

		@Override
		public BigDecimal toBit(BigDecimal d) {
			return d.multiply(__GB);
		}

		@Override
		public BigDecimal toGB(BigDecimal d) {
			return d;
		}

		@Override
		public long convert(long d, BinaryUnit source) {
			return source.toGB(d);
		}

		@Override
		public double convert(double d, BinaryUnit source) {
			return source.toGB(d);
		}

		@Override
		public BigDecimal convert(BigDecimal d, BinaryUnit source) {
			return source.toGB(d);
		}
	}

	static class TByte extends Bit {
		@Override
		public long toBit(long d) {
			return d * _TB;
		}

		@Override
		public long toTB(long d) {
			return d;
		}

		@Override
		public double toBit(double d) {
			return d * _TB;
		}

		@Override
		public double toTB(double d) {
			return d;
		}

		@Override
		public BigDecimal toBit(BigDecimal d) {
			return d.multiply(__TB);
		}

		@Override
		public BigDecimal toTB(BigDecimal d) {
			return d;
		}

		@Override
		public long convert(long d, BinaryUnit source) {
			return source.toTB(d);
		}

		@Override
		public double convert(double d, BinaryUnit source) {
			return source.toTB(d);
		}

		@Override
		public BigDecimal convert(BigDecimal d, BinaryUnit source) {
			return source.toTB(d);
		}
	}

	static class PByte extends Bit {
		@Override
		public long toBit(long d) {
			return d * _PB;
		}

		@Override
		public long toPB(long d) {
			return d;
		}

		@Override
		public double toBit(double d) {
			return d * _PB;
		}

		@Override
		public double toPB(double d) {
			return d;
		}

		@Override
		public BigDecimal toBit(BigDecimal d) {
			return d.multiply(__PB);
		}

		@Override
		public BigDecimal toPB(BigDecimal d) {
			return d;
		}

		@Override
		public long convert(long d, BinaryUnit source) {
			return source.toPB(d);
		}

		@Override
		public double convert(double d, BinaryUnit source) {
			return source.toPB(d);
		}

		@Override
		public BigDecimal convert(BigDecimal d, BinaryUnit source) {
			return source.toPB(d);
		}
	}

}
