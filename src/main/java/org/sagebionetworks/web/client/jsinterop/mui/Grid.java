package org.sagebionetworks.web.client.jsinterop.mui;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.Widget;
import org.sagebionetworks.web.client.jsinterop.ComponentRef;
import org.sagebionetworks.web.client.jsinterop.React;
import org.sagebionetworks.web.client.jsinterop.ReactNode;
import org.sagebionetworks.web.client.jsinterop.SynapseReactClientFullContextProviderProps;
import org.sagebionetworks.web.client.widget.ReactComponentDiv;

public class Grid extends ReactComponentDiv {

  SynapseReactClientFullContextProviderProps contextProps;
  GridProps props = GridProps.create(true);

  public Grid(SynapseReactClientFullContextProviderProps contextProps) {
    this.contextProps = contextProps;
  }

  @Override
  protected void onLoad() {
    renderComponent();
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
    if (w instanceof ReactComponentDiv) {
      props.children = ((ReactComponentDiv) w).getComponent();
      renderComponent();
    } else {
      Timer timer = new Timer() {
        @Override
        public void run() {
          ComponentRef ref = props.ref;
          if (ref.current == null) {
            GWT.log("ref.current is null");
          } else {
            Element div = (Element) props.ref.current;
            div.appendChild(w.getElement());
          }
        }
      };
      timer.schedule(500);
    }
    //    renderComponent();
  }

  //
  //  /**
  //   * Removes all child widgets.
  //   */
  //  public void clear() {}

  //  /**
  //   * Gets an iterator for the contained widgets. This iterator is required to
  //   * implement {@link Iterator#remove()}.
  //   */
  //  Iterator<Widget> iterator();
  //
  //  /**
  //   * Removes a child widget.
  //   *
  //   * @param w the widget to be removed
  //   * @return <code>true</code> if the widget was present
  //   */
  //  boolean remove(Widget w);

  private void renderComponent() {
    ReactNode component = React.createElementWithSynapseContext(
      MaterialUI.Grid,
      props,
      contextProps,
      props.children
    );
    this.render(component);
  }

  public void setContainer(boolean container) {
    props.container = container;
    renderComponent();
  }

  public void setItem(boolean item) {
    props.item = item;
    renderComponent();
  }

  public void setXs(int xs) {
    props.xs = xs;
    renderComponent();
  }

  public void setSm(int sm) {
    props.sm = sm;
    renderComponent();
  }

  public void setMd(int md) {
    props.md = md;
    renderComponent();
  }

  public void setLg(int lg) {
    props.lg = lg;
    renderComponent();
  }

  public void setXl(int xl) {
    props.xl = xl;
    renderComponent();
  }
}
