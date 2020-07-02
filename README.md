# url-processor
URL Processor using a Pub Sub model



##### Steps #####

1. Download the code and execute mvn clean compile install. This will generate a jar with dependencies which should be used.
2. To execute, pass in the following command line arguments:
java -cp url.processor-0.0.1-SNAPSHOT-jar-with-dependencies.jar com.ebay.url.processor.Application "<Input directory>"
3. The system will print out the total urls processed on the console.
4. The application expects gzip files composed of URLs.







