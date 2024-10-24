package org.sagebionetworks.web.unitclient.presenter;

import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.i18n.client.DateTimeFormat.PredefinedFormat;
import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.json.client.JSONValue;
import com.google.gwt.user.client.rpc.HasRpcToken;
import com.google.gwt.user.client.rpc.ServiceDefTarget;
import com.google.gwt.xhr.client.XMLHttpRequest;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.Date;
import org.sagebionetworks.web.client.GWTWrapper;
import org.sagebionetworks.web.client.utils.Callback;

public class GWTStub implements GWTWrapper {

  public GWTStub() {}

  @Override
  public String getHostPageBaseURL() {
    return "http://hostpage/url";
  }

  @Override
  public String getModuleBaseURL() {
    return "http://baseurl/";
  }

  @Override
  public void assignThisWindowWith(String url) {}

  @Override
  public String encodeQueryString(String queryString) {
    return URLEncoder.encode(queryString);
  }

  @Override
  public String decodeQueryString(String queryString) {
    return URLDecoder.decode(queryString);
  }

  @Override
  public int nextInt(int upperBound) {
    return 0;
  }

  @Override
  public XMLHttpRequest createXMLHttpRequest() {
    return null;
  }

  @Override
  public NumberFormat getNumberFormat(String pattern) {
    return null;
  }

  @Override
  public String getHostPrefix() {
    return null;
  }

  @Override
  public String getHostName() {
    return null;
  }

  @Override
  public String getCurrentURL() {
    return null;
  }

  @Override
  public DateTimeFormat getDateTimeFormat(PredefinedFormat format) {
    return null;
  }

  @Override
  public void scheduleExecution(Callback callback, int delay) {}

  @Override
  public String getUserAgent() {
    return null;
  }

  @Override
  public String getAppVersion() {
    return null;
  }

  @Override
  public int nextRandomInt() {
    return 0;
  }

  @Override
  public void scheduleDeferred(Callback callback) {}

  @Override
  public void addDaysToDate(Date date, int days) {}

  @Override
  public boolean isWhitespace(String text) {
    return false;
  }

  @Override
  public void newItem(String historyToken, boolean issueEvent) {}

  @Override
  public void replaceItem(String historyToken, boolean issueEvent) {}

  @Override
  public String getCurrentHistoryToken() {
    return null;
  }

  @Override
  public HasRpcToken asHasRpcToken(Object service) {
    return null;
  }

  @Override
  public ServiceDefTarget asServiceDefTarget(Object service) {
    return null;
  }

  @Override
  public String getUniqueElementId() {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public void scheduleFixedDelay(Callback callback, int delayMs) {}

  @Override
  public void restoreWindowPosition() {}

  @Override
  public void saveWindowPosition() {}

  @Override
  public String getUniqueAliasName(String inputName) {
    return null;
  }

  @Override
  public String encode(String decodedURL) {
    return null;
  }

  @Override
  public String getFriendlySize(double size, boolean abbreviatedUnits) {
    return null;
  }

  @Override
  public DateTimeFormat getFormat(PredefinedFormat predefinedFormat) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public DateTimeFormat getFormat(String formatPattern) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public boolean isValidJSONArray(String json) {
    return false;
  }

  @Override
  public JSONValue parseJSONStrict(String json) {
    return null;
  }
}
