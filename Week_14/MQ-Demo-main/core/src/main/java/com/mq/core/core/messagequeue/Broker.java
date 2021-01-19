/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.mq.core.core.messagequeue;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * @author lw1243925457
 */
@Component
@Slf4j
public class Broker {

    Map<String, ConcurrentLinkedQueue> queueMap = new HashMap<>();

    public boolean send(String topic, String content) {
//        log.info("receive message : " + content + " from topic : " + topic);

        if(!queueMap.containsKey(topic)) {
            queueMap.put(topic, new ConcurrentLinkedQueue());
        }

        queueMap.get(topic).add(content);

        return true;
    }

    public List<String> poll(String topic, int rate) {
//        log.info("poll data to : " + topic);

        ConcurrentLinkedQueue queue = queueMap.get(topic);

        List<String> messages = new ArrayList<>();
        if (queue == null) {
            return messages;
        }

        log.info("queue message amount : " + queue.size());
        while (!queue.isEmpty() || rate > 0) {
            messages.add(Objects.requireNonNull(queue.poll()).toString());
            rate -= 1;
        }

        return messages;
    }
}
