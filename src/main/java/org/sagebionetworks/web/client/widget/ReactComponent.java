package org.sagebionetworks.web.client.widget;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.DivElement;
import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Element;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.ComplexPanel;
import com.google.gwt.user.client.ui.Widget;
import java.util.ArrayList;
import java.util.List;
import jsinterop.base.Js;
import jsinterop.base.JsPropertyMap;
import org.sagebionetworks.web.client.jsinterop.JSON;
import org.sagebionetworks.web.client.jsinterop.React;
import org.sagebionetworks.web.client.jsinterop.ReactComponentProps;
import org.sagebionetworks.web.client.jsinterop.ReactComponentType;
import org.sagebionetworks.web.client.jsinterop.ReactDOMRoot;
import org.sagebionetworks.web.client.jsinterop.ReactNode;

/**
 * Automatically unmounts the ReactComponent (if any) inside this div when this container is detached/unloaded.
 */
public class ReactComponent<T extends ReactComponentProps>
  extends ComplexPanel
  implements HasClickHandlers {

  private ReactDOMRoot root;
  private ReactComponentType<T> reactComponentType;
  public T props;

  private ReactNode<?> reactElement;

  public ReactComponent() {
    this(DivElement.TAG);
  }

  public ReactComponent(String tag) {
    setElement(Document.get().createElement(tag));
  }

  boolean allChildrenAreReactComponents() {
    boolean allChildrenAreReactComponents = getChildren().size() > 0;
    for (Widget w : getChildren()) {
      if (!(w instanceof ReactComponent)) {
        allChildrenAreReactComponents = false;
        break;
      }
    }
    return allChildrenAreReactComponents;
  }

  boolean isRenderedAsReactComponentChild() {
    return (
      getParent() instanceof ReactComponent &&
      ((ReactComponent<?>) getParent()).allChildrenAreReactComponents()
    );
  }

  /**
   * This method
   * returns the root ReactComponent that should be re-rendered.
   *
   * @return
   */
  ReactComponent<?> getRootReactComponentWidget() {
    if (isRenderedAsReactComponentChild()) {
      return ((ReactComponent<?>) getParent()).getRootReactComponentWidget();
    } else {
      return this;
    }
  }

  @Override
  public HandlerRegistration addClickHandler(ClickHandler handler) {
    return addDomHandler(handler, ClickEvent.getType());
  }

  private void maybeCreateRoot() {
    if (root == null && !isRenderedAsReactComponentChild()) {
      root = ReactComponentLifecycleUtils.onLoad(this.getElement());
    }
  }

  /**
   * Override the current props of the React component.
   * Because re-rendering the component will use `React.cloneElement`, old props must be explicitly set to `undefined`
   * to remove them.
   */
  public void overrideProps(T props) {
    this.props = props;
    this.rerender();
  }

  public void render(ReactNode<?> reactNode) {
    this.reactElement = reactNode;
    maybeCreateRoot();

    if (this.allChildrenAreReactComponents()) { // If all widget children are ReactNodes, clone the React component and add them as children
      List<ReactComponent<?>> childWidgets = new ArrayList<>();
      getChildren().forEach(w -> childWidgets.add(((ReactComponent<?>) w)));

      ReactNode<?>[] childReactElements = childWidgets
        .stream()
        .map(ReactComponent::getReactElement)
        .toArray(ReactNode<?>[]::new);

      this.reactElement =
        React.cloneElement(reactElement, this.props, childReactElements);
    } else if (getChildren().size() > 0) {
      // Create a callback ref that will allow us to inject the GWT children into the DOM
      ReactComponentProps.CallbackRef refCallback = (Element node) -> {
        if (node != null) {
          // Once the DOM node is defined, inject each child
          getChildren().forEach(w -> node.appendChild(w.getElement()));
        }
      };

      if (this.props == null) {
        this.props = (T) JsPropertyMap.of();
      }
      this.props.ref = refCallback;

      this.reactElement =
        React.cloneElement(
          reactElement,
          // Override the ref
          this.props
        );
    }

    // This component may be a React child of another component. If so, re-render the entire tree
    // This can resolve issues where e.g. a component was set change visibility
    ReactComponent<?> componentToRender = getRootReactComponentWidget();
    if (componentToRender == this) {
      root.render(this.reactElement);
    } else {
      componentToRender.rerender();
    }
  }

  @Override
  public void setVisible(boolean visible) {
    super.setVisible(visible);
    // Re-render the element
    this.rerender();
  }

  @Override
  protected void onLoad() {
    super.onLoad();
    maybeCreateRoot();
    this.rerender();
  }

  @Override
  protected void onUnload() {
    super.onUnload();
    if (root != null) {
      root.unmount();
    }
    root = null;
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
      // This is because our render implementation depends on logical attachment

      // Logical detach.
      getChildren().remove(w);

      // Physical detach (via React API!)
      this.rerender();
    }
    return true;
  }

  public ReactNode<?> getReactElement() {
    return reactElement;
  }

  public void rerender() {
    if (reactElement != null) {
      this.render(reactElement);
    }
  }
}
