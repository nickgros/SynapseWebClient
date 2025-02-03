package org.sagebionetworks.web.client.jsinterop.mui;

import jsinterop.annotations.JsPackage;
import jsinterop.annotations.JsType;
import org.sagebionetworks.web.client.jsinterop.ReactComponentType;

@JsType(isNative = true, namespace = JsPackage.GLOBAL)
public class MaterialUI {

  /**
   * All MUI components must be manually added to the MaterialUI object in Portal.html before they can be accessed in JsInterop code
   */

  public static ReactComponentType<GridProps> Grid2;
}
