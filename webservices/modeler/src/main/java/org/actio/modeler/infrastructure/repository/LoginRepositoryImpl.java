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
package org.actio.modeler.infrastructure.repository;

import org.actio.modeler.domain.repository.LoginRepository;
import org.springframework.stereotype.Repository;

/**
 * @author quirino.brizi
 *
 */
@Repository
public class LoginRepositoryImpl implements LoginRepository {

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.actio.modeler.domain.repository.LoginRepository#authenticate(java.
     * lang.String, java.lang.String)
     */
    @Override
    public String authenticate(String username, String password) {
        // TODO Auto-generated method stub
        return null;
    }

}
