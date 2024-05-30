package org.sagebionetworks.web.client.widget.sharing;

import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;

public class OpenDataViewImpl implements OpenDataView {

  public interface Binder extends UiBinder<FlowPanel, OpenDataViewImpl> {}

  private FlowPanel widget;

  @UiField
  FlowPanel isPublicAndOpen;

  @UiField
  FlowPanel isPublicAndAdmin;

  @UiField
  FlowPanel isPrivateAndOpenAndAdmin;

  @Inject
  public OpenDataViewImpl(Binder binder) {
    widget = binder.createAndBindUi(this);
  }

  @Override
  public void reset() {
    isPublicAndOpen.setVisible(false);
    isPublicAndAdmin.setVisible(false);
    isPrivateAndOpenAndAdmin.setVisible(false);
  }

  @Override
  public void showMustGivePublicReadToBeOpenData() {
    isPrivateAndOpenAndAdmin.setVisible(true);
  }

  @Override
  public void showMustContactACTToBeOpenData() {
    isPublicAndAdmin.setVisible(true);
  }

  @Override
  public void showIsOpenData() {
    isPublicAndOpen.setVisible(true);
  }

  @Override
  public Widget asWidget() {
    return widget;
  }
}
