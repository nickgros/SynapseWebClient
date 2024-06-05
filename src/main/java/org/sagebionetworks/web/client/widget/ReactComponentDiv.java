package org.sagebionetworks.web.client.widget;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Widget;
import java.util.ArrayList;
import java.util.List;
import org.sagebionetworks.web.client.jsinterop.ComponentRef;
import org.sagebionetworks.web.client.jsinterop.React;
import org.sagebionetworks.web.client.jsinterop.ReactDOMRoot;
import org.sagebionetworks.web.client.jsinterop.ReactNode;

/**
 * Automatically unmounts the ReactComponent (if any) inside this div when this container is detached/unloaded.
 */
public class ReactComponentDiv extends FlowPanel {

  private ReactDOMRoot root;
  private ReactNode reactElement;
  List<Widget> childWidgets = new ArrayList<>();

  Timer widgetInjectionTimer = new Timer() {
    @Override
    public void run() {
      if (
        !childWidgets.isEmpty() &&
        reactElement != null &&
        reactElement.props != null
      ) {
        ComponentRef ref = reactElement.ref;
        if (ref == null || ref.current == null) {
          // Ref has not yet been initialized, so retry in a bit
          // TODO: Max retries
          this.schedule(100);
        } else {
          Element el = ((Element) ref.current);
          childWidgets.forEach(w -> {
            el.appendChild(w.getElement());
            GWT.log("successfully appended non react child");
          });
        }
      }
    }
  };

  public void render(ReactNode reactNode) {
    this.setReactElement(reactNode);

    // Cancel any widget injection that may be in progress
    widgetInjectionTimer.cancel();

    // If all widget children are ReactNodes, clone the React component and add them as children
    boolean allAreReactComponents =
      !childWidgets.isEmpty() &&
      childWidgets.stream().allMatch(w -> w instanceof ReactComponentDiv); // TODO support span as well, or add another type that uses fragment

    if (allAreReactComponents) {
      ReactNode[] newChildren = childWidgets
        .stream()
        .map(w -> ((ReactComponentDiv) w).getReactElement())
        .toArray(ReactNode[]::new);
      GWT.debugger();
      this.setReactElement(
          React.cloneElement(
            reactElement,
            // passing null will retain the original props
            null,
            newChildren
          )
        );
    }

    // Create the root (if necessary)
    if (root == null) {
      root = ReactComponentLifecycleUtils.onLoad(this.getElement());
    }

    // Render the component
    root.render(this.getReactElement());
    //    root.render(
    //      React.cloneElementWrappedInThemeContext(this.getReactElement())
    //    );

    // If any GWT child widget is not a ReactNode, inject them into the DOM
    if (!childWidgets.isEmpty() && !allAreReactComponents) {
      // Inject non-react children.
      // This timer will keep trying to inject the children until the ref is available.
      widgetInjectionTimer.run();
    }
  }

  @Override
  protected void onLoad() {
    super.onLoad();
    if (root == null) {
      root = ReactComponentLifecycleUtils.onLoad(this.getElement());
    }
    if (reactElement != null) {
      this.render(reactElement);
    }
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

    if (
      reactElement.props != null && reactElement.props.getChildren() != null
    ) {
      reactElement.props.clearChildren();
    }
    if (!this.childWidgets.isEmpty() && reactElement.props != null) {
      if (reactElement.ref != null && reactElement.ref.current != null) {
        Element div = (Element) reactElement.ref.current;
        div.removeAllChildren();
      }
      this.childWidgets.clear();
    }
    super.clear();
  }

  /**
   * Adds a child widget.
   *
   * @param w the widget to be added
   * @throws UnsupportedOperationException if this method is not supported (most
   *           often this means that a specific overload must be called)
   */
  @Override
  public void add(Widget w) {
    childWidgets.add(w);
  }

  @Override
  public boolean remove(Widget w) {
    GWT.log("remove called");
    return true;
  }

  protected void setReactElement(ReactNode element) {
    this.reactElement = element;
  }

  public ReactNode getReactElement() {
    return reactElement;
  }
}
