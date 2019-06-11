package org.yeyu.protocol;


import org.apache.commons.exec.LogOutputStream;

import java.util.LinkedList;
import java.util.List;

public class CollectingLogOutputStream extends LogOutputStream {
    private final List<String> lines = new LinkedList<String>();
    @Override
    protected void processLine(String s, int i) {
        lines.add(s);
    }

    public List<String> getLines() {
        return lines;
    }
}
