package org.sagebionetworks.web.client.jsinterop.react;

import jsinterop.base.JsPropertyMap;
import org.sagebionetworks.web.client.jsinterop.JsObject;
import org.sagebionetworks.web.client.jsinterop.PropsWithStyle;
import org.sagebionetworks.web.client.widget.ReactComponent;

/**
 * Abstract class for React component widgets that have a style prop. The style prop for the component may be manipulated
 * to show/hide the component based on the current state of the widget.
 * @param <T> the prop type.
 */
public abstract class HasStyle<T extends PropsWithStyle>
  extends ReactComponent<T> {

  public HasStyle() {
    super();
  }

  public void setStyle(JsPropertyMap<String> style) {
    this.props.style =
      (JsPropertyMap<String>) JsObject.assign(new JsObject(), (JsObject) style);
  }

  @Override
  public void setVisible(boolean visible) {
    if (visible) {
      if (this.props != null && this.props.style != null) {
        this.props.style.delete("display");
        // Clone the style object so React treats it as a new prop and properly re-renders.
        setStyle(
          (JsPropertyMap<String>) JsObject.assign(
            new JsObject(),
            (JsObject) this.props.style
          )
        );
      }
    } else {
      // Update the style prop to `display: none`.
      if (this.props == null) {
        this.props = (T) JsPropertyMap.of();
      }

      // Clone the style object so React treats it as a new prop and properly re-renders.
      JsPropertyMap<String> newStyle = (JsPropertyMap<String>) JsObject.assign(
        new JsObject(),
        (JsObject) this.props.style
      );
      newStyle.set("display", "none");
      setStyle(newStyle);
    }

    // Call the super method, which will trigger a re-render.
    super.setVisible(visible);
  }
}
