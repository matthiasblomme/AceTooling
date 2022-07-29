package com.id.ace.environment.overview;

import com.id.ace.models.MQQueue;
import com.id.ace.models.MQSubscription;
import com.id.ace.utils.Command;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MqEnvironment {

    String queueNameTypePattern = "DEFINE Q(.*?)\\(\\'(.*?)\\'.*";
    String queueTargetPattern = ".*TARGET\\(\\'(.*?)\\'.*";
    String queueDescriptionPattern = ".*DESCR\\(\\'(.*?)\\'.*";
    String queuePersistencePattern = ".*DEFPSIST\\(\\(.*?\\)\\).*";
    String queueBackoutQueuePattern = ".*BOQNAME\\(\\'(.*?)\\'.*";
    String subNameTopicDestPattern = "DEFINE\\sSUB\\(\\'(.*?)\\'\\)\\sTOPICSTR\\(\\'(.*?)\\'\\)\\sDEST\\(\\'(.*?)\\'\\).*";
    HashMap<String, MQQueue> queueMap= new HashMap<String, MQQueue>();
    HashMap<String, MQSubscription> subscriptionMap= new HashMap<String, MQSubscription>();

    public void getQueueList(){
        String[] commandString = {"-m", "V12QMGR", "-t", "queue", "-o", "1line", "-x", "object"};
        Command command = new Command();
        int exitVal = command.Exec("dmpmqcfg", commandString);

        ArrayList<String> output = command.getOutput();
        //ArrayList<String> error = command.getError();

        for(String line : output) parseQueueInfo(line);

    }

    public void getSubscriptionList(){
        String[] commandString = {"-m", "V12QMGR", "-t", "all", "-o", "1line", "-x", "sub"};
        Command command = new Command();
        int exitVal = command.Exec("dmpmqcfg", commandString);

        ArrayList<String> output = command.getOutput();
        //ArrayList<String> error = command.getError();

        for(String line : output) parseSubscriptionInfo(line);
    }


    private void parseQueueInfo(String queueInfo) {
        if (queueInfo.startsWith("*")) return;
        Pattern p = Pattern.compile(queueNameTypePattern);
        Matcher m =  p.matcher(queueInfo);
        MQQueue tempQueue = new MQQueue();
        if(m.matches()) {
            MQQueue.QueueType queueTypeEnum = MQQueue.QueueType.valueOf(m.group(1));
            tempQueue.setType(queueTypeEnum);
            tempQueue.setName(m.group(2));

            //DEFINE QALIAS('ALIASQUEUE') TARGET('VERLONING.DATA.XML.IN') REPLACE
            //DEFINE QLOCAL('ZEXAMPLE') BOQNAME('SYSTEM.DEAD.LETTER.QUEUE') DESCR('example queue') DISTL(NO) MAXDEPTH(5000) REPLACE
            //DEFINE QREMOTE('SYSTEM.DEFAULT.REMOTE.QUEUE') XMITQ(' ') REPLACE

            switch (queueTypeEnum) {
                case LOCAL:
                    m = Pattern.compile(queueBackoutQueuePattern).matcher(queueInfo);
                    if (m.matches()) tempQueue.setBackoutqueue(m.group(1));

                    m = Pattern.compile(queueDescriptionPattern).matcher(queueInfo);
                    if (m.matches()) tempQueue.setDescription(m.group(1));

                    m = Pattern.compile(queuePersistencePattern).matcher(queueInfo);
                    if (m.matches()) tempQueue.setPersistent(m.group(1));

                    break;
                case ALIAS:
                    m = Pattern.compile(queueDescriptionPattern).matcher(queueInfo);
                    if (m.matches()) tempQueue.setDescription(m.group(1));

                    m = Pattern.compile(queuePersistencePattern).matcher(queueInfo);
                    if (m.matches()) tempQueue.setPersistent(m.group(1));

                    m = Pattern.compile(queueTargetPattern).matcher(queueInfo);
                    if (m.matches()) tempQueue.setTarget(m.group(1));

                    break;
                case REMOTE:
                    m = Pattern.compile(queueDescriptionPattern).matcher(queueInfo);
                    if (m.matches()) tempQueue.setDescription(m.group(1));

                    m = Pattern.compile(queuePersistencePattern).matcher(queueInfo);
                    if (m.matches()) tempQueue.setPersistent(m.group(1));

                    break;
            }
        }
        if (tempQueue != null) queueMap.put(tempQueue.getName(), tempQueue);
    }


    private void parseSubscriptionInfo(String subscriptionInfo) {
        if (subscriptionInfo.startsWith("*")) return;
        Pattern p = Pattern.compile(subNameTopicDestPattern);
        Matcher m = p.matcher(subscriptionInfo);
        MQSubscription tempSub = null;
        if (m.matches()) {
            tempSub = new MQSubscription();
            tempSub.setName(m.group(1));
            tempSub.setTopicstring(m.group(2));
            tempSub.setDestination(m.group(3));
        }
        if (tempSub != null) subscriptionMap.put(tempSub.getName(), tempSub);
    }

    public HashMap<String, MQQueue> getQueueMap() {
        return queueMap;
    }

    public HashMap<String, MQSubscription> getSubcriptionMap() {
        return subscriptionMap;
    }
}
