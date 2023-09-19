package com.id.ace.utils;

import com.ibm.mq.*;
import com.ibm.mq.constants.MQConstants;

public class DLQTest2 {
    public static void main(String[] args) {
        MQQueueManager qMgr = null;
        MQQueue queue = null;

        try {
            // Connect to queue manager
            qMgr = new MQQueueManager("QMGR");
            System.out.println("Connected to " + qMgr.getName());

            // Open an existing queue
            queue = qMgr.accessQueue("OUT3", MQConstants.MQOO_INPUT_AS_Q_DEF);

            // Attempt to get a message with a short wait time
            MQMessage message = new MQMessage();
            MQGetMessageOptions gmo = new MQGetMessageOptions();
            gmo.options = MQConstants.MQGMO_WAIT;
            gmo.waitInterval = 2000;  // 2 seconds wait time

            queue.get(message, gmo);

        } catch (MQException ex) {
            if (ex.reasonCode == MQConstants.MQRC_NO_MSG_AVAILABLE) {
                System.out.println("Message retrieval timed out.");
            } else {
                ex.printStackTrace();
            }
        } finally {
            try {
                if (queue != null) {
                    queue.close();
                }
                if (qMgr != null) {
                    qMgr.disconnect();
                }
            } catch (MQException e) {
                e.printStackTrace();
            }
        }
    }
}
