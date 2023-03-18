import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.kafka.common.serialization.StringSerializer;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import java.util.Properties;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.Map;
import java.util.HashMap;

public class AWS_MSK_KafkaPublisher {
	private Producer<String, String> producer;
	private String topic = null;

	private Gson gson = null;

	AWS_MSK_KafkaPublisher() {
		if(gson == null)
			gson = new Gson();

		initiateMessengerProducer();
	}

	void initiateMessengerProducer() {
		try {
			producer = new KafkaProducer<>(getProperties());
		} catch(Exception me) {
			System.out.println("[From Messenger App] KafkaProducer couldn't be initiated::"+me.getMessage());
		}
	}

	private Properties getProperties() {
		Properties props = new Properties();

		props.put("security.protocol","SASL_SSL");
		props.put("acks", "all");
		props.put("retries", 0);
		props.put("batch.size", 16384);
		props.put("linger.ms", 1);
		props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "bootstrap-server=<<server>>:<<port>>");
		props.put("sasl.jaas.config", "org.apache.kafka.common.security.scram.ScramLoginModule required username=\"user\" password=\"password\";");
		props.put("sasl.mechanism", "SCRAM-SHA-512");
		props.put("ssl.truststore.location", "lib/cert.jks");
		props.put("ssl.truststore.password", "certpassword");
		props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
    props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);

		topic = "topic-name";
    return props;
	}

  public String publish(Map<String, String> headers, String message) {
		System.out.println("[message publishing begins]");

		String returnString = null;

		if(producer != null) {
			Future<RecordMetadata> future = null;
			ProducerRecord producerRecord = null;

			try {
				producerRecord = new ProducerRecord<String, String>(topic, "KEY", message);

				addHeader(producerRecord, headers);
				future = producer.send(producerRecord);
				
				System.out.println("Message "+message+" delivered successfully with offset: "+future.get().offset());
			} catch(ExecutionException e) {
				System.out.println("Unable to deliver message "+message+" , error : "+e.getMessage());
			} catch(InterruptedException ie) {
				System.out.println("Unable to deliver message "+message+" , error : "+ie.getMessage());
			}
		}

		return returnString;
  }

	private void addHeader(ProducerRecord producerRecord, Map<String, String> headers) {
    	if(headers != null && !headers.isEmpty()) {
    		for(Map.Entry<String, String> header : headers.entrySet()) {
    			producerRecord.headers().add(header.getKey(),header.getValue().getBytes());
    		}
    	}
	}
    
  public void closeProducer() {
  	if (producer != null) {
  		producer.flush();
  		producer.close();
  	}
  }

  private Map<String, String> getHeaders() {
		Map<String, String> headers = new HashMap<String, String>();
		headers.put("Element1","100");
		headers.put("POLICY_NUMBER","POL_NBR");
		headers.put("Element2","150");
		headers.put("Element3","200");
		headers.put("PRODUCT_TYPE","PA");

		return headers;
	}

	public static void main(String s[]) throws Exception {
		System.out.println("[main] entered");
		AWS_MSK_KafkaPublisher msgPublisher = new AWS_MSK_KafkaPublisher();

		String message = "{\"PaymentDetails\":{\"recordId\":1001001,\"transactionType\":\"payment\",\"transactionTotalAmount\":110,\"transactionDateTime\":\"2023-03-15T12:33:21.463-07:00\",\"status\":\"SUCC\",\"paymentCard\":{\"paymentCardType\":\"VISA\",\"paymentCardExpirationDate\":\"2024-09\",\"paymentCardLastFour\":6785,\"paymentCardZipCode\":80239},\"policy\":{\"agmtNumber\":\"891426-22-1801-01\",\"productType\":\"Auto\"}}}";

		msgPublisher.publish(msgPublisher.getHeaders(), message);
		msgPublisher.closeProducer();
	}
}
