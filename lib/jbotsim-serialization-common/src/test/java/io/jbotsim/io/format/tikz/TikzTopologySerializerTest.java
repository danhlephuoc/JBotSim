/*
 * Copyright 2008 - 2019, Arnaud Casteigts and the JBotSim contributors <contact@jbotsim.io>
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

package io.jbotsim.io.format.tikz;

import io.jbotsim.core.Topology;
import io.jbotsim.io.format.dot.DotTopologySerializer;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Collection;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;


@RunWith(Parameterized.class)
public class TikzTopologySerializerTest {

    protected static final String RESULT_EXTENSION = "-res.tikz";

    private static final boolean UPDATE_RESULTS = false;
    private static final String TEST_RC_ROOT = "/tikzinputs/";

    @Parameterized.Parameter
    public String inputFileName;

    @Parameterized.Parameters(name="{index}: {0}")
    public static Collection<String> makers() {
        return Arrays.asList(
                "syntax-04.xdot",
                "syntax-02.xdot",
                "syntax-01.gv",
                "syntax-02.gv",
                "syntax-03.gv",
                "syntax-01.xdot",
                "syntax-03.xdot",
                "hm-01.gv",
                "arboricity-100-2.gv",
                "barbell-6-5-4.gv",
                "cactus-20.gv",
                "kstar-20-2.gv",
                "gabriel-50.gv",
                "paley-10.gv",
                "paley-10.xdot",
                "sunlet-10-directed.gv");
    }

    private void updateExpectedResult(String result) throws IOException {
        File resFile = new File(inputFileName + RESULT_EXTENSION);

        if(!resFile.exists())
            resFile.createNewFile();

        FileOutputStream out = new FileOutputStream(resFile);
        out.write(result.getBytes());
        out.flush();
        out.close();
    }

    @Test
    public void exportToString() throws IOException {
        URL url = getClass().getResource(TEST_RC_ROOT + inputFileName);
        Topology tp = importTestTopology(url);

        String generatedTikz = new TikzTopologySerializer().exportToString(tp);
        assertNotNull(generatedTikz);

        if (UPDATE_RESULTS)
            updateExpectedResult(generatedTikz);

        String expectedTikz = loadExpectedResult(url);

        assertEquals(expectedTikz, generatedTikz);
    }

    private String loadExpectedResult(URL url) throws IOException {
        return new String(Files.readAllBytes(Paths.get(url.getPath()+ RESULT_EXTENSION)));
    }

    private Topology importTestTopology(URL url) {
        Topology tp = new Topology();

        String fileContent = tp.getFileManager().read(url.getPath());
        new DotTopologySerializer().importFromString(tp, fileContent);
        assertNotNull(tp);
        return tp;
    }
}