package org.sagebionetworks.web.client.widget.accessrequirements.createaccessrequirement;

import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import org.sagebionetworks.repo.model.AccessRequirement;
import org.sagebionetworks.repo.model.RestrictableObjectDescriptor;
import org.sagebionetworks.web.client.context.SynapseReactClientFullContextPropsProvider;
import org.sagebionetworks.web.client.jsinterop.CreateOrUpdateAccessRequirementWizardProps;
import org.sagebionetworks.web.client.jsinterop.React;
import org.sagebionetworks.web.client.jsinterop.ReactNode;
import org.sagebionetworks.web.client.jsinterop.SRC;
import org.sagebionetworks.web.client.widget.ReactComponentDiv;

public class CreateOrUpdateAccessRequirementWizard implements IsWidget {

  private final SynapseReactClientFullContextPropsProvider propsProvider;

  private final ReactComponentDiv reactComponentDiv;
  private RestrictableObjectDescriptor subject;
  private String accessRequirementId;
  private CreateOrUpdateAccessRequirementWizardProps.OnComplete onComplete;
  private CreateOrUpdateAccessRequirementWizardProps.OnCancel onCancel;

  @Inject
  public CreateOrUpdateAccessRequirementWizard(
    SynapseReactClientFullContextPropsProvider propsProvider
  ) {
    super();
    this.propsProvider = propsProvider;
    reactComponentDiv = new ReactComponentDiv();
  }

  private void renderComponent(
    CreateOrUpdateAccessRequirementWizardProps props
  ) {
    ReactNode reactNode = React.createElementWithSynapseContext(
      SRC.SynapseComponents.CreateOrUpdateAccessRequirementWizard,
      props,
      propsProvider.getJsInteropContextProps()
    );
    reactComponentDiv.render(reactNode);
  }

  public void configure(
    RestrictableObjectDescriptor subject,
    CreateOrUpdateAccessRequirementWizardProps.OnComplete onComplete,
    CreateOrUpdateAccessRequirementWizardProps.OnCancel onCancel
  ) {
    reactComponentDiv.clear();
    this.subject = subject;
    this.onComplete = onComplete;
    this.onCancel = onCancel;
    CreateOrUpdateAccessRequirementWizardProps props =
      CreateOrUpdateAccessRequirementWizardProps.create(
        false,
        subject,
        null,
        onComplete,
        onCancel
      );

    renderComponent(props);
  }

  public void configure(
    AccessRequirement accessRequirement,
    CreateOrUpdateAccessRequirementWizardProps.OnComplete onComplete,
    CreateOrUpdateAccessRequirementWizardProps.OnCancel onCancel
  ) {
    reactComponentDiv.clear();
    this.accessRequirementId = accessRequirement.getId().toString();
    this.onComplete = onComplete;
    this.onCancel = onCancel;
    CreateOrUpdateAccessRequirementWizardProps props =
      CreateOrUpdateAccessRequirementWizardProps.create(
        false,
        null,
        accessRequirementId,
        onComplete,
        onCancel
      );

    renderComponent(props);
  }

  public void setOpen(boolean open) {
    CreateOrUpdateAccessRequirementWizardProps props =
      CreateOrUpdateAccessRequirementWizardProps.create(
        open,
        subject,
        accessRequirementId,
        onComplete,
        onCancel
      );
    renderComponent(props);
  }

  @Override
  public Widget asWidget() {
    return reactComponentDiv.asWidget();
  }
}
