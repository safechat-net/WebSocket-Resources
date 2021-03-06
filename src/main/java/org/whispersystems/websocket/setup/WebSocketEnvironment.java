/**
 * Copyright (C) 2014 Open WhisperSystems
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.whispersystems.websocket.setup;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.jersey.spi.container.servlet.ServletContainer;

import org.eclipse.jetty.server.RequestLog;
import org.whispersystems.websocket.auth.WebSocketAuthenticator;
import org.whispersystems.websocket.messages.WebSocketMessageFactory;
import org.whispersystems.websocket.messages.protobuf.ProtobufWebSocketMessageFactory;

import javax.validation.Validator;

import io.dropwizard.Configuration;
import io.dropwizard.jersey.DropwizardResourceConfig;
import io.dropwizard.jersey.setup.JerseyContainerHolder;
import io.dropwizard.jersey.setup.JerseyEnvironment;
import io.dropwizard.server.AbstractServerFactory;
import io.dropwizard.setup.Environment;

public class WebSocketEnvironment {

  private final JerseyContainerHolder jerseyServletContainer;
  private final JerseyEnvironment     jerseyEnvironment;
  private final ObjectMapper          objectMapper;
  private final Validator             validator;
  private final RequestLog            requestLog;

  private WebSocketAuthenticator authenticator;
  private WebSocketMessageFactory   messageFactory;
  private WebSocketConnectListener  connectListener;

  public WebSocketEnvironment(Environment environment, Configuration configuration) {
    DropwizardResourceConfig jerseyConfig = new DropwizardResourceConfig(environment.metrics());

    this.objectMapper           = environment.getObjectMapper();
    this.validator              = environment.getValidator();
    this.requestLog             = ((AbstractServerFactory)configuration.getServerFactory()).getRequestLogFactory().build("websocket");
    this.jerseyServletContainer = new JerseyContainerHolder(new ServletContainer(jerseyConfig)  );
    this.jerseyEnvironment      = new JerseyEnvironment(jerseyServletContainer, jerseyConfig);
    this.messageFactory         = new ProtobufWebSocketMessageFactory();
  }

  public JerseyEnvironment jersey() {
    return jerseyEnvironment;
  }

  public WebSocketAuthenticator getAuthenticator() {
    return authenticator;
  }

  public void setAuthenticator(WebSocketAuthenticator authenticator) {
    this.authenticator = authenticator;
  }

  public ObjectMapper getObjectMapper() {
    return objectMapper;
  }

  public RequestLog getRequestLog() {
    return requestLog;
  }

  public Validator getValidator() {
    return validator;
  }

  public ServletContainer getJerseyServletContainer() {
    return jerseyServletContainer.getContainer();
  }

  public WebSocketMessageFactory getMessageFactory() {
    return messageFactory;
  }

  public void setMessageFactory(WebSocketMessageFactory messageFactory) {
    this.messageFactory = messageFactory;
  }

  public WebSocketConnectListener getConnectListener() {
    return connectListener;
  }

  public void setConnectListener(WebSocketConnectListener connectListener) {
    this.connectListener = connectListener;
  }
}
