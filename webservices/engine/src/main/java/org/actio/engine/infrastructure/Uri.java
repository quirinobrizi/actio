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
package org.actio.engine.infrastructure;

import java.net.URI;
import java.net.URISyntaxException;

import org.actio.engine.infrastructure.exception.InternalServerErrorException;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

/**
 * Wrapper for {@link URI} provide additional method to parse retrieve info from
 * a give URI representation.
 * 
 * @author quirino.brizi
 *
 */
public class Uri {

    private UriComponents components;

    public static Uri from(String uri) {
        return new Uri(uri);
    }

    private Uri(String uri) {
        this.components = UriComponentsBuilder.fromUri(URI.create(uri)).build();
    }

    public String getQueryParameter(String name) {
        return this.components.getQueryParams().getFirst(name);
    }

    public URI removeQueryString() {
        try {
            return new URI(this.components.getScheme(), null, this.components.getHost(), this.components.getPort(),
                    this.components.getPath(), null, null);
        } catch (URISyntaxException e) { // NOSONAR
            throw InternalServerErrorException.newInstance(e.getMessage());
        }
    }
}
