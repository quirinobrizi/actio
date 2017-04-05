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
package org.actio.modeler.infrastructure.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author quirino.brizi
 *
 */
@Component
@ConfigurationProperties(prefix = "actio.modeler")
public class ModelerConfigurationProperties {

    private String language;
    private Engine engine = new Engine();

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public Engine getEngine() {
        return engine;
    }

    public static class Engine {
    	private String urlFormat;
    	private String username;
    	private String password;
    
    	public String getUrlFormat() {
    		return urlFormat;
    	}
    
    	public void setUrlFormat(String urlFormat) {
    		this.urlFormat = urlFormat;
    	}
    
    	public String getUsername() {
    		return username;
    	}
    
    	public void setUsername(String username) {
    		this.username = username;
    	}
    
    	public String getPassword() {
    		return password;
    	}
    
    	public void setPassword(String password) {
    		this.password = password;
    	}
    }
}
