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
package io.jbotsim.gen.dynamic.graph;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.StringTokenizer;
import java.util.TreeSet;
import java.util.Vector;

import io.jbotsim.core.Node;

public class TVG{
    Vector<Node> nodes=new Vector<Node>();
    Vector<TVLink> tvlinks=new Vector<TVLink>();
    Class cNode;
    public TVG(){
        this(Node.class);
    }
    public TVG(Class cNode){
        this.cNode = cNode;
    }
    public void buildFromFile(String filename){
        try{
            BufferedReader in=new BufferedReader(new FileReader(filename));
            StringTokenizer st;
            String sin="";
            do sin=in.readLine(); while (sin.compareTo("vertices")!=0);
            st = new StringTokenizer(sin," ");
            while ((sin=in.readLine()).compareTo("")!=0){
                st = new StringTokenizer(sin," ");
                String id=st.nextToken();
                Node n=(Node)cNode.newInstance();
                n.setProperty("id", id);
                if (st.hasMoreTokens()){
                    double x=Double.parseDouble(st.nextToken());
                    double y=Double.parseDouble(st.nextToken());
                    n.setLocation(x, y);
                }
                addNode(n);
            }

            do sin=in.readLine(); while (sin.compareTo("edges")!=0);
            while ((sin=in.readLine())!=null && !sin.equals("")){
                sin=sin.replaceAll("-", " ");
                st = new StringTokenizer(sin," ");
                Node n1=getNodeById(st.nextToken());
                Node n2=getNodeById(st.nextToken());
                
                TVLink tvl = new TVLink(n1, n2);
                
                while (st.hasMoreTokens()){
                    tvl.addAppearanceDate(new Integer(st.nextToken()));
                    tvl.addDisappearanceDate(new Integer(st.nextToken()));
                }
                tvlinks.add(tvl);
            }
        }catch(Exception e){e.printStackTrace();}        
    }
    public Node getNodeById(String id){
        for (Node n : nodes)
            if (n.getProperty("id").equals(id))
                return n;
        return null;
    }
    public void buildCompleteGraph(Integer N){
        try{
            for (int i=0; i<N; i++){
                Node node = (Node)cNode.newInstance();
                node.setProperty("id", "v"+(new Integer(i)).toString());
                double angle=(2.0*Math.PI/N)*i;
                node.setLocation(200+Math.cos(angle)*150, 200+Math.sin(angle)*150);
                addNode(node);
            }
        }catch (Exception e) {e.printStackTrace();}
        for (int i=0; i<nodes.size(); i++){
            for (int j=i+1; j<nodes.size(); j++){
                TVLink l=new TVLink(nodes.elementAt(i), nodes.elementAt(j));
                addTVLink(l);
            }
        }        
    }
    public void addNode(Node n){
        n.disableWireless();
        nodes.add(n);
    }
    public void addTVLink(TVLink l){
        tvlinks.add(l);
    }
    public Vector<Node> getNodes(){
        return new Vector<Node>(nodes);
    }
    public Vector<TVLink> getTVLinks(){
        return new Vector<TVLink>(tvlinks);
    }
    public int nbDates(){
        TreeSet<Integer> allDates=new TreeSet<Integer>();
        for (TVLink l : tvlinks){
            allDates.addAll(l.appearanceDates);
            allDates.addAll(l.disappearanceDates);
        }
        return allDates.size();
    }
    public String toString(){
        String s="";
        for (TVLink l : tvlinks)
            s += l.toString()+"\n";
        return s;
    }
}
