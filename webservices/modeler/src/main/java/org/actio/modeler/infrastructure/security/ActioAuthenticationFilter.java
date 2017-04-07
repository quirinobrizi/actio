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

import java.io.IOException;
import java.util.Map;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.AuthenticationDetailsSource;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.NullRememberMeServices;
import org.springframework.security.web.authentication.RememberMeServices;
import org.springframework.web.filter.OncePerRequestFilter;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * @author quirino.brizi
 *
 */
public class ActioAuthenticationFilter extends OncePerRequestFilter {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    private AuthenticationManager authenticationManager;
    private AuthenticationEntryPoint authenticationEntryPoint;
    private RememberMeServices rememberMeServices = new NullRememberMeServices();
    private boolean ignoreFailure = false;
    private String credentialsCharset = "UTF-8";
    private AuthenticationDetailsSource<HttpServletRequest, ?> authenticationDetailsSource;

    public ActioAuthenticationFilter(AuthenticationManager authenticationManager, AuthenticationEntryPoint authenticationEntryPoint) {
        this.authenticationManager = authenticationManager;
        this.authenticationEntryPoint = authenticationEntryPoint;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.springframework.web.filter.OncePerRequestFilter#doFilterInternal(
     * javax.servlet.http.HttpServletRequest,
     * javax.servlet.http.HttpServletResponse, javax.servlet.FilterChain)
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String xActioUsername = request.getHeader("X-Actio-Username");
        if (StringUtils.isNotBlank(xActioUsername)) {
            if (authenticationIsRequired(xActioUsername)) {
                SecurityContextHolder.clearContext();
                this.rememberMeServices.loginFail(request, response);
                onUnsuccessfulAuthentication(request, response, new UsernameNotFoundException(xActioUsername));
                this.authenticationEntryPoint.commence(request, response, new UsernameNotFoundException(xActioUsername));
            }
            filterChain.doFilter(request, response);
            return;
        } else {
            ServletInputStream inputStream = request.getInputStream();
            if (null != inputStream && inputStream.available() > 0) {
                Map<String, String> body = MAPPER.readValue(inputStream, new TypeReference<Map<String, String>>() {
                });
                if (null == body || bodyDoesNotContainAuthenticationDetails(body)) {
                    filterChain.doFilter(request, response);
                    return;
                }
                String username = body.get("username");
                String password = body.get("password");
                try {
                    if (authenticationIsRequired(username)) {
                        UsernamePasswordAuthenticationToken authRequest = new UsernamePasswordAuthenticationToken(username, password);
                        if (null != authenticationDetailsSource) {
                            authRequest.setDetails(this.authenticationDetailsSource.buildDetails(request));
                        }
                        Authentication authResult = this.authenticationManager.authenticate(authRequest);
                        SecurityContextHolder.getContext().setAuthentication(authResult);
                        this.rememberMeServices.loginSuccess(request, response, authResult);
                        onSuccessfulAuthentication(request, response, authResult);
                    }
                } catch (AuthenticationException e) {
                    SecurityContextHolder.clearContext();
                    this.rememberMeServices.loginFail(request, response);
                    onUnsuccessfulAuthentication(request, response, e);
                    this.authenticationEntryPoint.commence(request, response, e);
                }
                filterChain.doFilter(request, response);
            } else {
                filterChain.doFilter(request, response);
            }
        }
    }

    public void setAuthenticationDetailsSource(AuthenticationDetailsSource<HttpServletRequest, ?> authenticationDetailsSource) {
        this.authenticationDetailsSource = authenticationDetailsSource;
    }

    public void setRememberMeServices(RememberMeServices rememberMeServices) {
        this.rememberMeServices = rememberMeServices;
    }

    protected void onSuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, Authentication authResult)
            throws IOException {
    }

    protected void onUnsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed)
            throws IOException {
    }

    private boolean authenticationIsRequired(String username) {
        // Only reauthenticate if username doesn't match SecurityContextHolder
        // and user
        // isn't authenticated
        // (see SEC-53)
        Authentication existingAuth = SecurityContextHolder.getContext().getAuthentication();

        if (existingAuth == null || !existingAuth.isAuthenticated()) {
            return true;
        }

        // Limit username comparison to providers which use usernames (ie
        // UsernamePasswordAuthenticationToken)
        // (see SEC-348)

        if (existingAuth instanceof UsernamePasswordAuthenticationToken && !existingAuth.getName().equals(username)) {
            return true;
        }

        // Handle unusual condition where an AnonymousAuthenticationToken is
        // already
        // present
        // This shouldn't happen very often, as BasicProcessingFitler is meant
        // to be
        // earlier in the filter
        // chain than AnonymousAuthenticationFilter. Nevertheless, presence of
        // both an
        // AnonymousAuthenticationToken
        // together with a BASIC authentication request header should indicate
        // reauthentication using the
        // BASIC protocol is desirable. This behaviour is also consistent with
        // that
        // provided by form and digest,
        // both of which force re-authentication if the respective header is
        // detected (and
        // in doing so replace
        // any existing AnonymousAuthenticationToken). See SEC-610.
        if (existingAuth instanceof AnonymousAuthenticationToken) {
            return true;
        }

        return false;
    }

    private boolean bodyDoesNotContainAuthenticationDetails(Map<String, String> body) {
        return !body.containsKey("username") && !body.containsKey("password");
    }
}
