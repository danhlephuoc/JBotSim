/*
 * Copyright 2008 - 2020, Arnaud Casteigts and the JBotSim contributors <contact@jbotsim.io>
 *
 *
 * This file is part of JBotSim.
 *
 * JBotSim is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * JBotSim is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with JBotSim.  If not, see <https://www.gnu.org/licenses/>.
 *
 */

package examples.tools;

import io.jbotsim.core.Topology;
import io.jbotsim.gen.dynamic.trace.TraceRecorder;
import io.jbotsim.io.format.xml.XMLTraceBuilder;
import io.jbotsim.ui.JViewer;

public class JBotSimRecorder {
    public static void main(String[] args) {
        if (args.length < 2) {
            System.err.println("usage: " + JBotSimRecorder.class.getName() + " input-file");
            System.exit(1);
        }
        String traceFileName = args[0];
        try {
            Topology tp = new Topology();
            TraceRecorder tr = new TraceRecorder(tp, new XMLTraceBuilder(tp));

            Runtime.getRuntime().addShutdownHook(new Thread() {
                @Override
                public void run() {
                    try {
                        tr.stopAndWrite(traceFileName);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
            tr.start();
            new JViewer(tp);
        } catch(Exception e) {
            e.printStackTrace();
        }
    }
}
