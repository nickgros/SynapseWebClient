package org.sagebionetworks.web.client.jsinterop;

import com.google.gwt.dom.client.Element;
import java.util.ArrayList;
import java.util.List;
import jsinterop.annotations.JsFunction;
import jsinterop.annotations.JsNullable;
import jsinterop.annotations.JsOverlay;
import jsinterop.annotations.JsPackage;
import jsinterop.annotations.JsType;

@JsType(isNative = true, namespace = JsPackage.GLOBAL, name = "Object")
public class ReactComponentProps {

  @JsFunction
  public interface CallbackRef {
    void run(Element element);
  }

  @JsNullable
  public ReactNode[] children;

  protected List<ReactNode> __childrenAsList;

  // ComponentRef or CallbackRef
  public Object ref;

  @JsOverlay
  public final void addChild(ReactNode child) {
    if (__childrenAsList == null) {
      __childrenAsList = new ArrayList<>();
    }
    __childrenAsList.add(child);
    children = __childrenAsList.toArray(new ReactNode[0]);
  }

  @JsOverlay
  public final void clearChildren() {
    __childrenAsList = new ArrayList<>();
    children = null;
  }

  @JsOverlay
  public final List<ReactNode> getChildren() {
    if (__childrenAsList == null) {
      __childrenAsList = new ArrayList<>();
    }
    return __childrenAsList;
  }
}
