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

package com.mq.core.core.producer;

import com.google.gson.Gson;
import lombok.SneakyThrows;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;

/**
 * @author lw1243925457
 */
public class WebsocketProducer implements Producer {

    private WebSocketClient client;
    private Gson gson = new Gson();

    @SneakyThrows
    public WebsocketProducer(String url) {
        client = new WebSocketClient(new URI(url)) {
            @Override
            public void onOpen(ServerHandshake serverHandshake) {
                System.out.println("producer websocket client connected");
            }

            @Override
            public void onMessage(String s) {
                System.out.println("receive message : " + s);
            }

            @Override
            public void onClose(int i, String s, boolean b) {
                System.out.println("producer websocket client close");
            }

            @Override
            public void onError(Exception e) {

            }
        };
        System.out.println(client.connectBlocking());
    }

    @Override
    public void send(String topic, String message) {
        Map<String, Object> map = new HashMap<>(2);
        map.put("topic", topic);
        map.put("message", message);
        client.send(gson.toJson(map));
    }

    public void close() {
        client.close();
    }
}
