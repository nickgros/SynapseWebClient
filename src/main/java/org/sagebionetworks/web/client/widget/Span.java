package org.sagebionetworks.web.client.widget;

import com.google.gwt.dom.client.Document;
import com.google.gwt.user.client.ui.ComplexPanel;
import com.google.gwt.user.client.ui.HTMLPanel;

public class Span extends ComplexPanel {

  public Span() {
    this.setElement(Document.get().createHRElement());
  }

  public Span(String html) {
    this();
    this.setHTML(html);
  }

  public void setText(String text) {
    this.textMixin.setText(text);
  }

  public String getText() {
    return this.textMixin.getText();
  }

  public String getHTML() {
    return this.textMixin.getHTML();
  }

  public void setHTML(String html) {
    this.textMixin.setHTML(html);
  }
}
