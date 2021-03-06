/**
 * See the NOTICE file distributed with this work
 * for additional information regarding copyright ownership.
 * Board of Regents of the University of Wisconsin System
 * licenses this file to you under the Apache License,
 * Version 2.0 (the "License"); you may not use this file
 * except in compliance with the License. You may obtain a
 * copy of the License at:
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package com.microsoft.exchange.integration;
import static org.junit.Assert.*;

import org.apache.http.auth.Credentials;
import org.apache.http.auth.NTCredentials;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.microsoft.exchange.impl.http.ThreadLocalCredentialsProviderFactory;
import com.microsoft.exchange.messages.FindFolder;
import com.microsoft.exchange.messages.FindFolderResponse;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations="classpath:/com/microsoft/exchange/exchangeContext-usingNtlmCredentials.xml")
public class NtlmCredentialsIntegrationTest extends AbstractIntegrationTest {

	
	@Value("${username}")
	private String userName;
	
	@Value("${password}")
	private String password;
	
	@Value("${domain}")
	private String domain;
	
	@Test
	public void isAutowired()  {
		assertNotNull(ewsClient);
	
	}

	@Override
	public void initializeCredentials() {
		Credentials credentials = new NTCredentials(userName, password, "", domain);
		ThreadLocalCredentialsProviderFactory.set(credentials);
	}
	
	@Test @Override
	public void getPrimaryCalendarFolder() {
		super.getPrimaryCalendarFolder();
	}
	
	@Test
	public void findFolders() {
		super.findFolders();
	}
	
}
