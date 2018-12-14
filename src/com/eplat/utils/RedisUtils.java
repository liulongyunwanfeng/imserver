package com.eplat.utils;

import org.apache.log4j.Logger;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.input.SAXBuilder;
import redis.clients.jedis.*;

import java.io.*;
import java.util.*;

/**
 * redis封装类
 * 
 * @author Administrator
 *
 */
public class RedisUtils {
	private static Logger logger = Logger.getLogger(RedisUtils.class);
	/**
	 * 最大活动链接数
	 */
	private static int MAX_ACTIVE = 10000;
	/**
	 * 最大空闲链接数
	 */
	private static int MAX_IDLE = 100;
	/**
	 * 最大等待时长
	 */
	private static int MAX_WAIT = 5000;
	/**
	 * 是否认证redis链接
	 */
	private static boolean TEST_ON_BORROW = true;

	//=========================liulongyun添加测试部分start============================//

	private static int MIN_IDEL=10;

	private static boolean TEST_ON_RETURN = true;
	//Idle时进行连接扫描
	private static boolean TEST_WHILE_IDLE =true;
	//表示idle object evitor两次扫描之间要sleep的毫秒数
	private static int TIME_BETWEEN_EVICTION_RUNS_MILLIS = 30000;
	//表示idle object evitor每次扫描的最多的对象数
	private static  int NUM_TEST_SPEREVICTION_RUN = 10;
	//表示一个对象至少停留在idle状态的最短时间，然后才能被idle object evitor扫描并驱逐；
	// 这一项只有在timeBetweenEvictionRunsMillis大于0时才有意义
	private static  int MINEVICTABLE_IDLE_TIME_MILLIS = 60000;

	//=========================liulongyun添加测试部分end============================//


	private static Set<HostAndPort> servers = new HashSet<HostAndPort>();
	/**
	 * jedis 连接池
	 */
	private static JedisPool pool = null;
	// 链接redis集群
	private static JedisCluster cluster = null;

	/**
	 * 设置值到缓存
	 * 
	 * @param key
	 * @param value
	 * @param timeout
	 * @throws Exception
	 */
	public static void setObj(String key, Object value, int timeout)
			throws Exception {
		set(key, serialize(value), timeout);
	}

	public static void setObj(String key, Object value) throws Exception {
		set(key, serialize(value), 0);
	}

	public static void set(String key, String value) throws Exception {
		set(key, value, 0);
	}

	/**
	 * 设置计数
	 * 
	 * @param key
	 * @param expire
	 * @throws Exception
	 */
	public static void inc(String key, int expire) throws Exception {
		if (cluster != null) {
			cluster.incr(key);
			cluster.expire(key, expire * 60 * 60);
		} else if (pool != null) {
			Jedis jedis = pool.getResource();
			try {
				jedis.incr(key);
				jedis.expire(key, expire * 60 * 60);
			} finally {
				if (jedis != null && pool != null) {
					pool.returnResourceObject(jedis);
				}
			}
		} else {
			throw new Exception("设置缓存异常：");
		}
	}

	public static void set(String key, String value, int timeout)
			throws Exception {
		if (cluster != null) {
			if (timeout == 0) {
				cluster.set(key, value);
			} else {
				cluster.setex(key, timeout, value);
			}

		} else if (pool != null) {
			Jedis jedis = pool.getResource();
			try {
				if (timeout == 0) {
					jedis.set(key, value);
				} else {
					jedis.setex(key, timeout, value);
				}

			} finally {
				if (jedis != null && pool != null) {
					pool.returnResourceObject(jedis);
				}
			}
		} else {
			throw new Exception("设置缓存异常：");
		}
	}

	public static void delete(String key) {
		if (cluster != null) {
			cluster.del(key);

		} else if (pool != null) {
			Jedis jedis = pool.getResource();
			try {
				jedis.del(key);
			} finally {
				if (jedis != null && pool != null) {
					pool.returnResourceObject(jedis);
				}
			}
		}
	}

	public static Object getObj(String key) throws Exception {
		String value = getString(key);
		if (value == null) {
			return null;
		} else {
			return unserialize(value);
		}
	}

	public static String getString(String key) {
		if (cluster != null) {
			return cluster.get(key);
		} else if (pool != null) {
			Jedis jedis = pool.getResource();
			try {
				return jedis.get(key);
			} finally {
				if (jedis != null && pool != null) {
					pool.returnResourceObject(jedis);
				}
			}
		}
		return null;
	}

	/**
	 * 设置列表值
	 * 
	 * @param key
	 * @param content
	 */
	public static void setStrs(String key, String... content) throws Exception {
		if (content == null || content.length == 0) {
			throw new Exception("批量设置值，不能为空！");
		}
		if (cluster != null) {
			cluster.sadd(key, content);
		} else if (pool != null) {
			Jedis jedis = pool.getResource();
			try {
				jedis.sadd(key, content);
			} finally {
				if (jedis != null && pool != null) {
					pool.returnResourceObject(jedis);
				}
			}
		} else {
			throw new Exception("设置缓存异常：");
		}
	}

	/**
	 * 设置Map值
	 * 
	 * @param key
	 * @param field
	 * @param content
	 * @throws Exception
	 */
	public static void setMap(String key, String field, String content)
			throws Exception {
		if (cluster != null) {
			cluster.hset(key, field, content);
		} else if (pool != null) {
			Jedis jedis = pool.getResource();
			try {
				jedis.hset(key, field, content);
			} finally {
				if (jedis != null && pool != null) {
					pool.returnResourceObject(jedis);
				}
			}
		} else {
			throw new Exception("设置缓存异常：");
		}
	}
	public static void setMapExp(String key, String field, String content,int timeout)
			throws Exception {
		if (cluster != null) {
			cluster.hset(key, field, content);
			cluster.expire(key, timeout);
		} else if (pool != null) {
			Jedis jedis = pool.getResource();
			try {
				jedis.hset(key, field, content);
				jedis.expire(key, timeout);
			} finally {
				if (jedis != null && pool != null) {
					pool.returnResourceObject(jedis);
				}
			}
		} else {
			throw new Exception("设置缓存异常：");
		}
	}
	public static void deleteMap(String key, String field) throws Exception {
		if (cluster != null) {
			cluster.hdel(key, field);
		} else if (pool != null) {
			Jedis jedis = pool.getResource();
			try {
				jedis.hdel(key, field);
			} finally {
				if (jedis != null && pool != null) {
					pool.returnResourceObject(jedis);
				}
			}
		} else {
			throw new Exception("设置缓存异常：");
		}
	}

	/**
	 * 判断键值是否存在
	 * 
	 * @param key
	 * @return
	 * @throws Exception
	 */
	public static boolean exists(String key) throws Exception {
		if (cluster != null) {
			return cluster.exists(key);
		} else if (pool != null) {
			Jedis jedis = pool.getResource();
			try {
				return jedis.exists(key);
			} finally {
				if (jedis != null && pool != null) {
					pool.returnResourceObject(jedis);
				}
			}
		} else {
			throw new Exception("设置缓存异常：");
		}
	}
	/**
	 * 设置票据过期时间
	 * @param key
	 * @return
	 * @throws Exception
	 */
	public static void exprie(String key,int timeout) throws Exception {
		if (cluster != null) {
			cluster.expire(key, timeout);
		} else if (pool != null) {
			Jedis jedis = pool.getResource();
			try {
				jedis.expire(key, timeout);
			} finally {
				if (jedis != null && pool != null) {
					pool.returnResourceObject(jedis);
				}
			}
		} else {
			throw new Exception("设置缓存异常：");
		}
	}
	/**
	 * 获取Map中的值
	 * @param key
	 * @param field
	 * @return
	 */
	public static String getMap(String key, String field) {
		if (cluster != null) {
			return cluster.hget(key, field);
		} else if (pool != null) {
			Jedis jedis = pool.getResource();
			try {
				return jedis.hget(key, field);
			} finally {
				if (jedis != null && pool != null) {
					pool.returnResourceObject(jedis);
				}
			}
		}
		return null;
	}

	/**
	 * 获取HashMap
	 * 
	 * @param key
	 * @return
	 */
	public static Map<String, String> getMapAll(String key) {
		
		if (cluster != null) {
			return cluster.hgetAll(key);
		} else if (pool != null) {
			Jedis jedis = pool.getResource();
			try {
				return jedis.hgetAll(key);
			} finally {
				if (jedis != null && pool != null) {
					pool.returnResourceObject(jedis);
				}
			}
		}
		return null;
	}

	/**
	 * 获取数组列表
	 * 
	 * @param key
	 * @return
	 */
	public static List<String> getStrList(String key) {
		List<String> arrayList = new ArrayList<String>();
		Set<String> sets = null;
		if (cluster != null) {
			sets = cluster.smembers(key);
		} else if (pool != null) {
			Jedis jedis = pool.getResource();
			try {
				sets = jedis.smembers(key);
			} finally {
				if (jedis != null && pool != null) {
					pool.returnResourceObject(jedis);
				}
			}
		}
		if (sets != null && sets.size() > 0) {
			arrayList.addAll(sets);
		}
		return arrayList;
	}
	

	private static void initPool() throws Exception {
		JedisPoolConfig poolConfig = new JedisPoolConfig();
		// 设置最大连接数
		poolConfig.setMaxTotal(RedisUtils.MAX_ACTIVE);

		// 设置最大阻塞时间，记住是毫秒数milliseconds
		poolConfig.setMaxWaitMillis(RedisUtils.MAX_WAIT);

		// 设置空间连接
		poolConfig.setMaxIdle(RedisUtils.MAX_IDLE);

		poolConfig.setTestOnBorrow(RedisUtils.TEST_ON_BORROW);


//=========================liulongyun添加测试部分start============================//
		poolConfig.setMinIdle(MIN_IDEL);//设置最小空闲数
		poolConfig.setTestOnReturn(TEST_ON_RETURN);
		//Idle时进行连接扫描
		poolConfig.setTestWhileIdle(TEST_WHILE_IDLE);
		//表示idle object evitor两次扫描之间要sleep的毫秒数
		poolConfig.setTimeBetweenEvictionRunsMillis(TIME_BETWEEN_EVICTION_RUNS_MILLIS);
		//表示idle object evitor每次扫描的最多的对象数
		poolConfig.setNumTestsPerEvictionRun(NUM_TEST_SPEREVICTION_RUN);
		//表示一个对象至少停留在idle状态的最短时间，然后才能被idle object evitor扫描并驱逐；这一项只有在timeBetweenEvictionRunsMillis大于0时才有意义
		poolConfig.setMinEvictableIdleTimeMillis(MINEVICTABLE_IDLE_TIME_MILLIS);

//=========================liulongyun添加测试部分end============================//





		if (servers.size() == 0) {
			throw new Exception("没有服务器配置，不能加载Redis连接池");
		}
		if (servers.size() == 1) {// 单节点直接使用连接池
			String ip = "";
			int port = 0;
			Iterator<HostAndPort> hpit = servers.iterator();
			if (hpit.hasNext()) {
				HostAndPort hp = hpit.next();
				ip = hp.getHost();
				port = hp.getPort();

			}
			// 创建连接池
			pool = new JedisPool(poolConfig, ip, port);
		} else {
			cluster = new JedisCluster(servers, poolConfig);
		}
	}

	/**
	 * 连接池的初始化
	 */
	public static void init() {
		logger.info("开始初始化Redis的连接");
		try {
			InputStream in = RedisUtils.class
					.getResourceAsStream("/conf/redis.cfg.xml");
			SAXBuilder saxBuilder = new SAXBuilder(false);
			saxBuilder.setExpandEntities(false);
			Document doc = saxBuilder.build(in);
			Element el = doc.getRootElement();
			if (StringUtils.hasLength(el.getChildTextTrim("MAX_ACTIVE"))) {
				MAX_ACTIVE = Integer
						.parseInt(el.getChildTextTrim("MAX_ACTIVE"));
			}
			if (StringUtils.hasLength(el.getChildTextTrim("MAX_IDLE"))) {
				MAX_IDLE = Integer.parseInt(el.getChildTextTrim("MAX_IDLE"));
			}
			if (StringUtils.hasLength(el.getChildTextTrim("MAX_WAIT"))) {
				MAX_WAIT = Integer.parseInt(el.getChildTextTrim("MAX_WAIT"));
			}
			if (StringUtils.hasLength(el.getChildTextTrim("TEST_ON_BORROW"))) {
				if ("true".equalsIgnoreCase(el
						.getChildTextTrim("TEST_ON_BORROW"))) {
					TEST_ON_BORROW = true;
				} else {
					TEST_ON_BORROW = false;
				}
			}


			if (StringUtils.hasLength(el.getChildTextTrim("TEST_ON_RETURN"))) {
				if ("true".equalsIgnoreCase(el
						.getChildTextTrim("TEST_ON_RETURN"))) {
					TEST_ON_RETURN = true;
				} else {
					TEST_ON_RETURN = false;
				}
			}

			if (StringUtils.hasLength(el.getChildTextTrim("TEST_WHILE_IDLE"))) {
				if ("true".equalsIgnoreCase(el
						.getChildTextTrim("TEST_WHILE_IDLE"))) {
					TEST_WHILE_IDLE = true;
				} else {
					TEST_WHILE_IDLE = false;
				}
			}
			if (StringUtils.hasLength(el.getChildTextTrim("MIN_IDEL"))) {
				MIN_IDEL = Integer.parseInt(el.getChildTextTrim("MIN_IDEL"));
			}

			if (StringUtils.hasLength(el.getChildTextTrim("TIME_BETWEEN_EVICTION_RUNS_MILLIS"))) {
				TIME_BETWEEN_EVICTION_RUNS_MILLIS = Integer.parseInt(el.getChildTextTrim("TIME_BETWEEN_EVICTION_RUNS_MILLIS"));
			}

			if (StringUtils.hasLength(el.getChildTextTrim("NUM_TEST_SPEREVICTION_RUN"))) {
				NUM_TEST_SPEREVICTION_RUN = Integer.parseInt(el.getChildTextTrim("NUM_TEST_SPEREVICTION_RUN"));
			}
			if (StringUtils.hasLength(el.getChildTextTrim("MINEVICTABLE_IDLE_TIME_MILLIS"))) {
				MINEVICTABLE_IDLE_TIME_MILLIS = Integer.parseInt(el.getChildTextTrim("MINEVICTABLE_IDLE_TIME_MILLIS"));
			}

			List<Element> childList = el.getChild("servers").getChildren();
			if (childList != null && childList.size() > 0) {
				for (int i = 0; i < childList.size(); i++) {
					HostAndPort node = new HostAndPort(childList.get(i)
							.getAttributeValue("ip"),
							Integer.parseInt(childList.get(i)
									.getAttributeValue("port")));
					servers.add(node);
				}
			}
			initPool();
			logger.info("初始化Redis的连接成功！");
		} catch (Exception e) {
			logger.error("初始化Redis数据库连接异常：" + e.getMessage());
		}
	}

	public static String serialize(Object obj) throws IOException {
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		ObjectOutputStream oos = new ObjectOutputStream(
				new BufferedOutputStream(bos));
		oos.writeObject(obj);
		oos.close();
		String rtn = bytesToHexString(bos.toByteArray());
		return rtn;
	}

	public static String bytesToHexString(byte[] src) {
		StringBuilder stringBuilder = new StringBuilder("");
		if (src == null || src.length <= 0) {
			return null;
		}
		for (int i = 0; i < src.length; i++) {
			int v = src[i] & 0xFF;
			String hv = Integer.toHexString(v);
			if (hv.length() < 2) {
				stringBuilder.append(0);
			}
			stringBuilder.append(hv);
		}
		return stringBuilder.toString();
	}

	/**
	 * Convert hex string to byte[]
	 * 
	 * @param hexString
	 *            the hex string
	 * @return byte[]
	 */
	public static byte[] hexStringToBytes(String hexString) {
		if (hexString == null || hexString.equals("")) {
			return null;
		}
		hexString = hexString.toUpperCase();
		int length = hexString.length() / 2;
		char[] hexChars = hexString.toCharArray();
		byte[] d = new byte[length];
		for (int i = 0; i < length; i++) {
			int pos = i * 2;
			d[i] = (byte) (charToByte(hexChars[pos]) << 4 | charToByte(hexChars[pos + 1]));
		}
		return d;
	}

	/**
	 * Convert char to byte
	 * 
	 * @param c
	 *            char
	 * @return byte
	 */
	private static byte charToByte(char c) {
		return (byte) "0123456789ABCDEF".indexOf(c);
	}

	public static Object unserialize(String datastr) throws IOException,
			ClassNotFoundException {
		try {
			byte[] data = hexStringToBytes(datastr);

			BufferedInputStream bis = new BufferedInputStream(
					new ByteArrayInputStream(data));
			ObjectInputStream ois = new ObjectInputStream(bis);
			return ois.readObject();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

}
