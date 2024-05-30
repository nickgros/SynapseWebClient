package org.sagebionetworks.web.client.view;

import com.google.gwt.user.client.ui.HasVisibility;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.IsWidget;

/**
 * A basic div view. One use case is when a widget view is composed of other complex widgets that
 * need to be attached to the dom.
 *
 * @author jayhodgson
 */
public interface DivView
  extends HasWidgets.ForIsWidget, IsWidget, HasVisibility {
  void setText(String text);

  void addStyleName(String style);
}
