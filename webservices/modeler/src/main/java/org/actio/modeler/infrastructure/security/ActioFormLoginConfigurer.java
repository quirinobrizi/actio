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
package org.actio.modeler.infrastructure.security;

import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashMap;

import javax.servlet.http.HttpServletRequest;

import org.actio.modeler.infrastructure.config.ModelerConfigurationProperties;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationDetailsSource;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.HttpSecurityBuilder;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.ExceptionHandlingConfigurer;
import org.springframework.security.config.annotation.web.configurers.HttpBasicConfigurer;
import org.springframework.security.config.annotation.web.configurers.LogoutConfigurer;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.DelegatingAuthenticationEntryPoint;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.authentication.RememberMeServices;
import org.springframework.security.web.authentication.logout.HttpStatusReturningLogoutSuccessHandler;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.security.web.util.matcher.AndRequestMatcher;
import org.springframework.security.web.util.matcher.MediaTypeRequestMatcher;
import org.springframework.security.web.util.matcher.NegatedRequestMatcher;
import org.springframework.security.web.util.matcher.OrRequestMatcher;
import org.springframework.security.web.util.matcher.RequestHeaderRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.web.accept.ContentNegotiationStrategy;
import org.springframework.web.accept.HeaderContentNegotiationStrategy;

/**
 * Based on {@link HttpBasicConfigurer}
 * 
 * @author quirino.brizi
 *
 */
public class ActioFormLoginConfigurer<B extends HttpSecurityBuilder<B>> extends AbstractHttpConfigurer<ActioFormLoginConfigurer<B>, B> {

    private static final RequestHeaderRequestMatcher X_REQUESTED_WITH = new RequestHeaderRequestMatcher("X-Requested-With",
            "XMLHttpRequest");

    private AuthenticationEntryPoint authenticationEntryPoint;
    private AuthenticationDetailsSource<HttpServletRequest, ?> authenticationDetailsSource;
    private ActioAuthenticationEntrypoint actioAuthenticationEntrypoint = new ActioAuthenticationEntrypoint();

    private ModelerConfigurationProperties modelerConfiguration;

    public ActioFormLoginConfigurer(ModelerConfigurationProperties modelerConfiguration) {
        this.modelerConfiguration = modelerConfiguration;
        LinkedHashMap<RequestMatcher, AuthenticationEntryPoint> entryPoints = new LinkedHashMap<>();
        entryPoints.put(X_REQUESTED_WITH, new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED));
        DelegatingAuthenticationEntryPoint defaultEntryPoint = new DelegatingAuthenticationEntryPoint(entryPoints);
        defaultEntryPoint.setDefaultEntryPoint(this.actioAuthenticationEntrypoint);
        this.authenticationEntryPoint = defaultEntryPoint;
    }

    @Override
    public void init(B http) throws Exception {
        registerDefaults(http);
    }

    private void registerDefaults(B http) {
        ContentNegotiationStrategy contentNegotiationStrategy = http.getSharedObject(ContentNegotiationStrategy.class);
        if (contentNegotiationStrategy == null) {
            contentNegotiationStrategy = new HeaderContentNegotiationStrategy();
        }

        MediaTypeRequestMatcher restMatcher = new MediaTypeRequestMatcher(contentNegotiationStrategy, MediaType.APPLICATION_ATOM_XML,
                MediaType.APPLICATION_FORM_URLENCODED, MediaType.APPLICATION_JSON, MediaType.APPLICATION_OCTET_STREAM,
                MediaType.APPLICATION_XML, MediaType.MULTIPART_FORM_DATA, MediaType.TEXT_XML);
        restMatcher.setIgnoredMediaTypes(Collections.singleton(MediaType.ALL));

        RequestMatcher notHtmlMatcher = new NegatedRequestMatcher(
                new MediaTypeRequestMatcher(contentNegotiationStrategy, MediaType.TEXT_HTML));
        RequestMatcher restNotHtmlMatcher = new AndRequestMatcher(Arrays.<RequestMatcher>asList(notHtmlMatcher, restMatcher));

        RequestMatcher preferredMatcher = new OrRequestMatcher(Arrays.asList(X_REQUESTED_WITH, restNotHtmlMatcher));

        registerDefaultEntryPoint(http, preferredMatcher);
        registerDefaultLogoutSuccessHandler(http, preferredMatcher);
    }

    @SuppressWarnings("unchecked")
    private void registerDefaultEntryPoint(B http, RequestMatcher preferredMatcher) {
        ExceptionHandlingConfigurer<B> exceptionHandling = http.getConfigurer(ExceptionHandlingConfigurer.class);
        if (exceptionHandling == null) {
            return;
        }
        exceptionHandling.defaultAuthenticationEntryPointFor(postProcess(this.authenticationEntryPoint), preferredMatcher);
    }

    @SuppressWarnings("unchecked")
    private void registerDefaultLogoutSuccessHandler(B http, RequestMatcher preferredMatcher) {
        LogoutConfigurer<B> logout = http.getConfigurer(LogoutConfigurer.class);
        if (logout == null) {
            return;
        }
        logout.defaultLogoutSuccessHandlerFor(postProcess(new HttpStatusReturningLogoutSuccessHandler(HttpStatus.NO_CONTENT)),
                preferredMatcher);
    }

    @Override
    public void configure(B http) throws Exception {
        AuthenticationManager authenticationManager = http.getSharedObject(AuthenticationManager.class);
        ActioAuthenticationFilter actioAuthenticationFilter = new ActioAuthenticationFilter(authenticationManager,
                this.authenticationEntryPoint);
        if (this.authenticationDetailsSource != null) {
            actioAuthenticationFilter.setAuthenticationDetailsSource(this.authenticationDetailsSource);
        }
        RememberMeServices rememberMeServices = http.getSharedObject(RememberMeServices.class);
        if (rememberMeServices != null) {
            actioAuthenticationFilter.setRememberMeServices(rememberMeServices);
        }
        actioAuthenticationFilter = postProcess(actioAuthenticationFilter);
        http.addFilterBefore(actioAuthenticationFilter, BasicAuthenticationFilter.class);
    }
}
