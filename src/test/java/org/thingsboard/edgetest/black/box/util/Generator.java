package org.thingsboard.edgetest.black.box.util;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

public class Generator {

    private static Random random = new Random();

    public static String generateRandomContent() {
        StringBuilder content = new StringBuilder();
        content.append("{");
        List<String> keys = Arrays.asList("key1", "key2", "key3");
        Iterator<String> iterator = keys.iterator();
        while (iterator.hasNext()) {
            String key = iterator.next();
            int value = random.nextInt(101);
            content.append("'").append(key).append("'").append(":").append(value);
            if(iterator.hasNext()) {
                content.append(",");
            }
        }
        content.append("}");
        return content.toString();
    }

}
