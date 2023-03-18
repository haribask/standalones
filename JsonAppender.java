import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import java.util.Map;
import java.util.HashMap;

public class JsonAppender {
	private Gson gson = null;

	JsonAppender() {
      if(gson == null)
          gson = new Gson();
	}

	private static Map<String, String> getAdditionalAttributes() {
		Map<String, String> attributes = new HashMap<String, String>();
		attributes.put("Attr1","100");
		attributes.put("POLICY_NUMBER","POL_NBR");
		attributes.put("TYPE","AUTO");
		attributes.put("Attr2","150");
		attributes.put("Attr3","200");

		return attributes;
	}

  private String appendAttributes(Map<String, String> attributes, String data) throws Exception {
		JsonParser parser = new JsonParser();

	  if(attributes != null && !attributes.isEmpty()) {
			JsonElement jsonElement = parser.parse(data);
      for(Map.Entry<String, String> attribute : attributes.entrySet()) {
          jsonElement.getAsJsonObject().addProperty(attribute.getKey(), attribute.getValue());
      }
          
      data = gson.toJson(jsonElement);
    }

    return data;
  }

	public static void main(String s[]) throws Exception {
		System.out.println("[main] entered");
		JsonAppender jsonAppender = new JsonAppender();

		Map<String, String> attributes = getAdditionalAttributes();

		String data = "{\"PaymentDetails\":{\"recordId\":1001001,\"transactionType\":\"payment\",\"transactionTotalAmount\":110,\"transactionDateTime\":\"2023-03-15T12:33:21.463-07:00\",\"status\":\"SUCC\",\"paymentCard\":{\"paymentCardType\":\"VISA\",\"paymentCardExpirationDate\":\"2024-09\",\"paymentCardLastFour\":6785,\"paymentCardZipCode\":80239},\"policy\":{\"agmtNumber\":\"891426-22-1801-01\",\"productType\":\"Auto\"}}}";

		data = jsonAppender.appendAttributes(attributes, data);
		System.out.println("data enhanceed with additional attribues:::"+data);
	}
}
