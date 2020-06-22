import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.UUID;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import redis.clients.jedis.Jedis;
import Map.RedisMapJedisImpl;

public class MapTest {
  static String key = "user##1";
  private final Jedis jedis = new Jedis("localhost",6379);
  private Map<String, Integer> jedisMap;
  Random random;

  @BeforeEach
  public void beforeEach() {
    if (!jedis.isConnected()){
      jedis.connect();
    }
    System.out.println("connected: " + jedis.isConnected());
    random = new Random();
    key = "user#" + random.nextInt() + "#" + UUID.randomUUID();
    System.out.println(key);
  }

  @AfterEach
  public void afterEach() {
    jedis.close();
  }

  @Test
  void SizeEqualToZero() {
    jedisMap = new RedisMapJedisImpl<>(jedis, key);
    assertThat(jedisMap.size(), equalTo(0));
  }


  @Test
  void SizeEqualTo6() {
    jedisMap = new RedisMapJedisImpl<>(jedis, key);
    addOne(jedis, key, "name1", 17);
    jedis.hset(key, "name", "Peter1");
    jedis.hset(key, "name2", "Peter1");
    jedis.hset(key, "name3", "Peter1");
    jedis.hset(key, "1", "Peter");
    jedis.hset(key, "job", "politician");
    assertThat(jedisMap.size(), equalTo(6));
  }

  @Test
  void putItemToMap() {
    jedisMap = new RedisMapJedisImpl<>(jedis, key);
    jedisMap.put("one", 1);
    jedisMap.put("two", 2);
    assertThat(jedis.hget(key, "one"), equalTo("1"));
  }

  @Test
  void containsKeyInMap() {
    jedisMap = new RedisMapJedisImpl<>(jedis, key);
    jedisMap.put("one", 1);
    jedisMap.put("two", 2);
    assertThat(jedisMap.containsKey("one"), equalTo(true));
    jedisMap.remove("one");
    assertThat(jedisMap.containsKey("one"), equalTo(false));
  }

  @Test
  void containsValueInMap() {
    jedisMap = new RedisMapJedisImpl<>(jedis, key);
    jedisMap.put("one", 1);
    jedisMap.put("two", 2);
    assertThat(jedisMap.containsValue(1), equalTo(true));
    jedisMap.remove("one");
    assertThat(jedisMap.containsValue(1), equalTo(false));
  }

  @Test
  void updateItemInMap() {
    jedisMap = new RedisMapJedisImpl<>(jedis, key);
    jedisMap.put("one", 1);
    jedisMap.put("one", 2);
    assertThat(jedis.hget(key, "one"), equalTo("2"));
  }

  @Test
  void deleteItemFromMap() {
    jedisMap = new RedisMapJedisImpl<>(jedis, key);
    jedisMap.put("one", 1);
    jedisMap.put("two", 2);
    assertThat(jedis.hget(key, "one"), equalTo("1"));
  }

  private void addOne(Jedis jedis, String keyMap, String key, Integer value) {
    jedis.hset(keyMap, key, value.toString());
  }

  @Test
  void removeItemFromMap() {
    jedisMap = new RedisMapJedisImpl<>(jedis, key);
    addOne(jedis, key, "one", 1);
    addOne(jedis, key, "two", 2);
    Integer remove = jedisMap.remove("one");
    assertThat(jedis.hget(key, "one"), equalTo(null));
    assertThat(remove, equalTo(1));
  }

  @Test
  void getItemFromMap() {
    jedisMap = new RedisMapJedisImpl<>(jedis, key);
    addOne(jedis, key, "one", 1);
    addOne(jedis, key, "two", 2);
    assertThat(jedisMap.get("one"), equalTo(1));
  }

  @Test
  void putAllItemToMap() {
    Map<String, Integer> jdkMap = new HashMap<>();
    jdkMap.put("5Key", 5);
    jdkMap.put("6Key", 6);
    jdkMap.put("3Key", 3);
    jdkMap.put("4Key", 4);

    jedisMap = new RedisMapJedisImpl<>(jedis, key);
    addOne(jedis, key, "1Key", 1);
    addOne(jedis, key, "2Key", 2);
    jedisMap.putAll(jdkMap);
    assertThat(jedisMap.size(), equalTo(6));
  }

  @Test
  void clearItemFromMap() {
    jedisMap = new RedisMapJedisImpl<>(jedis, key);
    addOne(jedis, key, "one", 1);
    addOne(jedis, key, "two", 2);
    jedisMap.clear();
    assertThat(jedisMap.size(), equalTo(0));
  }

  @Test
  void keySetItemFromMap() {
    jedisMap = new RedisMapJedisImpl<>(jedis, key);
    Map<String, Integer> jdkMap = new HashMap<>();
    jdkMap.put("5Key", 5);
    jdkMap.put("6Key", 6);
    jdkMap.put("3Key", 3);
    jdkMap.put("4Key", 4);
    jedisMap.putAll(jdkMap);
    assertThat(jdkMap.keySet(), equalTo(jedisMap.keySet()));
  }

  @Test
  void entrySetItemFromMap() {
    jedisMap = new RedisMapJedisImpl<>(jedis, key);
    Map<String, Integer> jdkMap = new HashMap<>();
    jdkMap.put("5Key", 5);
    jdkMap.put("6Key", 6);
    jdkMap.put("3Key", 3);
    jdkMap.put("4Key", 4);
    jedisMap.putAll(jdkMap);
    System.out.println("JDK " + jdkMap);
    System.out.println("Jedis " + jedisMap);
    assertThat(jdkMap.keySet(), equalTo(jedisMap.keySet()));
  }

  @Test
  void valuesFromMap() {
    jedisMap = new RedisMapJedisImpl<>(jedis, key);
    Map<String, Integer> jdkMap = new HashMap<>();
    jdkMap.put("5Key", 5);
    jdkMap.put("6Key", 6);
    jdkMap.put("3Key", 3);
    jdkMap.put("4Key", 4);
    jedisMap.putAll(jdkMap);
    System.out.println("JDK " + jdkMap);
    System.out.println("Jedis " + jedisMap);
    assertThat(jdkMap.values().containsAll(jedisMap.values()), equalTo(true));
  }

  @Test
  void testforEach() {
    jedisMap = new RedisMapJedisImpl<>(jedis, key);
    Map<String, Integer> hashMap = new HashMap<>();
    hashMap.put("5Key", 5);
    hashMap.put("6Key", 6);
    hashMap.put("3Key", 3);
    hashMap.put("4Key", 4);
    jedisMap.putAll(hashMap);
    System.out.println("JDK " + hashMap);
    System.out.println("Jedis " + jedisMap);
    jedisMap.forEach((k, v) -> System.out.println((k + ":" + v)));
    assertThat(hashMap.values().containsAll(jedisMap.values()), equalTo(true));
  }
}
