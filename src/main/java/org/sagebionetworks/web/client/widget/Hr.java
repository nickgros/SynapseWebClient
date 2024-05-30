package org.sagebionetworks.web.client.widget;

import com.google.gwt.dom.client.Document;
import com.google.gwt.user.client.ui.ComplexPanel;

/**
 * Used so that we can mock in tests. Gin injects a bootstrap Button, and these are pass through.
 *
 * @author jayhodgson
 *
 */
public class Hr extends ComplexPanel {

  public Hr() {
    this.setElement(Document.get().createHRElement());
  }
}
