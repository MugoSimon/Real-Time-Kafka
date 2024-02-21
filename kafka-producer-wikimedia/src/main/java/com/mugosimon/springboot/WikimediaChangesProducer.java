package com.mugosimon.springboot;

import com.launchdarkly.eventsource.EventSource;
import com.launchdarkly.eventsource.background.BackgroundEventHandler;
import com.launchdarkly.eventsource.background.BackgroundEventSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.util.concurrent.TimeUnit;

@Service
public class WikimediaChangesProducer {

    private static final Logger LOGGER = LoggerFactory.getLogger(WikimediaChangesProducer.class);
    private KafkaTemplate<String, String> kafkaTemplate;

    public WikimediaChangesProducer(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendMessage() throws InterruptedException {
        String topic = "wikimedia_recentChange";
        String url = "https://stream.wikimedia.org/v2/stream/recentchange";
        //To read real time String data from wikimedia, we use event source
        BackgroundEventHandler eventHandler =  new WikimediaChangeHandler(kafkaTemplate,topic);
        URI uriUrl = URI.create(url);
        EventSource.Builder esBuilder = new EventSource.Builder(uriUrl);
        BackgroundEventSource.Builder eventSource = new BackgroundEventSource.Builder(eventHandler,esBuilder);
        BackgroundEventSource source = eventSource.build();
        source.start();

        TimeUnit.MINUTES.sleep(10);
    }
}
