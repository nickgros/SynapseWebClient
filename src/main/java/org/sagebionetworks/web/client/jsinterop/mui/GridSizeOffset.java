package org.sagebionetworks.web.client.jsinterop.mui;

import elemental2.core.JsObject;
import jsinterop.annotations.JsPackage;
import jsinterop.annotations.JsType;

@JsType(isNative = true, namespace = JsPackage.GLOBAL, name = "Object")
public class GridSizeOffset extends JsObject {

  int xs;
  int sm;
  int md;
  int lg;
  int xl;
}
