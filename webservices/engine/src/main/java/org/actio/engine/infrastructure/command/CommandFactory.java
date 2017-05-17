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
package org.actio.engine.infrastructure.command;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;

import org.actio.engine.infrastructure.command.Command.Type;
import org.springframework.stereotype.Component;

/**
 * @author quirino.brizi
 *
 */
@Component
public class CommandFactory {

    private final Map<Command.Type, Command<?, ?>> commands;

    public CommandFactory(List<Command<?, ?>> commands) {
        this.commands = new EnumMap<>(Command.Type.class);
        for (Command<?, ?> command : commands) {
            this.commands.put(command.type(), command);
        }
    }

    public Command<?, ?> get(Type type) {
        if (this.commands.containsKey(type)) {
            return this.commands.get(type);
        }
        throw new IllegalArgumentException("unable to find requeted command");
    }
}
