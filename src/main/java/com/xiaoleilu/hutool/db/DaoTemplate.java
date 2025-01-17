package com.xiaoleilu.hutool.db;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

import javax.sql.DataSource;

import com.xiaoleilu.hutool.db.handler.EntityListHandler;
import com.xiaoleilu.hutool.util.CollectionUtil;
import com.xiaoleilu.hutool.util.StrUtil;
import com.xiaoleilu.hutool.db.handler.EntityHandler;

/**
 * 数据访问层模板<br>
 * 此模板用于简化对指定表的操作，简化的操作如下：<br>
 * 1、在初始化时指定了表名，CRUD操作时便不需要表名<br>
 * 2、在初始化时指定了主键，某些需要主键的操作便不需要指定主键类型
 * @author Looly
 *
 */
public class DaoTemplate {
	protected SqlRunner runner;
	
	/** 表名 */
	protected String tableName;
	/** 本表的主键字段，请在子类中覆盖或构造方法中指定，默认为id */
	protected String primaryKeyField = "id";
	
	//--------------------------------------------------------------- Constructor start
	/**
	 * 构造，此构造需要自定义SqlRunner，主键默认为id
	 * @param tableName 数据库表名
	 */
	public DaoTemplate(String tableName) {
		this.tableName = tableName;
	}
	
	/**
	 * 构造，此构造需要自定义SqlRunner
	 * @param tableName 数据库表名
	 * @param primaryKeyField 主键字段名
	 */
	public DaoTemplate(String tableName, String primaryKeyField) {
		this.tableName = tableName;
		this.primaryKeyField = primaryKeyField;
	}
	
	/**
	 * 构造
	 * @param tableName 表名
	 * @param primaryKeyField 主键字段名
	 * @param ds 数据源
	 */
	public DaoTemplate(String tableName, String primaryKeyField, DataSource ds) {
		this(tableName, primaryKeyField, DbUtil.newSqlRunner(ds));
	}
	
	/**
	 * 构造
	 * @param tableName 表名
	 * @param primaryKeyField 主键字段名
	 * @param runner SqlRunner对象
	 */
	public DaoTemplate(String tableName, String primaryKeyField, SqlRunner runner) {
		this.tableName = tableName;
		this.primaryKeyField = primaryKeyField;
		this.runner = runner;
	}
	//--------------------------------------------------------------- Constructor end
	
	//------------------------------------------------------------- Add start
	/**
	 * 添加
	 * @param entity 实体对象
	 * @return 插入行数
	 * @throws SQLException
	 */
	public int add(Entity entity) throws SQLException {
		return runner.insert(fixEntity(entity));
	}
	
	/**
	 * 添加
	 * @param entity 实体对象
	 * @return 主键列表
	 * @throws SQLException
	 */
	public List<Object> addForGeneratedKeys(Entity entity) throws SQLException {
		return runner.insertForGeneratedKeys(fixEntity(entity));
	}
	
	/**
	 * 添加
	 * @param entity 实体对象
	 * @return 自增主键
	 * @throws SQLException
	 */
	public Long addForGeneratedKey(Entity entity) throws SQLException {
		return runner.insertForGeneratedKey(fixEntity(entity));
	}
	//------------------------------------------------------------- Add end
	
	//------------------------------------------------------------- Delete start
	/**
	 * 删除
	 * @param <T> 主键类型
	 * 
	 * @param pk 主键
	 * @return 删除行数
	 * @throws SQLException
	 */
	public <T> int del(T pk) throws SQLException {
		if (pk == null) {
			return 0;
		}
		return this.del(Entity.create(tableName).set(primaryKeyField, pk));
	}
	
	/**
	 * 删除
	 * @param <T> 主键类型
	 * 
	 * @param field 字段名
	 * @param value 字段值
	 * @return 删除行数
	 * @throws SQLException
	 */
	public <T> int del(String field, T value) throws SQLException {
		if (StrUtil.isBlank(field)) {
			return 0;
		}

		return this.del(Entity.create(tableName).set(field, value));
	}
	
	/**
	 * 删除
	 * @param <T> 主键类型
	 * 
	 * @param where 删除条件，当条件为空时，返回0（防止误删全表）
	 * @return 删除行数
	 * @throws SQLException
	 */
	public <T> int del(Entity where) throws SQLException {
		if (CollectionUtil.isEmpty(where)) {
			return 0;
		}
		return runner.del(fixEntity(where));
	}
	//------------------------------------------------------------- Delete end
	
	//------------------------------------------------------------- Update start
	/**
	 * 按照条件更新
	 * @param record 更新的内容
	 * @param where 条件
	 * @return 更新条目数
	 * @throws SQLException
	 */
	public int update(Entity record, Entity where) throws SQLException{
		if (CollectionUtil.isEmpty(record)) {
			return 0;
		}
		return runner.update(fixEntity(record), where);
	}
	
	/**
	 * 更新
	 * @param entity 实体对象，必须包含主键
	 * @return 更新行数
	 * @throws SQLException
	 */
	public int update(Entity entity) throws SQLException {
		if (CollectionUtil.isEmpty(entity)) {
			return 0;
		}
		entity = fixEntity(entity);
		Object pk = entity.get(primaryKeyField);
		if (null == pk) {
			throw new SQLException(StrUtil.format("Please determine `{}` for update", primaryKeyField));
		}

		final Entity where = Entity.create(tableName).set(primaryKeyField, pk);
		final Entity record = (Entity) entity.clone();
		record.remove(primaryKeyField);

		return runner.update(record, where);
	}
	
	/**
	 * 增加或者更新实体
	 * @param entity 实体，当包含主键时更新，否则新增
	 * @return 新增或更新条数
	 * @throws SQLException
	 */
	public int addOrUpdate(Entity entity) throws SQLException {
		return null == entity.get(primaryKeyField) ? add(entity) : update(entity);
	}
	//------------------------------------------------------------- Update end
	
	//------------------------------------------------------------- Get start
	/**
	 * 根据主键获取单个记录
	 * @param <T>
	 * 
	 * @param pk 主键值
	 * @return 记录
	 * @throws SQLException
	 */
	public <T> Entity get(T pk) throws SQLException {
		return this.get(primaryKeyField, pk);
	}
	
	/**
	 * 根据某个字段（最好是唯一字段）查询单个记录<br>
	 * 当有多条返回时，只显示查询到的第一条
	 * @param <T>
	 * 
	 * @param field 字段名
	 * @param value 字段值
	 * @return 记录
	 * @throws SQLException
	 */
	public <T> Entity get(String field, T value) throws SQLException {
		return this.get(Entity.create(tableName).set(field, value));
	}
	
	/**
	 * 根据条件实体查询单个记录，当有多条返回时，只显示查询到的第一条
	 * 
	 * @param where 条件
	 * @return 记录
	 * @throws SQLException
	 */
	public Entity get(Entity where) throws SQLException {
		return runner.find(null, fixEntity(where), new EntityHandler());
	}
	//------------------------------------------------------------- Get end
	
	//------------------------------------------------------------- Find start
	/**
	 * 根据某个字段值查询结果
	 * @param <T>
	 * 
	 * @param field 字段名
	 * @param value 字段值
	 * @return 记录
	 * @throws SQLException
	 */
	public <T> List<Entity> find(String field, T value) throws SQLException {
		return this.find(Entity.create(tableName).set(field, value));
	}
	
	/**
	 * 查询当前表的所有记录
	 * @return 记录
	 * @throws SQLException
	 */
	public List<Entity> findAll() throws SQLException {
		return this.find(Entity.create(tableName));
	}
	
	/**
	 * 根据某个字段值查询结果
	 * 
	 * @param where 查询条件
	 * @return 记录
	 * @throws SQLException
	 */
	public List<Entity> find(Entity where) throws SQLException {
		return runner.find(null, fixEntity(where), new EntityListHandler());
	}
	
	/**
	 * 根据SQL语句查询结果<br>
	 * SQL语句可以是非完整SQL语句，可以只提供查询的条件部分（例如WHERE部分）<br>
	 * 此方法会自动补全SELECT * FROM [tableName] 部分，这样就无需关心表名，直接提供条件即可
	 * 
	 * @param sql SQL语句
	 * @return 记录
	 * @throws SQLException
	 */
	public List<Entity> findBySql(String sql, Object... params) throws SQLException {
		String selectKeyword = StrUtil.subPre(sql.trim(), 6).toLowerCase();
		if(false == "select".equals(selectKeyword)){
			sql = "SELECT * FROM " + this.tableName + " " + sql;
		}
		return runner.query(sql, new EntityListHandler(), params);
	}
	
	/**
	 * 分页
	 * @param where 条件
	 * @param page 分页对象
	 * @param selectFields 查询的字段列表
	 * @return 分页结果集
	 * @throws SQLException
	 */
	public PageResult<Entity> page(Entity where, Page page, String... selectFields) throws SQLException{
		return runner.page(Arrays.asList(selectFields), fixEntity(where), page);
	}
	
	/**
	 * 分页
	 * @param where 条件
	 * @param page 分页对象
	 * @return 分页结果集
	 * @throws SQLException
	 */
	public PageResult<Entity> page(Entity where, Page page) throws SQLException{
		return runner.page(fixEntity(where), page);
	}
	
	/**
	 * 满足条件的数据条目数量
	 * @param where 条件
	 * @return 数量
	 * @throws SQLException
	 */
	public int count(Entity where) throws SQLException{
		return runner.count(fixEntity(where));
	}
	
	/**
	 * 指定条件的数据是否存在
	 * @param where 条件
	 * @return 是否存在
	 * @throws SQLException
	 */
	public boolean exist(Entity where) throws SQLException{
		return this.count(where) > 0;
	}
	//------------------------------------------------------------- Find end
	
	/**
	 * 修正Entity对象，避免null和填充表名
	 * @param entity 实体类
	 * @return 修正后的实体类
	 */
	private Entity fixEntity(Entity entity){
		if(null == entity){
			entity = Entity.create(tableName);
		}else if(StrUtil.isBlank(entity.getTableName())){
			entity.setTableName(tableName);
		}
		return entity;
	}
}
