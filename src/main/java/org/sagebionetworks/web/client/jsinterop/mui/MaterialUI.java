package org.sagebionetworks.web.client.jsinterop.mui;

import jsinterop.annotations.JsPackage;
import jsinterop.annotations.JsType;
import org.sagebionetworks.web.client.jsinterop.ReactComponentType;

@JsType(isNative = true, namespace = JsPackage.GLOBAL)
public class MaterialUI {

  // Ensure each component is loaded via ESM in Portal.html
  public static ReactComponentType<GridProps> Grid;
}
