package org.sagebionetworks.web.client.widget.accessrequirements;

import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.InlineLabel;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;

public class LockAccessRequirementWidgetViewImpl
  implements LockAccessRequirementWidgetView {

  @UiField
  FlowPanel deleteAccessRequirementContainer;

  @UiField
  FlowPanel teamSubjectsWidgetContainer;

  @UiField
  FlowPanel entitySubjectsWidgetContainer;

  @UiField
  FlowPanel accessRequirementRelatedProjectsListContainer;

  @UiField
  FlowPanel coveredEntitiesHeadingUI;

  @UiField
  FlowPanel accessRequirementIDUI;

  @UiField
  InlineLabel accessRequirementIDField;

  public interface Binder
    extends UiBinder<Widget, LockAccessRequirementWidgetViewImpl> {}

  Widget w;

  @Inject
  public LockAccessRequirementWidgetViewImpl(Binder binder) {
    this.w = binder.createAndBindUi(this);
  }

  @Override
  public void addStyleNames(String styleNames) {
    w.addStyleName(styleNames);
  }

  @Override
  public Widget asWidget() {
    return w;
  }

  @Override
  public void setDeleteAccessRequirementWidget(IsWidget w) {
    deleteAccessRequirementContainer.clear();
    deleteAccessRequirementContainer.add(w);
  }

  @Override
  public void setTeamSubjectsWidget(IsWidget w) {
    teamSubjectsWidgetContainer.clear();
    teamSubjectsWidgetContainer.add(w);
  }

  @Override
  public void setEntitySubjectsWidget(IsWidget w) {
    entitySubjectsWidgetContainer.clear();
    entitySubjectsWidgetContainer.add(w);
  }

  @Override
  public void setAccessRequirementRelatedProjectsList(IsWidget w) {
    accessRequirementRelatedProjectsListContainer.clear();
    accessRequirementRelatedProjectsListContainer.add(w);
  }

  @Override
  public void setCoveredEntitiesHeadingVisible(boolean visible) {
    coveredEntitiesHeadingUI.setVisible(visible);
  }

  @Override
  public void setAccessRequirementID(String arID) {
    accessRequirementIDField.setText(arID);
  }

  @Override
  public void setAccessRequirementIDVisible(boolean visible) {
    accessRequirementIDUI.setVisible(visible);
  }
}
