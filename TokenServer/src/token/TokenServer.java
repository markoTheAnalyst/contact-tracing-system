package token;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import redis.clients.jedis.Jedis;

public class TokenServer {
	
	private static String HOST = "localhost";
	private static String ERROR = "error";
	private static String NAME = "name";
	private static String LASTNAME = "lastName";
	private static String TOKEN = "token";
	private static String ID = "id";
	private static String JMBG = "jmbg:*";
	
	public String logIn(String data) {
		String[] logInInfo = data.split("@");
		String token = ERROR;
		try(Jedis jedis = new Jedis(HOST)){
			if(jedis.exists(logInInfo[0])) {
				String name = jedis.hget(logInInfo[0], NAME);
				String lastName = jedis.hget(logInInfo[0], LASTNAME);
				if(name.equals(logInInfo[1]) && lastName.equals(logInInfo[2])){
					token = UUID.randomUUID().toString();
					jedis.hset(logInInfo[0], TOKEN, token);
				}
			}
		}
		return token;
	}
	
	public void logOut(String token) {

		try(Jedis jedis = new Jedis(HOST)){
			Set<String> keys = jedis.keys(JMBG);
			for(String key : keys) {
				if(jedis.hget(key, TOKEN).equals(token)) {
					jedis.hset(key, TOKEN, "");
					break;
				}
			}
		}	
	}
	
	public String listTokens() {
		Set<String> tokens = new HashSet<>();
		try(Jedis jedis = new Jedis(HOST)){
			Set<String> keys = jedis.keys(JMBG);
			for(String key : keys) {
				tokens.add(jedis.hget(key, TOKEN));
			}
		}
		tokens.remove("");
		return tokens.toString();
	}
	public String doesTokenExist(String token) {
		String id = "none";
		try(Jedis jedis = new Jedis(HOST)){
			Set<String> keys = jedis.keys(JMBG);
			for(String key : keys) {
				if(jedis.hget(key, TOKEN).equals(token)) {
					id = jedis.hget(key, ID);
					break;
				}
			}
		}
		return id;
	}
	
	public String getToken(String id) {
		String token = "";
		try(Jedis jedis = new Jedis(HOST)){
			Set<String> keys = jedis.keys(JMBG);
			for(String key : keys) {
				if(jedis.hget(key, ID).equals(id)) {
					token = jedis.hget(key, TOKEN);
					break;
				}
			}
		}
		return token;
	}
}

