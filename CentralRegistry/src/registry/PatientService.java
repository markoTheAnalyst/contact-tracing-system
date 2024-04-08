package registry;

import model.Patient;
import redis.clients.jedis.Jedis;

public class PatientService {
	static Jedis jedis = new Jedis("localhost");
	static {
		jedis.hset("aaa", "x", "4");
		jedis.hset("aaa", "y", "41");
		jedis.hset("bbb", "x", "24");
		jedis.hset("bbb", "y", "14");
		jedis.hset("ccc", "x", "3");
		jedis.hset("ccc", "y", "7");
		
		
	}
	
	public Patient getByToken(String token) {
		for (String key : jedis.keys("*")) {
			if (key.contentEquals(token)) {
				return new Patient(token,jedis.hget(token, "x"),jedis.hget(token, "y"));
			}
		}
		return null;
	}
}
