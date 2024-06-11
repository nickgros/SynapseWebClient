package org.sagebionetworks.web.client.jsinterop.react;

import org.sagebionetworks.web.client.jsinterop.JSON;
import org.sagebionetworks.web.client.widget.ReactComponent;

public abstract class HasProps<T> extends ReactComponent {

  T props;

  HasProps(T props) {
    this.props = JSON.parse("{}");
  }
}
