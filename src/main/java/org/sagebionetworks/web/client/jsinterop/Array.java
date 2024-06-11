package org.sagebionetworks.web.client.jsinterop;

import jsinterop.annotations.JsConstructor;
import jsinterop.annotations.JsPackage;
import jsinterop.annotations.JsType;

@JsType(isNative = true, namespace = JsPackage.GLOBAL, name = "Object")
public class Array<T> {

  @SafeVarargs
  @JsConstructor
  public Array(T... items) {}

  public native void push(T item);
}
