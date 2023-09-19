package com.id.ace.utils;

import com.ibm.mq.*;
import com.ibm.mq.constants.MQConstants;

import java.io.IOException;

public class DLQTest {
    public static void main(String[] args) throws IOException {
        MQQueueManager qMgr = null;
        MQQueue queue = null;

        try {
            // Connect to queue manager
            qMgr = new MQQueueManager("QMGR");

            // Open a queue to put a message (Assuming the queue doesn't exist)
            queue = qMgr.accessQueue("INVALID.QUEUE", MQConstants.MQOO_OUTPUT);

            // Create and put a message onto the invalid queue
            MQMessage message = new MQMessage();
            message.writeString("This is a test message.");
            MQPutMessageOptions pmo = new MQPutMessageOptions();
            queue.put(message, pmo);
        } catch (MQException ex) {
            ex.printStackTrace();
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