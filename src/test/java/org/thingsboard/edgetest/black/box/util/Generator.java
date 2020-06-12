package org.thingsboard.edgetest.black.box.util;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

@Slf4j
public class Generator {

    private static Random random = new Random();

    @Test
    public static String generateContent() {
        StringBuilder content = new StringBuilder();
        content.append("{");
        addKeysAndValuesToContent(content);
        content.append("}");
        log.debug(content.toString());
        return content.toString();
    }

    @Test
    public static String generateContentWithTs() {
        long ts = System.currentTimeMillis();
        StringBuilder content = new StringBuilder();
        content.append("{'ts':").append(ts).append(",'values':{");
        addKeysAndValuesToContent(content);
        content.append("}}");
        log.debug(content.toString());
        return content.toString();
    }

    private static void addKeysAndValuesToContent(StringBuilder content) {
        List<String> keys = Arrays.asList("key1", "key2", "key3");
        Iterator<String> iterator = keys.iterator();
        Random random = new Random();
        while (iterator.hasNext()) {
            String key = iterator.next();
            int value = random.nextInt(101);
            content.append("'").append(key).append("'").append(":").append(value);
            if(iterator.hasNext()) {
                content.append(",");
            }
        }
    }

}
