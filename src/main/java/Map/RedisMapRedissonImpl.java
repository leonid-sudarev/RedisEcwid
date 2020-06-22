package Map;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

import org.redisson.api.RMap;

public class RedisMapRedissonImpl<K extends String, V extends Integer> implements Map<String, Integer> {
  RMap<String, Integer> map;

  @Override
  public String toString() {
    return super.toString();
  }

  public RedisMapRedissonImpl(RMap<String, Integer> map ) {
    this.map = map;
  }

  @Override
  public int size() {
    return map.size();
  }

  @Override
  public boolean isEmpty() {
    return map.isEmpty();
  }

  @Override
  public boolean containsKey(Object key) {
    return map.containsKey(key);
  }

  @Override
  public boolean containsValue(Object value) {
    return map.containsValue(value);
  }

  @Override
  public Integer get(Object key) {
    return map.get(key);
  }

  @Override
  public Integer put(String key, Integer value) {
    return map.put(key, value);
  }

  @Override
  public Integer remove(Object key) {
    return map.remove(key);
  }

  @Override
  public void putAll(Map<? extends String, ? extends Integer> m) {
    map.putAll(m);
  }

  @Override
  public void clear() {
    map.clear();
  }

  @Override
  public Set<String> keySet() {
    return map.keySet();
  }

  @Override
  public Collection<Integer> values() {
    return map.values();
  }

  @Override
  public Set<Entry<String, Integer>> entrySet() {
    return map.entrySet();
  }
}
