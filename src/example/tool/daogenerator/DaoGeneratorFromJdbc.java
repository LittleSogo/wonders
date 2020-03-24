package example.tool.daogenerator;



import wfc.facility.tool.autocode.InternalDaoGenerator;
import wfc.facility.tool.autocode.TableXmlFromJdbc;

/**
 * dao层生成工具
 * 需要用到的jar包在lib里边，同时需要添加wfc_config.properties文件，里边配置数据库信息
 * 其中配置文件中autocode.table.xml.path路径一定需要指定，用来生成表的xml文件
 *
 *
 * 生成的dao，entity类需要手动添加@Id，dao层需要添加@Reposity ,
 * service实现类需要注入dao,提供getset方法，set方法上添加注解标志
 */
public class DaoGeneratorFromJdbc {

	public static void main(String[] args) {


		//String[] tables = {"t_user_role","t_permission","t_role","t_user","t_role_permission"};
		String[] tables ={"easyui_tree"};
		for (int i = 0; i <tables.length ; i++) {
			String table = tables[i];
			String[] param2 = { "src", "com.wonders.dao", "UTF-8", table };
			TableXmlFromJdbc.main(new String[]{table});

			InternalDaoGenerator.main(param2);
		}
		

	
		
	}
}