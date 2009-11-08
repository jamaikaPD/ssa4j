/**
 * SOAPInterfaceService.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis WSDL2Java emitter.
 */

package com.screenscraper.soapclient;

public interface SOAPInterfaceService extends javax.xml.rpc.Service {
    public java.lang.String getSOAPInterfaceAddress();

    public com.screenscraper.soapclient.SOAPInterface getSOAPInterface() throws javax.xml.rpc.ServiceException;

    public com.screenscraper.soapclient.SOAPInterface getSOAPInterface(java.net.URL portAddress) throws javax.xml.rpc.ServiceException;
}
