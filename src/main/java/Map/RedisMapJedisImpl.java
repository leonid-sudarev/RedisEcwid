package Map;

import java.io.Serializable;
import java.util.AbstractMap;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.Transaction;

public class RedisMapJedisImpl<K extends String, V extends Integer>
    extends AbstractMap<String, Integer> implements Map<String, Integer>, Cloneable, Serializable {
  Jedis jedis;
  String keyMap;

  public RedisMapJedisImpl(Jedis jedis, String key) {
    this.jedis = jedis;
    this.keyMap = key;
  }

  @Override
  public int size() {
    return jedis.hkeys(keyMap).size();
  }

  @Override
  public boolean isEmpty() {
    return size() == 0;
  }

  @Override
  public boolean containsKey(Object key) {
    return jedis.hexists(keyMap, (String) key);
  }

  @Override
  public boolean containsValue(Object value) {
    List<String> hvals = jedis.hvals(keyMap);
    return hvals.contains(value.toString());
  }

  @Override
  public Integer get(Object key) {
    return Integer.parseInt(jedis.hget(keyMap, (String) key));
  }

  @Override
  public Integer put(String key, Integer value) {
    jedis.hset(keyMap, key, value.toString());
    return value;
  }

  @Override
  public Integer remove(Object key) {
    var hget = Integer.parseInt(jedis.hget(keyMap, (String) key));
    jedis.hdel(keyMap, (String) key);
    return hget;
  }

  @Override
  public void putAll(Map<? extends String, ? extends Integer> m) {
    Transaction t = jedis.multi();
    for (Entry<? extends String, ? extends Integer> entry : m.entrySet()) {
      t.hset(keyMap, entry.getKey(), entry.getValue().toString());
    }
    t.exec();
  }

  //   Меняется порядок записей
  //   JDKmap {6Key=6, 5Key=5, 4Key=4, 3Key=3}
  //   JedisMap {6Key=6, 4Key=4,5Key=5, 3Key=3}
  //
  //  @Override
  //  public void putAll(Map<? extends String, ? extends Integer> m) {
  //    Map<String, String> map = new HashMap<>(m.size());
  //    for (Entry<? extends String, ? extends Integer> entry : m.entrySet()) {
  //      map.put(entry.getKey(), entry.getValue().toString());
  //       System.out.println(map);
  //    }
  //    jedis.hmset(keyMap, map);
  //  }

  @Override
  public void clear() {
    Map<String, String> hgetAll = jedis.hgetAll(keyMap);
    jedis.hdel(keyMap, hgetAll.keySet().toArray(new String[hgetAll.size()]));
  }

  @Override
  public Set<String> keySet() {
    return jedis.hgetAll(keyMap).keySet();
  }

  @Override
  public Collection<Integer> values() {
    return jedis.hvals(keyMap).stream().map(Integer::parseInt).collect(Collectors.toList());
  }

  @Override
  public Set<Entry<String, Integer>> entrySet() {
    Map<String, String> hgetAll = jedis.hgetAll(keyMap);
    Map<String, Integer> result = new HashMap<>((hgetAll.size() + 2) / 2);
    for (Entry<? extends String, ? extends String> entry : hgetAll.entrySet()) {
      result.put(entry.getKey(), Integer.parseInt(entry.getValue()));
    }
    return result.entrySet();
  }
}
