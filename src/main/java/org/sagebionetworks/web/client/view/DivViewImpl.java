package org.sagebionetworks.web.client.view;

import com.google.gwt.user.client.ui.FlowPanel;
import org.gwtbootstrap3.client.ui.html.Text;

public class DivViewImpl extends FlowPanel implements DivView {

  @Override
  public void setText(String text) {
    add(new Text(text));
  }
}
