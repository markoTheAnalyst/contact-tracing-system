/**
 * TokenServerServiceLocator.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package token;

public class TokenServerServiceLocator extends org.apache.axis.client.Service implements token.TokenServerService {

    public TokenServerServiceLocator() {
    }


    public TokenServerServiceLocator(org.apache.axis.EngineConfiguration config) {
        super(config);
    }

    public TokenServerServiceLocator(java.lang.String wsdlLoc, javax.xml.namespace.QName sName) throws javax.xml.rpc.ServiceException {
        super(wsdlLoc, sName);
    }

    // Use to get a proxy class for TokenServer
    private java.lang.String TokenServer_address = "http://localhost:8080/TokenServer/services/TokenServer";

    public java.lang.String getTokenServerAddress() {
        return TokenServer_address;
    }

    // The WSDD service name defaults to the port name.
    private java.lang.String TokenServerWSDDServiceName = "TokenServer";

    public java.lang.String getTokenServerWSDDServiceName() {
        return TokenServerWSDDServiceName;
    }

    public void setTokenServerWSDDServiceName(java.lang.String name) {
        TokenServerWSDDServiceName = name;
    }

    public token.TokenServer getTokenServer() throws javax.xml.rpc.ServiceException {
       java.net.URL endpoint;
        try {
            endpoint = new java.net.URL(TokenServer_address);
        }
        catch (java.net.MalformedURLException e) {
            throw new javax.xml.rpc.ServiceException(e);
        }
        return getTokenServer(endpoint);
    }

    public token.TokenServer getTokenServer(java.net.URL portAddress) throws javax.xml.rpc.ServiceException {
        try {
            token.TokenServerSoapBindingStub _stub = new token.TokenServerSoapBindingStub(portAddress, this);
            _stub.setPortName(getTokenServerWSDDServiceName());
            return _stub;
        }
        catch (org.apache.axis.AxisFault e) {
            return null;
        }
    }

    public void setTokenServerEndpointAddress(java.lang.String address) {
        TokenServer_address = address;
    }

    /**
     * For the given interface, get the stub implementation.
     * If this service has no port for the given interface,
     * then ServiceException is thrown.
     */
    public java.rmi.Remote getPort(Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        try {
            if (token.TokenServer.class.isAssignableFrom(serviceEndpointInterface)) {
                token.TokenServerSoapBindingStub _stub = new token.TokenServerSoapBindingStub(new java.net.URL(TokenServer_address), this);
                _stub.setPortName(getTokenServerWSDDServiceName());
                return _stub;
            }
        }
        catch (java.lang.Throwable t) {
            throw new javax.xml.rpc.ServiceException(t);
        }
        throw new javax.xml.rpc.ServiceException("There is no stub implementation for the interface:  " + (serviceEndpointInterface == null ? "null" : serviceEndpointInterface.getName()));
    }

    /**
     * For the given interface, get the stub implementation.
     * If this service has no port for the given interface,
     * then ServiceException is thrown.
     */
    public java.rmi.Remote getPort(javax.xml.namespace.QName portName, Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        if (portName == null) {
            return getPort(serviceEndpointInterface);
        }
        java.lang.String inputPortName = portName.getLocalPart();
        if ("TokenServer".equals(inputPortName)) {
            return getTokenServer();
        }
        else  {
            java.rmi.Remote _stub = getPort(serviceEndpointInterface);
            ((org.apache.axis.client.Stub) _stub).setPortName(portName);
            return _stub;
        }
    }

    public javax.xml.namespace.QName getServiceName() {
        return new javax.xml.namespace.QName("http://token", "TokenServerService");
    }

    private java.util.HashSet ports = null;

    public java.util.Iterator getPorts() {
        if (ports == null) {
            ports = new java.util.HashSet();
            ports.add(new javax.xml.namespace.QName("http://token", "TokenServer"));
        }
        return ports.iterator();
    }

    /**
    * Set the endpoint address for the specified port name.
    */
    public void setEndpointAddress(java.lang.String portName, java.lang.String address) throws javax.xml.rpc.ServiceException {
        
if ("TokenServer".equals(portName)) {
            setTokenServerEndpointAddress(address);
        }
        else 
{ // Unknown Port Name
            throw new javax.xml.rpc.ServiceException(" Cannot set Endpoint Address for Unknown Port" + portName);
        }
    }

    /**
    * Set the endpoint address for the specified port name.
    */
    public void setEndpointAddress(javax.xml.namespace.QName portName, java.lang.String address) throws javax.xml.rpc.ServiceException {
        setEndpointAddress(portName.getLocalPart(), address);
    }

}
