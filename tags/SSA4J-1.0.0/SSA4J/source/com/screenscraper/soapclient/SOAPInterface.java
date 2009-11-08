/**
 * SOAPInterface.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis WSDL2Java emitter.
 */

package com.screenscraper.soapclient;

public interface SOAPInterface extends java.rmi.Remote {
    public int update(java.lang.String xml) throws java.rmi.RemoteException;
    public int removeScrapingSession(java.lang.String name) throws java.rmi.RemoteException;
    public int removeScript(java.lang.String name) throws java.rmi.RemoteException;
    public java.lang.String getLog(java.lang.String filename, boolean start, int lines) throws java.rmi.RemoteException;
    public java.lang.String getLog(java.lang.String filename) throws java.rmi.RemoteException;
    public java.lang.String[] getRunningScrapingSessions() throws java.rmi.RemoteException;
    public int setTimeout(java.lang.String id, int minutes) throws java.rmi.RemoteException;
    public int isFinished(java.lang.String id) throws java.rmi.RemoteException;
    public java.lang.String getVariable(java.lang.String id, java.lang.String var) throws java.rmi.RemoteException;
    public int setVariable(java.lang.String id, java.lang.String var, java.lang.String value) throws java.rmi.RemoteException;
    public int scrape(java.lang.String id) throws java.rmi.RemoteException;
    public java.lang.String[] getDataRecord(java.lang.String id, java.lang.String var) throws java.rmi.RemoteException;
    public java.lang.String[][] getDataSet(java.lang.String id, java.lang.String var) throws java.rmi.RemoteException;
    public boolean isAcceptingConnections() throws java.rmi.RemoteException;
    public int setAcceptingConnections(boolean accepting) throws java.rmi.RemoteException;
    public java.lang.String[] getScrapingSessionNames() throws java.rmi.RemoteException;
    public java.lang.String[] getScriptNames() throws java.rmi.RemoteException;
    public java.lang.String[] getCompletedScrapingSessions() throws java.rmi.RemoteException;
    public int removeCompletedScrapingSession(java.lang.String id) throws java.rmi.RemoteException;
    public java.lang.String getScrapingSessionName(java.lang.String id) throws java.rmi.RemoteException;
    public long getScrapingSessionStartTime(java.lang.String id) throws java.rmi.RemoteException;
    public java.lang.String initializeScrapingSession(java.lang.String name) throws java.rmi.RemoteException;
    public int stopScrapingSession(java.lang.String id) throws java.rmi.RemoteException;
    public java.lang.String[] getLogNames() throws java.rmi.RemoteException;
    public long getLogSize(java.lang.String filename) throws java.rmi.RemoteException;
    public int removeLog(java.lang.String filename) throws java.rmi.RemoteException;
}
