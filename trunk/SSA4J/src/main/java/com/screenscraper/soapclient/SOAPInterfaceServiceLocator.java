/**
 * SOAPInterfaceServiceLocator.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis WSDL2Java emitter.
 */

package com.screenscraper.soapclient;
@SuppressWarnings("rawtypes") 
public class SOAPInterfaceServiceLocator extends org.apache.axis.client.Service implements com.screenscraper.soapclient.SOAPInterfaceService {

	private static final long serialVersionUID = -5286453917115317894L;
	// Use to get a proxy class for SOAPInterface
    private final java.lang.String SOAPInterface_address = "http://localhost:8779/axis/services/SOAPInterface";

    public java.lang.String getSOAPInterfaceAddress() {
        return SOAPInterface_address;
    }

    // The WSDD service name defaults to the port name.
    private java.lang.String SOAPInterfaceWSDDServiceName = "SOAPInterface";

    public java.lang.String getSOAPInterfaceWSDDServiceName() {
        return SOAPInterfaceWSDDServiceName;
    }

    public void setSOAPInterfaceWSDDServiceName(java.lang.String name) {
        SOAPInterfaceWSDDServiceName = name;
    }

    public com.screenscraper.soapclient.SOAPInterface getSOAPInterface() throws javax.xml.rpc.ServiceException {
       java.net.URL endpoint;
        try {
            endpoint = new java.net.URL(SOAPInterface_address);
        }
        catch (java.net.MalformedURLException e) {
            throw new javax.xml.rpc.ServiceException(e);
        }
        return getSOAPInterface(endpoint);
    }

    public com.screenscraper.soapclient.SOAPInterface getSOAPInterface(java.net.URL portAddress) throws javax.xml.rpc.ServiceException {
        try {
            com.screenscraper.soapclient.SOAPInterfaceSoapBindingStub _stub = new com.screenscraper.soapclient.SOAPInterfaceSoapBindingStub(portAddress, this);
            _stub.setPortName(getSOAPInterfaceWSDDServiceName());
            return _stub;
        }
        catch (org.apache.axis.AxisFault e) {
            return null;
        }
    }

    /**
     * For the given interface, get the stub implementation.
     * If this service has no port for the given interface,
     * then ServiceException is thrown.
     */
    public java.rmi.Remote getPort(Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        try {
            if (com.screenscraper.soapclient.SOAPInterface.class.isAssignableFrom(serviceEndpointInterface)) {
                com.screenscraper.soapclient.SOAPInterfaceSoapBindingStub _stub = new com.screenscraper.soapclient.SOAPInterfaceSoapBindingStub(new java.net.URL(SOAPInterface_address), this);
                _stub.setPortName(getSOAPInterfaceWSDDServiceName());
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
        String inputPortName = portName.getLocalPart();
        if ("SOAPInterface".equals(inputPortName)) {
            return getSOAPInterface();
        }
        else  {
            java.rmi.Remote _stub = getPort(serviceEndpointInterface);
            ((org.apache.axis.client.Stub) _stub).setPortName(portName);
            return _stub;
        }
    }

    public javax.xml.namespace.QName getServiceName() {
        return new javax.xml.namespace.QName("http://localhost:8779/axis/services/SOAPInterface", "SOAPInterfaceService");
    }

    private java.util.HashSet ports = null;

    @SuppressWarnings("unchecked")
    public java.util.Iterator getPorts() {
        if (ports == null) {
            ports = new java.util.HashSet();
            ports.add(new javax.xml.namespace.QName("SOAPInterface"));
        }
        return ports.iterator();
    }

}
