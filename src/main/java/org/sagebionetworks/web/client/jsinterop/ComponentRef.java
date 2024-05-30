package org.sagebionetworks.web.client.jsinterop;

import jsinterop.annotations.*;

@JsType(isNative = true, namespace = JsPackage.GLOBAL, name = "Object")
public class ComponentRef {

  public Object current;
}
