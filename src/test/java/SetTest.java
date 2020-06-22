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
  private final Jedis jedis = new Jedis("localhost",7000);
  private Map<String, Integer> jdkMap;
  Random random;

  @BeforeEach
  public void beforeEach() {
    if (!jedis.isConnected()){
      jedis.connect();
    }
    System.out.println("connected: " + jedis.isConnected());
    random = new Random();
    key = "map#{asd}" + random.nextInt() + "#" + UUID.randomUUID();
    System.out.println(key);
  }

  @AfterEach
  public void afterEach() {
    jedis.close();
  }

  @Test
  void SizeEqualToZero() {
    jdkMap = new RedisMapJedisImpl<>(jedis, key);
    assertThat(jdkMap.size(), equalTo(0));
  }


  @Test
  void SizeEqualTo6() {
    jdkMap = new RedisMapJedisImpl<>(jedis, key);
    addOne(jedis, key, "name1", 17);
    jedis.hset(key, "name", "Peter1");
    jedis.hset(key, "name2", "Peter1");
    jedis.hset(key, "name3", "Peter1");
    jedis.hset(key, "1", "Peter");
    jedis.hset(key, "job", "politician");
    assertThat(jdkMap.size(), equalTo(6));
  }

  @Test
  void putItemToMap() {
    jdkMap = new RedisMapJedisImpl<>(jedis, key);
    jdkMap.put("one", 1);
    jdkMap.put("two", 2);
    assertThat(jedis.hget(key, "one"), equalTo("1"));
  }

  @Test
  void containsKeyInMap() {
    jdkMap = new RedisMapJedisImpl<>(jedis, key);
    jdkMap.put("one", 1);
    jdkMap.put("two", 2);
    assertThat(jdkMap.containsKey("one"), equalTo(true));
    jdkMap.remove("one");
    assertThat(jdkMap.containsKey("one"), equalTo(false));
  }

  @Test
  void containsValueInMap() {
    jdkMap = new RedisMapJedisImpl<>(jedis, key);
    jdkMap.put("one", 1);
    jdkMap.put("two", 2);
    assertThat(jdkMap.containsValue(1), equalTo(true));
    jdkMap.remove("one");
    assertThat(jdkMap.containsValue(1), equalTo(false));
  }

  @Test
  void updateItemInMap() {
    jdkMap = new RedisMapJedisImpl<>(jedis, key);
    jdkMap.put("one", 1);
    jdkMap.put("one", 2);
    assertThat(jedis.hget(key, "one"), equalTo("2"));
  }

  @Test
  void deleteItemFromMap() {
    jdkMap = new RedisMapJedisImpl<>(jedis, key);
    jdkMap.put("one", 1);
    jdkMap.put("two", 2);
    assertThat(jedis.hget(key, "one"), equalTo("1"));
  }

  private void addOne(Jedis jedis, String keyMap, String key, Integer value) {
    jedis.hset(keyMap, key, value.toString());
  }

  @Test
  void removeItemFromMap() {
    jdkMap = new RedisMapJedisImpl<>(jedis, key);
    addOne(jedis, key, "one", 1);
    addOne(jedis, key, "two", 2);
    Integer remove = jdkMap.remove("one");
    assertThat(jedis.hget(key, "one"), equalTo(null));
    assertThat(remove, equalTo(1));
  }

  @Test
  void getItemFromMap() {
    jdkMap = new RedisMapJedisImpl<>(jedis, key);
    addOne(jedis, key, "one", 1);
    addOne(jedis, key, "two", 2);
    assertThat(jdkMap.get("one"), equalTo(1));
  }

  @Test
  void putAllItemToMap() {
    Map<String, Integer> jdkMap = new HashMap<>();
    jdkMap.put("5Key", 5);
    jdkMap.put("6Key", 6);
    jdkMap.put("3Key", 3);
    jdkMap.put("4Key", 4);

    this.jdkMap = new RedisMapJedisImpl<>(jedis, key);
    addOne(jedis, key, "1Key", 1);
    addOne(jedis, key, "2Key", 2);
    this.jdkMap.putAll(jdkMap);
    assertThat(this.jdkMap.size(), equalTo(6));
  }

  @Test
  void clearItemFromMap() {
    jdkMap = new RedisMapJedisImpl<>(jedis, key);
    addOne(jedis, key, "one", 1);
    addOne(jedis, key, "two", 2);
    jdkMap.clear();
    assertThat(jdkMap.size(), equalTo(0));
  }

  @Test
  void keySetItemFromMap() {
    jdkMap = new RedisMapJedisImpl<>(jedis, key);
    Map<String, Integer> jdkMap = new HashMap<>();
    jdkMap.put("5Key", 5);
    jdkMap.put("6Key", 6);
    jdkMap.put("3Key", 3);
    jdkMap.put("4Key", 4);
    this.jdkMap.putAll(jdkMap);
    assertThat(jdkMap.keySet(), equalTo(this.jdkMap.keySet()));
  }

  @Test
  void entrySetItemFromMap() {
    jdkMap = new RedisMapJedisImpl<>(jedis, key);
    Map<String, Integer> jdkMap = new HashMap<>();
    jdkMap.put("5Key", 5);
    jdkMap.put("6Key", 6);
    jdkMap.put("3Key", 3);
    jdkMap.put("4Key", 4);
    this.jdkMap.putAll(jdkMap);
    System.out.println("JDK " + jdkMap);
    System.out.println("Jedis " + this.jdkMap);
    assertThat(jdkMap.keySet(), equalTo(this.jdkMap.keySet()));
  }

  @Test
  void valuesFromMap() {
    jdkMap = new RedisMapJedisImpl<>(jedis, key);
    Map<String, Integer> jdkMap = new HashMap<>();
    jdkMap.put("5Key", 5);
    jdkMap.put("6Key", 6);
    jdkMap.put("3Key", 3);
    jdkMap.put("4Key", 4);
    this.jdkMap.putAll(jdkMap);
    System.out.println("JDK " + jdkMap);
    System.out.println("Jedis " + this.jdkMap);
    assertThat(jdkMap.values().containsAll(this.jdkMap.values()), equalTo(true));
  }

  @Test
  void testforEach() {
    jdkMap = new RedisMapJedisImpl<>(jedis, key);
    Map<String, Integer> hashMap = new HashMap<>();
    hashMap.put("5Key", 5);
    hashMap.put("6Key", 6);
    hashMap.put("3Key", 3);
    hashMap.put("4Key", 4);
    jdkMap.putAll(hashMap);
    System.out.println("JDK " + hashMap);
    System.out.println("Jedis " + jdkMap);
    jdkMap.forEach((k, v) -> System.out.println((k + ":" + v)));
    assertThat(hashMap.values().containsAll(jdkMap.values()), equalTo(true));
  }
}
