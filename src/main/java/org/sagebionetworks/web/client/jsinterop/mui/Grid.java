package org.sagebionetworks.web.client.jsinterop.mui;

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

  private void renderComponent() {
    ReactNode component = React.createElement(MaterialUI.Grid, props);
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
