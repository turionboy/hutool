package com.xiaoleilu.hutool.getter;

import java.math.BigDecimal;
import java.math.BigInteger;

/**
 * 基本类型的getter接口抽象实现<br>
 * 提供一个统一的接口定义返回不同类型的值（基本类型）<br>
 * 在不提供默认值的情况下， 如果值不存在或获取错误，返回null<br>
 * 用户只需实现{@code com.xiaoleilu.hutool.getter.OptBasicTypeGetter}接口即可
 * @author Looly
 */
public abstract class OptNullBasicTypeGetter implements BasicTypeGetter, OptBasicTypeGetter{
	/**
	 * 获取字符串型属性值<br>
	 * 无值或获取错误返回null
	 * 
	 * @param key 属性名
	 * @return 属性值
	 */
	@Override
	public String getStr(String key){
		return this.getStr(key, null);
	}
	
	/**
	 * 获取int型属性值<br>
	 * 无值或获取错误返回null
	 * 
	 * @param key 属性名
	 * @return 属性值
	 */
	@Override
	public Integer getInt(String key) {
		return this.getInt(key, null);
	}
	
	/**
	 * 获取short型属性值<br>
	 * 无值或获取错误返回null
	 * 
	 * @param key 属性名
	 * @return 属性值
	 */
	@Override
	public Short getShort(String key){
		return this.getShort(key, null);
	}
	
	/**
	 * 获取boolean型属性值<br>
	 * 无值或获取错误返回null
	 * 
	 * @param key 属性名
	 * @return 属性值
	 */
	@Override
	public Boolean getBool(String key){
		return this.getBool(key, null);
	}
	
	/**
	 * 获取long型属性值<br>
	 * 无值或获取错误返回null
	 * 
	 * @param key 属性名
	 * @return 属性值
	 */
	@Override
	public Long getLong(String key){
		return this.getLong(key, null);
	}
	
	/**
	 * 获取char型属性值<br>
	 * 无值或获取错误返回null
	 * 
	 * @param key 属性名
	 * @return 属性值
	 */
	@Override
	public Character getChar(String key){
		return this.getChar(key, null);
	}
	
	/**
	 * 获取float型属性值<br>
	 * 无值或获取错误返回null
	 * 
	 * @param key 属性名
	 * @return 属性值
	 */
	@Override
	public Float getFloat(String key){
		return this.getFloat(key, null);
	}
	
	/**
	 * 获取double型属性值<br>
	 * 无值或获取错误返回null
	 * 
	 * @param key 属性名
	 * @return 属性值
	 */
	@Override
	public Double getDouble(String key){
		return this.getDouble(key, null);
	}
	
	/**
	 * 获取byte型属性值<br>
	 * 无值或获取错误返回null
	 * 
	 * @param key 属性名
	 * @return 属性值
	 */
	@Override
	public Byte getByte(String key){
		return this.getByte(key, null);
	}
	
	/**
	 * 获取BigDecimal型属性值<br>
	 * 无值或获取错误返回null
	 * 
	 * @param key 属性名
	 * @return 属性值
	 */
	@Override
	public BigDecimal getBigDecimal(String key){
		return this.getBigDecimal(key, null);
	}
	
	/**
	 * 获取BigInteger型属性值<br>
	 * 无值或获取错误返回null
	 * 
	 * @param key 属性名
	 * @return 属性值
	 */
	@Override
	public BigInteger getBigInteger(String key){
		return this.getBigInteger(key, null);
	}
}
