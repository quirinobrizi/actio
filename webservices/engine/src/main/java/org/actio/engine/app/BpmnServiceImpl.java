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
package org.actio.engine.app;

import java.util.List;

import org.actio.engine.domain.model.bpmn.Bpmn;
import org.actio.engine.domain.model.bpmn.BpmnId;
import org.actio.engine.domain.repository.BpmnRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author quirino.brizi
 *
 */
@Service
public class BpmnServiceImpl implements BpmnService {

    @Autowired
    private BpmnRepository bpmnRepository;

    /*
     * (non-Javadoc)
     * 
     * @see org.actio.engine.app.BpmnService#getAllBpmns()
     */
    @Override
    public List<Bpmn> getAllBpmns() {
        return bpmnRepository.getAll();
    }

    @Override
    public void deleteBpmn(BpmnId bpmnId) {
        bpmnRepository.remove(bpmnId);
    }

}
