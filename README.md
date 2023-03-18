# standalones

**JsonAppender** - When you receive various types of JSON string with its own structure and elements, and want to include a common set of additional attributes to all these JSON strings, this class can be referred. Additional attributes will be included to the end of any type of JSON string.
**compilation**: javac -cp lib/* JsonAppender.java
**execution**: java -cp ".;lib/*" JsonAppender


**AWS_MSK_KafkaPublisher** - This class create Kafkaproducer with required properties, SASL_SSL as the security protocol and publish to AWS MSK.
**compilation**: javac -cp lib/* AWS_MSK_KafkaPublisher.java
**execution**: java -cp ".;lib/*" AWS_MSK_KafkaPublisher
