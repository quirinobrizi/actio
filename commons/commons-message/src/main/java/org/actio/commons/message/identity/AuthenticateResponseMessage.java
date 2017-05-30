/*******************************************************************************
 * Copyright [2016] [Quirino Brizi (quirino.brizi@gmail.com)]
 *  
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *  
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *******************************************************************************/
package org.actio.commons.message.identity;

import org.actio.commons.message.Message;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author quirino.brizi
 *
 */
public class AuthenticateResponseMessage implements Message {

    private static final long serialVersionUID = 8170238734220872030L;

    @JsonProperty("expiryTime")
    private long expiryTime;
    @JsonProperty("user")
    private UserMessage user;

    @JsonCreator
    public AuthenticateResponseMessage(@JsonProperty("expiryTime") long expiryTime, @JsonProperty("user") UserMessage user) {
        this.expiryTime = expiryTime;
        this.user = user;
    }
}
