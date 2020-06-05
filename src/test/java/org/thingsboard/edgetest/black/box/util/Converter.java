package org.thingsboard.edgetest.black.box.util;

import lombok.extern.slf4j.Slf4j;
import org.thingsboard.server.common.data.kv.TsKvEntry;

import java.util.Iterator;
import java.util.List;

@Slf4j
public class Converter {

    /**
     * Convert latest timeseries to string view
     */

    public static String tsKvEntryToString(List<TsKvEntry> tsKvEntries) {
        Iterator<TsKvEntry> iterator = tsKvEntries.iterator();
        StringBuilder content = new StringBuilder();
        content.append("{");
        while (iterator.hasNext()) {
            TsKvEntry tsKvEntry = iterator.next();
            content.append("'").append(tsKvEntry.getKey()).append("'").append(":").append(tsKvEntry.getValueAsString());
            if(iterator.hasNext()) {
                content.append(",");
            }
        }
        content.append("}");
        return content.toString();
    }
}
