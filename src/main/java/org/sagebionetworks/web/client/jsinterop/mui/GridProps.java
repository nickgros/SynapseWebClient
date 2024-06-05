package org.sagebionetworks.web.client.jsinterop.mui;

import java.util.ArrayList;
import jsinterop.annotations.JsNullable;
import jsinterop.annotations.JsOverlay;
import jsinterop.annotations.JsPackage;
import jsinterop.annotations.JsType;
import org.sagebionetworks.web.client.jsinterop.React;
import org.sagebionetworks.web.client.jsinterop.ReactComponentProps;

@JsType(isNative = true, namespace = JsPackage.GLOBAL, name = "Object")
public class GridProps extends ReactComponentProps {

  boolean item;
  boolean container;

  @JsNullable
  int xs;

  @JsNullable
  int sm;

  @JsNullable
  int md;

  @JsNullable
  int lg;

  @JsNullable
  int xl;

  @JsOverlay
  public static GridProps create(boolean container) {
    GridProps props = new GridProps();
    props.ref = React.createRef();
    props.__childrenAsList = new ArrayList<>();
    if (container) {
      props.container = true;
    } else {
      props.item = true;
    }
    return props;
  }
}
