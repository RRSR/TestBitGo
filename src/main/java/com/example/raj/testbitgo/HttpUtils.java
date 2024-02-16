package com.example.raj.testbitgo;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

//@Component
public class HttpUtils {

  static final Map<String, Set<String>> parentMap = new ConcurrentHashMap<>();

  public static boolean getTransactions(int offset) {
    RestTemplate restTemplate = new RestTemplate();
    String uri =
        "https://blockstream.info/api/block/000000000000000000076c036ff5119e5a5a74df77abf64203473364509f7732/txs/";

    HttpEntity<String> entity = new HttpEntity<>("parameters", null);
    try {
      ResponseEntity<String> result =
          restTemplate.exchange(uri + offset, HttpMethod.GET, entity, String.class);

      JSONArray jsonArray = new JSONArray(result.getBody());
      if (jsonArray == null || jsonArray.length() == 0) {
        return false;
      }
      for (int i = 0; i < jsonArray.length(); i++) {
        JSONObject jsonObject = (JSONObject) jsonArray.get(i);
        Set<String> parents = new HashSet<>();
        JSONArray parentArr = (JSONArray) jsonObject.get("vin");
        if (parentArr != null && parentArr.length() > 0) {
          for (int j = 0; j < parentArr.length(); j++) {
            JSONObject parentObj = (JSONObject) parentArr.get(j);
            parents.add((String) parentObj.get("txid"));
          }
        }
        parentMap.put((String) jsonObject.get("txid"), parents);
      }

    } catch (JSONException e) {
      throw new RuntimeException(e);
    } catch (HttpClientErrorException.NotFound notFound) {
      System.out.println("No more results left");
      return false;
    }
    return true;
  }
}
