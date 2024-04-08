package token;

public class TokenServerProxy implements token.TokenServer {
  private String _endpoint = null;
  private token.TokenServer tokenServer = null;
  
  public TokenServerProxy() {
    _initTokenServerProxy();
  }
  
  public TokenServerProxy(String endpoint) {
    _endpoint = endpoint;
    _initTokenServerProxy();
  }
  
  private void _initTokenServerProxy() {
    try {
      tokenServer = (new token.TokenServerServiceLocator()).getTokenServer();
      if (tokenServer != null) {
        if (_endpoint != null)
          ((javax.xml.rpc.Stub)tokenServer)._setProperty("javax.xml.rpc.service.endpoint.address", _endpoint);
        else
          _endpoint = (String)((javax.xml.rpc.Stub)tokenServer)._getProperty("javax.xml.rpc.service.endpoint.address");
      }
      
    }
    catch (javax.xml.rpc.ServiceException serviceException) {}
  }
  
  public String getEndpoint() {
    return _endpoint;
  }
  
  public void setEndpoint(String endpoint) {
    _endpoint = endpoint;
    if (tokenServer != null)
      ((javax.xml.rpc.Stub)tokenServer)._setProperty("javax.xml.rpc.service.endpoint.address", _endpoint);
    
  }
  
  public token.TokenServer getTokenServer() {
    if (tokenServer == null)
      _initTokenServerProxy();
    return tokenServer;
  }
  
  public java.lang.String getToken(java.lang.String id) throws java.rmi.RemoteException{
    if (tokenServer == null)
      _initTokenServerProxy();
    return tokenServer.getToken(id);
  }
  
  public java.lang.String logIn(java.lang.String data) throws java.rmi.RemoteException{
    if (tokenServer == null)
      _initTokenServerProxy();
    return tokenServer.logIn(data);
  }
  
  public void logOut(java.lang.String token) throws java.rmi.RemoteException{
    if (tokenServer == null)
      _initTokenServerProxy();
    tokenServer.logOut(token);
  }
  
  public java.lang.String listTokens() throws java.rmi.RemoteException{
    if (tokenServer == null)
      _initTokenServerProxy();
    return tokenServer.listTokens();
  }
  
  public java.lang.String doesTokenExist(java.lang.String token) throws java.rmi.RemoteException{
    if (tokenServer == null)
      _initTokenServerProxy();
    return tokenServer.doesTokenExist(token);
  }
  
  
}