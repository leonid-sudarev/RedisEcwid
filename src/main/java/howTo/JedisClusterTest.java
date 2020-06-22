package howTo;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.JedisPool;

public class JedisClusterTest {
  static Random random = new Random();

  public static void main(String[] args) throws IOException {

    Set<HostAndPort> connectionPoints = new HashSet<HostAndPort>();
    connectionPoints.add(new HostAndPort("localhost", 7001));
    JedisCluster cluster = new JedisCluster(connectionPoints);

    cluster.getClusterNodes().forEach((x, y) -> System.out.println(x + "   " + y));
    Map<String, JedisPool> clusterNodes = cluster.getClusterNodes();
    System.out.println("clusterNodes " + clusterNodes);

    // jedisPools1.forEach(x -> System.out.println("\n \t " +  x.getResource().clusterInfo()));
    //    cluster.set("key", "value");
    Map<String, String> hashMap = new HashMap<>();
    for (int i = 0; i < 5; i++) {
      String key = "hashKey" + random.nextInt();
      String value = random.nextInt() + "A";
      cluster.hset("Key1", key, value);
    }
    System.out.println(cluster.hgetAll("Key1"));


    //        System.out.println("meet: " +
    //     jedisPools1.get(0).getResource().clusterMeet("127.0.0.1",8001));

    //    jedisPools1.forEach(x -> System.out.println("\n \t " +  x.getResource().clusterInfo()));
    cluster.getClusterNodes().forEach((x, y) -> System.out.println(x + "   " + y));

    JedisPool[] jedisPools =
        cluster.getClusterNodes().values().toArray(new JedisPool[cluster.getClusterNodes().size()]);

    System.out.println(jedisPools[0].getResource().clusterInfo());
    System.out.println("SLOTS");
    System.out.println(jedisPools[0].getResource().clusterSlots());
    System.out.println("NODES");
    System.out.println(jedisPools[0].getResource().clusterNodes());

    System.out.println(Arrays.toString(jedisPools));
    cluster.close();
  }
}
