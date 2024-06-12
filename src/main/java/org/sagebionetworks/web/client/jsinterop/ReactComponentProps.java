package org.sagebionetworks.web.client.jsinterop;

import com.google.gwt.dom.client.Element;
import jsinterop.annotations.JsConstructor;
import jsinterop.annotations.JsFunction;
import jsinterop.annotations.JsOverlay;
import jsinterop.annotations.JsPackage;
import jsinterop.annotations.JsType;

@JsType(isNative = true, namespace = JsPackage.GLOBAL, name = "Object")
public class ReactComponentProps {

  @JsConstructor
  public ReactComponentProps() {}

  @JsFunction
  public interface CallbackRef {
    void run(Element element);
  }

  public Array<ReactNode<?>> children;

  // ComponentRef or CallbackRef
  public Object ref;

  @JsOverlay
  public final void addChild(ReactNode<?> child) {
    if (children == null) {
      children = new Array<>();
    }
    children.push(child);
  }

  @JsOverlay
  public final void clearChildren() {
    children = new Array<>();
  }

  @JsOverlay
  public final Array<ReactNode<?>> getChildren() {
    if (children == null) {
      children = new Array<>();
    }
    return children;
  }
}
