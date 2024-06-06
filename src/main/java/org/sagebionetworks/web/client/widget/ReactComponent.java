package org.sagebionetworks.web.client.widget;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.DivElement;
import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Element;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.ComplexPanel;
import com.google.gwt.user.client.ui.Widget;
import java.util.ArrayList;
import java.util.List;
import org.sagebionetworks.web.client.jsinterop.JSON;
import org.sagebionetworks.web.client.jsinterop.React;
import org.sagebionetworks.web.client.jsinterop.ReactComponentProps;
import org.sagebionetworks.web.client.jsinterop.ReactDOMRoot;
import org.sagebionetworks.web.client.jsinterop.ReactNode;

/**
 * Automatically unmounts the ReactComponent (if any) inside this div when this container is detached/unloaded.
 */
public class ReactComponent extends ComplexPanel implements HasClickHandlers {

  public ReactComponent() {
    this(DivElement.TAG);
  }

  public ReactComponent(String tag) {
    setElement(Document.get().createElement(tag));
  }

  @Override
  public HandlerRegistration addClickHandler(ClickHandler handler) {
    return addDomHandler(handler, ClickEvent.getType());
  }

  private ReactDOMRoot root;
  private ReactNode<?> reactElement;

  public void render(ReactNode<?> reactNode) {
    this.reactElement = reactNode;
    // Create the root (if necessary)
    if (root == null) {
      root = ReactComponentLifecycleUtils.onLoad(this.getElement());
    }

    boolean allAreReactComponents = getChildren().size() > 0;
    for (Widget w : getChildren()) {
      if (!(w instanceof ReactComponent)) {
        allAreReactComponents = false;
        break;
      }
    }

    if (allAreReactComponents) { // If all widget children are ReactNodes, clone the React component and add them as children
      ReactNode<?>[] newChildren = new ReactNode<?>[getChildren().size()];
      for (int i = 0; i < getChildren().size(); i++) {
        newChildren[i] =
          ((ReactComponent) getChildren().get(i)).getReactElement();
      }

      this.reactElement =
        React.cloneElement(
          reactElement,
          // passing null will retain the original props
          null,
          newChildren
        );
    } else if (getChildren().size() > 0) {
      // Create a callback ref that will allow us to inject the GWT children into the DOM
      ReactComponentProps.CallbackRef refCallback = (Element node) -> {
        if (node != null) {
          // Once the DOM node is defined, inject each child
          getChildren().forEach(w -> node.appendChild(w.getElement()));
        }
      };

      ReactComponentProps newProps = (ReactComponentProps) JSON.parse("{}");
      newProps.ref = refCallback;

      this.reactElement =
        React.cloneElement(
          reactElement,
          // Override the ref
          newProps
        );
    }
    // Render the component
    root.render(this.reactElement);
  }

  @Override
  protected void onLoad() {
    super.onLoad();
    if (root == null) {
      root = ReactComponentLifecycleUtils.onLoad(this.getElement());
    }
    this.rerender();
  }

  @Override
  protected void onUnload() {
    ReactComponentLifecycleUtils.onUnload(root);
    root = null;
    super.onUnload();
  }

  @Override
  public void clear() {
    // clear doesn't typically call onUnload, but we want to for this element.
    this.onUnload();

    super.clear();
  }

  /**
   * Adds a child widget.
   *
   * @param child the widget to be added
   * @throws UnsupportedOperationException if this method is not supported (most
   *           often this means that a specific overload must be called)
   */
  @Override
  public void add(Widget child) {
    // See implementation in com.google.gwt.user.client.ui.ComplexPanel

    // Detach new child
    child.removeFromParent();

    // Logical attach
    getChildren().add(child);

    // Physical attach (via React API!)
    if (reactElement != null) {
      // Rerender if possible
      this.render(reactElement);
    }

    // Adopt.
    adopt(child);
  }

  @Override
  public boolean remove(Widget w) {
    // See implementation in ComplexPanel

    // Validate.
    if (w.getParent() != this) {
      return false;
    }
    // Orphan.
    try {
      orphan(w);
    } finally {
      // Note - compared to ComplexPanel, we flipped logical and physical detach
      // This is because our rerender implementation depends on logical attachment

      // Logical detach.
      getChildren().remove(w);

      // Physical detach (via React API!)
      this.rerender();
    }
    return true;
  }

  public ReactNode getReactElement() {
    return reactElement;
  }

  public void rerender() {
    if (reactElement != null) {
      this.render(reactElement);
    }
  }
}
