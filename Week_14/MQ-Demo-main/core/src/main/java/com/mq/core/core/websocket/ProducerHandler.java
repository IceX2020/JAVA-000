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

package com.mq.core.core.websocket;

import com.google.gson.Gson;
import com.mq.core.core.messagequeue.Broker;
import org.springframework.web.socket.*;

import java.util.HashMap;
import java.util.Map;

/**
 * @author lw1243925457
 */
public class ProducerHandler implements WebSocketHandler {

    private static Map<WebSocketSession, Map> clients = new HashMap<>();
    private final Broker broker;
    private Gson gson = new Gson();

    ProducerHandler(Broker broker) {
        this.broker = broker;
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession webSocketSession) throws Exception {
        clients.put(webSocketSession, new HashMap(0));
    }

    @Override
    public void handleMessage(WebSocketSession webSocketSession, WebSocketMessage<?> webSocketMessage) throws Exception {
//        System.out.println(webSocketMessage.getPayload().toString());
        Map map = gson.fromJson(webSocketMessage.getPayload().toString(), Map.class);
        broker.send(map.get("topic").toString(), map.get("message").toString());
    }

    @Override
    public void handleTransportError(WebSocketSession webSocketSession, Throwable throwable) throws Exception {
        clients.remove(webSocketSession);
        webSocketSession.sendMessage(new TextMessage(throwable.toString()));
    }

    @Override
    public void afterConnectionClosed(WebSocketSession webSocketSession, CloseStatus closeStatus) throws Exception {
        clients.remove(webSocketSession);
        webSocketSession.sendMessage(new TextMessage("Close connect"));
    }

    @Override
    public boolean supportsPartialMessages() {
        return false;
    }
}
