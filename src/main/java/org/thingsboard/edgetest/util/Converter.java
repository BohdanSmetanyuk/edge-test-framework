package org.thingsboard.edgetest.util;

import org.thingsboard.server.common.data.kv.TsKvEntry;

import java.util.ArrayList;
import java.util.List;

public class Converter {

    public static List<String> convertTsKvEntryListToSimpleStringList(List<TsKvEntry> tsKvEntryList, int numberOfKeys) {
        List<String> simpleStringList = new ArrayList<>();

        int sizeOfTsKvEntrySublist = tsKvEntryList.size()/numberOfKeys;

        for (int i = sizeOfTsKvEntrySublist-1; i >= 0; i--) {
            StringBuilder simpleString = new StringBuilder();
            for(int j=0; j<numberOfKeys; j++) {
                TsKvEntry tsKvEntry = tsKvEntryList.get(i+j*sizeOfTsKvEntrySublist);
                simpleString.append(tsKvEntry.getKey()).append(tsKvEntry.getValueAsString());
            }
            simpleStringList.add(simpleString.toString());
        }
        return simpleStringList;
    }

    public static String convertContentToSimpleString(String content) {
        return content.replaceAll("\\W", "");
    }

    public static String convertContentWithTsToSimpleString(String content) {
        return content.replaceAll("[{:',}]", "").split("values")[1];
    }
}
