/**
 * TokenServer.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package token;

public interface TokenServer extends java.rmi.Remote {
    public java.lang.String getToken(java.lang.String id) throws java.rmi.RemoteException;
    public java.lang.String logIn(java.lang.String data) throws java.rmi.RemoteException;
    public void logOut(java.lang.String token) throws java.rmi.RemoteException;
    public java.lang.String listTokens() throws java.rmi.RemoteException;
    public java.lang.String doesTokenExist(java.lang.String token) throws java.rmi.RemoteException;
}
