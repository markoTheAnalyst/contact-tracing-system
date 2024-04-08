/**
 * TokenServerService.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package token;

public interface TokenServerService extends javax.xml.rpc.Service {
    public java.lang.String getTokenServerAddress();

    public token.TokenServer getTokenServer() throws javax.xml.rpc.ServiceException;

    public token.TokenServer getTokenServer(java.net.URL portAddress) throws javax.xml.rpc.ServiceException;
}
