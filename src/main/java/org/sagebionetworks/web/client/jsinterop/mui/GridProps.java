package org.sagebionetworks.web.client.jsinterop.mui;

import elemental2.core.JsObject;
import jsinterop.annotations.JsNullable;
import jsinterop.annotations.JsOverlay;
import jsinterop.annotations.JsPackage;
import jsinterop.annotations.JsType;
import org.sagebionetworks.web.client.jsinterop.PropsWithStyle;
import org.sagebionetworks.web.client.jsinterop.SxProps;

@JsType(isNative = true, namespace = JsPackage.GLOBAL, name = "Object")
public class GridProps extends PropsWithStyle {

  @JsNullable
  String id;

  boolean container;

  @JsNullable
  GridSizeOffset size;

  @JsNullable
  GridSizeOffset offset;

  @JsNullable
  SxProps sx;

  @JsNullable
  String rowSpacing;

  @JsNullable
  String columnSpacing;

  @JsOverlay
  public static GridProps create(boolean container) {
    GridProps props = new GridProps();
    props.size = new GridSizeOffset();
    props.offset = new GridSizeOffset();
    props.sx = new SxProps();

    if (container) {
      props.container = true;
    }
    return props;
  }
}
