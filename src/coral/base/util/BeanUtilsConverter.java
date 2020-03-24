package coral.base.util;

import java.io.File;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.URL;
import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;

import javax.annotation.PostConstruct;

import org.apache.commons.beanutils.BeanUtilsBean;
import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.beanutils.converters.BigDecimalConverter;
import org.apache.commons.beanutils.converters.BigIntegerConverter;
import org.apache.commons.beanutils.converters.ClassConverter;
import org.apache.commons.beanutils.converters.FileConverter;
import org.apache.commons.beanutils.converters.SqlDateConverter;
import org.apache.commons.beanutils.converters.SqlTimeConverter;
import org.apache.commons.beanutils.converters.SqlTimestampConverter;
import org.apache.commons.beanutils.converters.URLConverter;
import org.springframework.stereotype.Component;

/**
 * commons-beanutils的帮助类
 * 
 * @author 龚云
 * 
 */
@Component
public class BeanUtilsConverter {

	/**
	 * 覆盖org.apache.commons.beanutils.ConvertUtils中以下几个Converter的默认值配置（设为null）：<br>
	 * 1.BigDecimalConverter<br>
	 * 2.BigIntegerConverter<br>
	 * 3.ClassConverter<br>
	 * 4.SqlDateConverter<br>
	 * 5.SqlTimeConverter<br>
	 * 6.SqlTimestampConverter<br>
	 * 7.FileConverter<br>
	 * 8.URLConverter<br>
	 */
	@PostConstruct
	public void init() {
		BeanUtilsBean.getInstance().getConvertUtils();
		ConvertUtils.register(new BigDecimalConverter(null), BigDecimal.class);
		ConvertUtils.register(new BigIntegerConverter(null), BigInteger.class);
		ConvertUtils.register(new ClassConverter(null), Class.class);
		ConvertUtils.register(new SqlDateConverter(null), Date.class);
		ConvertUtils.register(new SqlTimeConverter(null), Time.class);
		ConvertUtils.register(new SqlTimestampConverter(null), Timestamp.class);
		ConvertUtils.register(new FileConverter(null), File.class);
		ConvertUtils.register(new URLConverter(null), URL.class);
	}
}
