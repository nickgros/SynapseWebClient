package org.sagebionetworks.web.client.widget.mui;

import com.google.gwt.user.client.ui.FlowPanel;

public class Grid extends FlowPanel {

  private static final String CONTAINER_CLASS = "MuiGrid-container";
  private static final String ITEM_CLASS = "MuiGrid-item";
  private static final String GRID_CLASS = "MuiGrid-grid";

  public Grid() {}

  public void setContainer(boolean container) {
    if (container) {
      this.addStyleName(CONTAINER_CLASS);
    } else {
      this.removeStyleName(CONTAINER_CLASS);
    }
  }

  public void setItem(boolean item) {
    if (item) {
      this.addStyleName(ITEM_CLASS);
    } else {
      this.removeStyleName(ITEM_CLASS);
    }
  }

  public void setXs(int xs) {
    this.addStyleName(GRID_CLASS + "-xs-" + xs);
  }

  public void setSm(int sm) {
    this.addStyleName(GRID_CLASS + "-sm-" + sm);
  }

  public void setMd(int md) {
    this.addStyleName(GRID_CLASS + "-md-" + md);
  }

  public void setLg(int lg) {
    this.addStyleName(GRID_CLASS + "-lg-" + lg);
  }

  public void setXl(int xl) {
    this.addStyleName(GRID_CLASS + "-xl-" + xl);
  }
}
