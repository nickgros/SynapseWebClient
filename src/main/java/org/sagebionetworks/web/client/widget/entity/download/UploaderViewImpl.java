package org.sagebionetworks.web.client.widget.entity.download;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.dom.client.InputElement;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyPressEvent;
import com.google.gwt.event.dom.client.KeyPressHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.FormPanel;
import com.google.gwt.user.client.ui.FormPanel.SubmitCompleteEvent;
import com.google.gwt.user.client.ui.FormPanel.SubmitCompleteHandler;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.InlineHTML;
import com.google.gwt.user.client.ui.PasswordTextBox;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import org.gwtbootstrap3.client.ui.AnchorListItem;
import org.gwtbootstrap3.client.ui.Button;
import org.gwtbootstrap3.client.ui.ButtonGroup;
import org.gwtbootstrap3.client.ui.DropDownMenu;
import org.gwtbootstrap3.client.ui.FieldSet;
import org.gwtbootstrap3.client.ui.Form;
import org.gwtbootstrap3.client.ui.FormGroup;
import org.gwtbootstrap3.client.ui.FormLabel;
import org.gwtbootstrap3.client.ui.Heading;
import org.gwtbootstrap3.client.ui.Input;
import org.gwtbootstrap3.client.ui.NavTabs;
import org.gwtbootstrap3.client.ui.Progress;
import org.gwtbootstrap3.client.ui.ProgressBar;
import org.gwtbootstrap3.client.ui.TabContent;
import org.gwtbootstrap3.client.ui.TabListItem;
import org.gwtbootstrap3.client.ui.TabPane;
import org.gwtbootstrap3.client.ui.TextBox;
import org.gwtbootstrap3.client.ui.constants.AlertType;
import org.gwtbootstrap3.client.ui.constants.ButtonSize;
import org.gwtbootstrap3.client.ui.constants.ButtonType;
import org.gwtbootstrap3.client.ui.constants.HeadingSize;
import org.gwtbootstrap3.client.ui.constants.IconType;
import org.gwtbootstrap3.client.ui.constants.InputType;
import org.gwtbootstrap3.client.ui.constants.ProgressBarType;
import org.gwtbootstrap3.client.ui.constants.Pull;
import org.gwtbootstrap3.client.ui.constants.Toggle;
import org.gwtbootstrap3.client.ui.constants.ValidationState;
import org.gwtbootstrap3.client.ui.html.Div;
import org.gwtbootstrap3.client.ui.html.Italic;
import org.gwtbootstrap3.client.ui.html.Span;
import org.sagebionetworks.web.client.DisplayConstants;
import org.sagebionetworks.web.client.DisplayUtils;
import org.sagebionetworks.web.client.EventHandlerUtils;
import org.sagebionetworks.web.client.PortalGinInjector;
import org.sagebionetworks.web.client.SageImageBundle;
import org.sagebionetworks.web.client.SynapseJSNIUtils;
import org.sagebionetworks.web.client.jsinterop.ToastMessageOptions;
import org.sagebionetworks.web.client.jsinterop.mui.Grid;
import org.sagebionetworks.web.client.place.Synapse;
import org.sagebionetworks.web.client.utils.Callback;
import org.sagebionetworks.web.client.utils.JavaScriptCallback;
import org.sagebionetworks.web.client.widget.FullWidthAlert;
import org.sagebionetworks.web.client.widget.entity.SharingAndDataUseConditionWidget;

/**
 * Note on the form submission. This supports two form submission use cases. 1. Submit to Portal
 * servlet. These will return the response html in the SubmitCompleteHandler. 2. Submit to SFTP
 * proxy servlet. This will not return the response html in the SubmitCompleteHandler (due to CORS).
 * But the sftp proxy will return a page that sends a message (via postMessage) to the parent
 * window. So we set up a listener (onAttach) for this cross-window message.
 *
 * @author jayhodgson
 *
 */
public class UploaderViewImpl extends FlowPanel implements UploaderView {

  private boolean showCancelButton = true;
  private boolean isExternal;

  private TextBox externalUsername;
  private PasswordTextBox externalPassword;

  public static final String FILE_FIELD_ID = "fileToUpload";
  public static final int BUTTON_HEIGHT_PX = 25;
  public static final int BUTTON_WIDTH_PX = 100;

  private Presenter presenter;
  SynapseJSNIUtils synapseJSNIUtils;
  private SageImageBundle sageImageBundle;

  TextBox pathField, nameField;

  // initialized in constructor
  private boolean isEntity;
  private String parentEntityId;
  private FlowPanel formFieldsPanel;
  private FormPanel formPanel;
  private FlowPanel uploadDestinationContainer;
  private FlowPanel fileInputPanel;

  private Form externalLinkFormPanel;
  private FormGroup externalUrlFormGroup;

  private FlowPanel uploadPanel;

  private FullWidthAlert closeToLimitAlert, selectionExceedsLimitAlert, currentlyOverLimitAlert;

  private Button uploadBtn, cancelBtn, chooseSingleFileBtn;
  private ButtonGroup chooseFileButtonGroup;
  AnchorListItem chooseFilesItem, chooseFolderItem;

  private Progress progressContainer;
  private ProgressBar progressBar;
  // external link panel

  private Div spinningProgressContainer;
  private Input fileUploadInput;
  private Heading fileUploadLabel = new Heading(HeadingSize.H5);
  private Italic uploadSpeedLabel = new Italic();
  FlowPanel container;
  SharingAndDataUseConditionWidget sharingDataUseWidget;
  PortalGinInjector ginInjector;

  private HandlerRegistration messageHandler;
  FormGroup externalNameFormGroup;
  AwsLoginView awsLoginView;
  TabListItem externalTab;

  @Inject
  public UploaderViewImpl(
    SynapseJSNIUtils synapseJSNIUtils,
    SageImageBundle sageImageBundle,
    SharingAndDataUseConditionWidget sharingDataUseWidget,
    PortalGinInjector ginInjector,
    AwsLoginView awsLoginView
  ) {
    this.synapseJSNIUtils = synapseJSNIUtils;
    this.sageImageBundle = sageImageBundle;
    this.sharingDataUseWidget = sharingDataUseWidget;
    this.ginInjector = ginInjector;
    this.awsLoginView = awsLoginView;

    this.progressContainer = new Progress();
    progressContainer.setMarginTop(10);
    // striped and animated (helps to indicate that the upload may not be stuck)
    progressContainer.addStyleName("progress-striped active");
    this.progressBar = new ProgressBar();
    progressBar.setType(ProgressBarType.INFO);
    progressContainer.add(progressBar);
    uploadSpeedLabel.addStyleName("margin-left-5");
    progressContainer.add(uploadSpeedLabel);
    this.addStyleName("uploader");
    this.formPanel = new FormPanel();
    this.externalLinkFormPanel = new Form();

    spinningProgressContainer = new Div();

    chooseFileButtonGroup = new ButtonGroup();
    chooseSingleFileBtn = new Button("Click to upload");
    chooseSingleFileBtn.setType(ButtonType.LINK);
    chooseSingleFileBtn.setSize(ButtonSize.LARGE);
    chooseSingleFileBtn.addStyleName("clickToUploadButton");

    Button browseDropdownButton = new Button("Click to upload");
    browseDropdownButton.setType(ButtonType.LINK);
    browseDropdownButton.setSize(ButtonSize.LARGE);
    browseDropdownButton.addStyleName("clickToUploadButton");
    browseDropdownButton.setToggleCaret(false);
    chooseFileButtonGroup.add(browseDropdownButton);
    chooseFileButtonGroup.add(chooseSingleFileBtn);
    chooseFilesItem = new AnchorListItem("Files");
    chooseFilesItem.setIcon(IconType.FILES_O);
    chooseFolderItem = new AnchorListItem("Folder");
    chooseFolderItem.setIcon(IconType.FOLDER_O);
    DropDownMenu dropdownMenu = new DropDownMenu();
    dropdownMenu.add(chooseFilesItem);
    dropdownMenu.add(chooseFolderItem);
    chooseFileButtonGroup.add(dropdownMenu);
    browseDropdownButton.setDataToggle(Toggle.DROPDOWN);
    uploadBtn = new Button();
    uploadBtn.setType(ButtonType.PRIMARY);
    uploadBtn.setPull(Pull.RIGHT);

    cancelBtn = new Button(DisplayConstants.BUTTON_CANCEL);
    cancelBtn.setType(ButtonType.LINK);
    cancelBtn.setPull(Pull.RIGHT);
    cancelBtn.setMarginRight(5);

    pathField = new TextBox();
    initUploadPanel();
    initExternalPanel();

    initHandlers();
  }

  private void initHandlers() {
    uploadBtn.addClickHandler(
      new ClickHandler() {
        @Override
        public void onClick(ClickEvent event) {
          if (isExternal) {
            String url = pathField.getValue();
            // let the service decide what is a valid url (now supporting sftp, and perhaps others)
            if (url == null || url.isEmpty()) {
              externalUrlFormGroup.setValidationState(ValidationState.ERROR);
              return;
            }

            presenter.setExternalFilePath(
              pathField.getValue(),
              nameField.getValue(),
              presenter.getStorageLocationId()
            );
          } else {
            presenter.handleUploads();
          }
        }
      }
    );

    cancelBtn.addClickHandler(
      new ClickHandler() {
        @Override
        public void onClick(ClickEvent event) {
          presenter.cancelClicked();
        }
      }
    );

    SubmitCompleteHandler submitHandler = new SubmitCompleteHandler() {
      @Override
      public void onSubmitComplete(SubmitCompleteEvent event) {
        handleSubmitResult(event.getResults());
      }
    };
    formPanel.addSubmitCompleteHandler(submitHandler);
  }

  @Override
  public void disableSelectionDuringUpload() {
    formFieldsPanel.setVisible(false);
    fileUploadLabel.setVisible(false);
    uploadBtn.setEnabled(false);
    // SWC-1730: disable clicking on external tab
    externalTab.setEnabled(false);

    initializeProgress();
  }

  private void handleSubmitResult(String result) {
    if (result != null) {
      presenter.handleSubmitResult(result);
      hideLoading();
    }
  }

  public static native String _getMessage(JavaScriptObject event) /*-{
		console.log("event received: " + event);
		console.log("event.data received: " + event.data);
		if (event !== undefined && event.data !== undefined)
			return event.data;
		else
			return null;
	}-*/;

  private void clearMessageHandler() {
    if (messageHandler != null) {
      messageHandler.removeHandler();
      messageHandler = null;
    }
  }

  protected void initMessageHandler() {
    clearMessageHandler();
    // register to listen for the "message" events
    messageHandler =
      EventHandlerUtils.addEventListener(
        "message",
        EventHandlerUtils.getWnd(),
        new JavaScriptCallback() {
          @Override
          public void invoke(JavaScriptObject event) {
            handleSubmitResult(_getMessage(event));
            clearMessageHandler();
          }
        }
      );
  }

  @Override
  public void resetToInitialState() {
    hideLoading();
    enableUpload();
    // Clear previously selected files.
    enableMultipleFileUploads(true);
    fileUploadInput.setValue(null);
    fileUploadLabel.setText("");
    uploadSpeedLabel.setHTML("");
    awsLoginView.clear();
  }

  @Override
  public void enableUpload() {
    uploadBtn.setEnabled(true);
    formFieldsPanel.setVisible(true);
    fileUploadLabel.setVisible(true);
  }

  @Override
  public String getExternalUsername() {
    return externalUsername.getValue();
  }

  @Override
  public String getExternalPassword() {
    return externalPassword.getValue();
  }

  @Override
  public Widget asWidget() {
    return this;
  }

  @Override
  public void triggerUpload() {
    uploadBtn.click();
  }

  @Override
  public void setPresenter(Presenter presenter) {
    this.presenter = presenter;
  }

  @Override
  public void showErrorMessage(String message) {
    DisplayUtils.showErrorMessage(message);
  }

  @Override
  public void showErrorMessage(String title, String message) {
    DisplayUtils.showErrorMessage(title, message);
  }

  @Override
  public void showLoading() {
    spinningProgressContainer.clear();
    spinningProgressContainer.add(
      DisplayUtils.getLoadingWidget(DisplayConstants.LABEL_INITIALIZING)
    );
    spinningProgressContainer.addStyleName("margin-top-10");
  }

  @Override
  public void showInfo(String message) {
    DisplayUtils.showInfo(message);
  }

  @Override
  public void showSingleFileUploaded(String entityId) {
    ToastMessageOptions toastOptions = new ToastMessageOptions.Builder()
      .setPrimaryButton(
        "View File",
        () ->
          ginInjector
            .getGlobalApplicationState()
            .getPlaceChanger()
            .goTo(new Synapse(entityId))
      )
      .build();
    DisplayUtils.notify(
      DisplayConstants.TEXT_UPLOAD_SUCCESS,
      DisplayUtils.NotificationVariant.INFO,
      toastOptions
    );
  }

  @Override
  public void clear() {
    super.clear();
    if (pathField != null) externalUrlFormGroup.setValidationState(
      ValidationState.NONE
    );
    pathField.clear();
    if (nameField != null) nameField.clear();
    if (externalUsername != null) {
      externalUsername.clear();
      externalUsername.setVisible(false);
      externalUsername.setPlaceholder("Username");
    }
    if (externalPassword != null) {
      externalPassword.setValue("");
      externalPassword.setVisible(false);
      externalPassword.getElement().setAttribute("placeholder", "Password");
    }
    awsLoginView.clear();
    awsLoginView.setVisible(false);

    // SWC-1730: enable clicking on external tab
    externalTab.setEnabled(true);
  }

  @Override
  public void createUploadForm(boolean isEntity, String parentEntityId) {
    this.isEntity = isEntity;
    this.parentEntityId = parentEntityId;
    initializeControls();

    createUploadContents();

    // reset
    if (pathField != null) pathField.clear();
    if (nameField != null) nameField.clear();
  }

  @Override
  public void updateProgress(double value, String text, String uploadSpeed) {
    progressBar.setText(text);
    progressBar.setPercent(value * 100);
    uploadSpeedLabel.setHTML(
      SafeHtmlUtils.htmlEscapeAllowEntities(
        uploadSpeed.replaceAll(" ", "&nbsp;")
      )
    );
  }

  @Override
  public void setShowCancelButton(boolean showCancel) {
    this.showCancelButton = showCancel;
  }

  @Override
  public void hideLoading() {
    // try to hide the loading progress bar. ignore any errors
    resetProgress();
    progressContainer.setVisible(false);
    uploadSpeedLabel.setHTML("");
    spinningProgressContainer.clear();
    spinningProgressContainer.setVisible(false);
  }

  private void resetProgress() {
    progressBar.setPercent(0.0);
    progressBar.setText("");
    uploadSpeedLabel.setHTML("");
  }

  @Override
  public void submitForm(String actionUrl) {
    initMessageHandler();
    showSpinningProgress();
    formPanel.setAction(actionUrl);
    spinningProgressContainer.clear();
    spinningProgressContainer.add(
      DisplayUtils.getLoadingWidget(DisplayConstants.LABEL_UPLOADING)
    );
    formPanel.submit();
  }

  @Override
  public void enableMultipleFileUploads(boolean isEnabled) {
    if (chooseFileButtonGroup != null && chooseSingleFileBtn != null) {
      chooseFileButtonGroup.setVisible(isEnabled);
      chooseSingleFileBtn.setVisible(!isEnabled);
    }
  }

  /*
   * Private Methods
   */
  private void createUploadContents() {
    if (container == null) this.container =
      new FlowPanel(); else container.clear();
    container.add(
      new HTML("<div style=\"padding: 5px 10px 0px 15px;\"></div>")
    );
    uploadPanel.removeFromParent();
    if (isEntity) {
      // create tabs
      NavTabs tabs = new NavTabs();
      TabContent tabContent = new TabContent();

      // Upload File
      TabListItem tab = new TabListItem(DisplayConstants.UPLOAD_FILE);
      tab.setDataTarget("#uploadTab");
      tab.addStyleName("entity-tab");
      tab.setActive(true);
      tabs.add(tab);
      TabPane tabPanel = new TabPane();
      tabPanel.setActive(true);
      tabPanel.setId("uploadTab");
      tabPanel.add(uploadPanel);
      tabContent.add(tabPanel);
      tab.addClickHandler(
        new ClickHandler() {
          @Override
          public void onClick(ClickEvent event) {
            configureUploadButton();
          }
        }
      );

      // External URL
      tab = new TabListItem(DisplayConstants.LINK_TO_URL);
      tab.setDataTarget("#externalTab");
      tab.addStyleName("entity-tab");
      tabs.add(tab);
      externalLinkFormPanel.removeFromParent();
      tabPanel = new TabPane();
      tabPanel.setId("externalTab");
      tabPanel.add(externalLinkFormPanel);
      tabContent.add(tabPanel);
      tab.addClickHandler(
        new ClickHandler() {
          @Override
          public void onClick(ClickEvent event) {
            configureUploadButtonForExternal();
          }
        }
      );
      externalTab = tab;

      container.add(tabs);
      container.add(tabContent);
    } else {
      container.add(uploadPanel);
      configureUploadButton();
    }

    if (isEntity && parentEntityId != null) {
      // add sharing settings and data use conditions (associated to the parent)
      sharingDataUseWidget.configure(parentEntityId);
      container.add(sharingDataUseWidget.asWidget());
    }

    Grid row = new Grid();
    row.setContainer(true);
    Grid col = new Grid();
    col.setXs(12);
    col.add(uploadBtn);
    if (showCancelButton) {
      col.add(cancelBtn);
    }
    row.add(col);
    container.add(row);
  }

  @Override
  public void showUploaderUI() {
    clear();
    add(container);
  }

  @Override
  public void showConfirmDialog(
    String message,
    final Callback yesCallback,
    final Callback noCallback
  ) {
    DisplayUtils.showConfirmDialog(
      DisplayConstants.UPLOAD_DIALOG_TITLE,
      message,
      yesCallback,
      noCallback
    );
  }

  // set the initial state of the controls when widget is made visible
  private void initializeControls() {
    if (formPanel.isVisible()) formPanel.reset(); // clear file choice from fileUploadField

    configureUploadButton();
    progressContainer.setVisible(false);
  }

  private void initializeProgress() {
    showSpinningProgress();
    spinningProgressContainer.clear();
    spinningProgressContainer.add(
      DisplayUtils.getLoadingWidget(DisplayConstants.LABEL_INITIALIZING)
    );
  }

  @Override
  public void showProgressBar() {
    resetProgress();
    progressContainer.setVisible(true);
    spinningProgressContainer.setVisible(false);
  }

  private void showSpinningProgress() {
    spinningProgressContainer.setVisible(true);
    resetProgress();
    progressContainer.setVisible(false);
  }

  private void initUploadPanel() {
    uploadDestinationContainer = new FlowPanel();
    uploadDestinationContainer.addStyleName("margin-top-10 margin-bottom-10");
    formPanel.setEncoding(FormPanel.ENCODING_MULTIPART);
    formPanel.setMethod(FormPanel.METHOD_POST);
    fileInputPanel = new FlowPanel();
    fileInputPanel.addStyleName("uploadContainer center margin-top-15");
    fileUploadInput = new Input(InputType.FILE);
    fileUploadInput.setId(FILE_FIELD_ID);
    fileUploadInput.setName("uploads[]");
    fileUploadInput.setVisible(false);
    fileUploadLabel.addStyleName("displayInline margin-left-5");
    fileUploadInput.addValueChangeHandler(
      new ValueChangeHandler<String>() {
        @Override
        public void onValueChange(ValueChangeEvent<String> event) {
          uploadBtn.click();
        }
      }
    );
    chooseFilesItem.addClickHandler(event -> {
      // click file upload input field
      fileUploadInput.getElement().setAttribute("multiple", null);
      fileUploadInput.getElement().removeAttribute("webkitdirectory");
      fileUploadInput.getElement().<InputElement>cast().click();
    });
    chooseFolderItem.addClickHandler(event -> {
      // click file upload input field
      fileUploadInput.getElement().removeAttribute("multiple");
      fileUploadInput.getElement().setAttribute("webkitdirectory", "");
      fileUploadInput.getElement().<InputElement>cast().click();
    });

    chooseSingleFileBtn.addClickHandler(event -> {
      fileUploadInput.getElement().removeAttribute("multiple");
      fileUploadInput.getElement().removeAttribute("webkitdirectory");
      fileUploadInput.getElement().<InputElement>cast().click();
    });

    FlowPanel uploadIllustration = new FlowPanel();
    uploadIllustration.add(
      new Image(
        "https://s3.us-east-1.amazonaws.com/static.synapse.org/images/upload_illustration.svg"
      )
    );
    uploadIllustration.addStyleName("padding-top-15 padding-bottom-15");

    fileInputPanel.add(uploadIllustration);
    fileInputPanel.add(fileUploadInput);
    fileInputPanel.add(chooseSingleFileBtn);
    fileInputPanel.add(chooseFileButtonGroup);
    Span dropText = new Span("or drag and drop");
    dropText.addStyleName("margin-right-5 font-size-20 movedown-2");
    fileInputPanel.add(dropText);

    fileInputPanel.add(fileUploadLabel);
    fileInputPanel.add(uploadDestinationContainer);
    enableMultipleFileUploads(true);
    formFieldsPanel = new FlowPanel();

    externalUsername = new TextBox();
    externalUsername.addStyleName("margin-bottom-5");
    externalUsername.setName("username");
    externalPassword = new PasswordTextBox();
    externalPassword.setName("password");
    externalPassword.setStyleName("form-control margin-bottom-5");
    externalUsername.setVisible(false);
    externalPassword.setVisible(false);
    awsLoginView.setVisible(false);

    formFieldsPanel.add(externalUsername);
    formFieldsPanel.add(externalPassword);
    formFieldsPanel.add(awsLoginView);
    formFieldsPanel.add(fileInputPanel);

    configureUploadButton(); // upload tab first by default

    closeToLimitAlert = new FullWidthAlert();
    closeToLimitAlert.setGlobal(false);
    closeToLimitAlert.setAlertType(AlertType.WARNING);
    // TODO: Number should come from API
    closeToLimitAlert.setMessageTitle("Your storage availability is limited");
    closeToLimitAlert.setMessage(
      "Your organization only has XXX MB remaining of available storage left. In order to upload more files, you must edit your file list or upgrade your plan."
    );
    closeToLimitAlert.setPrimaryCTAText("Learn more");
    closeToLimitAlert.setPrimaryCTAHref(
      "https://help.synapse.org/docs/Sage-Offerings.2965078125.html"
    );
    closeToLimitAlert.setVisible(false);

    selectionExceedsLimitAlert = new FullWidthAlert();
    selectionExceedsLimitAlert.setGlobal(false);
    selectionExceedsLimitAlert.setAlertType(AlertType.DANGER);
    // TODO: Appropriately pluralize File is / Files are
    selectionExceedsLimitAlert.setMessageTitle("File is too large");
    // TODO: Number should come from API
    selectionExceedsLimitAlert.setMessage(
      "You only have 1 MB of storage remaining within this project. In order to upload more files, you must edit your files or upgrade your plan."
    );
    selectionExceedsLimitAlert.setPrimaryCTAText("Learn more");
    selectionExceedsLimitAlert.setPrimaryCTAHref(
      "https://help.synapse.org/docs/Sage-Offerings.2965078125.html"
    );
    selectionExceedsLimitAlert.setVisible(false);

    currentlyOverLimitAlert = new FullWidthAlert();
    currentlyOverLimitAlert.setGlobal(false);
    currentlyOverLimitAlert.setAlertType(AlertType.DANGER);
    currentlyOverLimitAlert.setMessageTitle("You have no data availability");
    currentlyOverLimitAlert.setMessage(
      "You do not have any remaining data within this project. In order to upload more files, you must edit your file list or upgrade your plan."
    );
    currentlyOverLimitAlert.setPrimaryCTAText("Learn more");
    currentlyOverLimitAlert.setPrimaryCTAHref(
      "https://help.synapse.org/docs/Sage-Offerings.2965078125.html"
    );
    currentlyOverLimitAlert.setVisible(false);

    formPanel.setWidget(formFieldsPanel);
    uploadPanel = new FlowPanel();
    uploadPanel.add(closeToLimitAlert);
    uploadPanel.add(selectionExceedsLimitAlert);
    uploadPanel.add(currentlyOverLimitAlert);
    uploadPanel.add(formPanel);

    Grid row = new Grid();
    row.setContainer(true);
    Grid col = new Grid();
    col.setXs(12);
    col.add(spinningProgressContainer);
    col.add(progressContainer);
    row.add(col);
    uploadPanel.add(row);
  }

  @Override
  public void showUploadingToS3DirectStorage(String endpoint, String banner) {
    awsLoginView.clear();
    uploadDestinationContainer.clear();
    String escapedEndpoint = SafeHtmlUtils.htmlEscape(endpoint);
    awsLoginView.setEndpoint(escapedEndpoint);
    if (banner != null) uploadDestinationContainer.add(
      new HTML(SafeHtmlUtils.htmlEscape(banner))
    );
    awsLoginView.setVisible(true);
  }

  @Override
  public String getS3DirectAccessKey() {
    return awsLoginView.getAccessKey();
  }

  @Override
  public String getS3DirectSecretKey() {
    return awsLoginView.getSecretKey();
  }

  @Override
  public void showUploadingToExternalStorage(String host, String banner) {
    uploadDestinationContainer.clear();
    String escapedHost = SafeHtmlUtils.htmlEscape(host);
    uploadDestinationContainer.add(
      new HTML(
        DisplayConstants.UPLOAD_DESTINATION +
        "<strong>" +
        escapedHost +
        "</strong>"
      )
    );
    if (banner != null) uploadDestinationContainer.add(
      new HTML(SafeHtmlUtils.htmlEscape(banner))
    );
    // add the host to the field names too
    externalUsername.setPlaceholder(escapedHost + " username");
    externalPassword
      .getElement()
      .setAttribute("placeholder", escapedHost + " password");

    externalUsername.setVisible(true);
    externalPassword.setVisible(true);
  }

  @Override
  public void showUploadingToSynapseStorage() {
    uploadDestinationContainer.clear();
    uploadDestinationContainer.add(
      new InlineHTML(DisplayConstants.UPLOAD_DESTINATION + " Synapse storage")
    );
  }

  @Override
  public void showUploadingBanner(String banner) {
    uploadDestinationContainer.clear();
    uploadDestinationContainer.add(new HTML(SafeHtmlUtils.htmlEscape(banner)));
  }

  private void initExternalPanel() {
    pathField = new TextBox();
    nameField = new TextBox();

    FieldSet set = new FieldSet();

    externalUrlFormGroup = new FormGroup();
    FormLabel l = new FormLabel();
    l.setText("URL");
    externalUrlFormGroup.add(l);
    externalUrlFormGroup.add(pathField);
    set.add(externalUrlFormGroup);

    externalNameFormGroup = new FormGroup();
    l = new FormLabel();
    l.setText("Name (Optional)");
    externalNameFormGroup.add(l);
    externalNameFormGroup.add(nameField);
    set.add(externalNameFormGroup);

    externalLinkFormPanel.add(set);
    externalLinkFormPanel.addStyleName("margin-top-10 margin-bottom-10");
    pathField.addKeyPressHandler(
      new KeyPressHandler() {
        @Override
        public void onKeyPress(KeyPressEvent event) {
          uploadBtn.setEnabled(true);
        }
      }
    );
  }

  private void configureUploadButton() {
    isExternal = false;
    uploadBtn.setText("Save");
  }

  private void configureUploadButtonForExternal() {
    isExternal = true;
    uploadBtn.setText("Save");
  }

  @Override
  public void setUploaderLinkNameVisible(boolean visible) {
    externalNameFormGroup.setVisible(visible);
  }

  @Override
  public void setShowCloseToLimitAlert(boolean visible) {
    closeToLimitAlert.setVisible(visible);
  }

  @Override
  public void setShowSelectionExceedsLimitAlert(boolean visible) {
    selectionExceedsLimitAlert.setVisible(visible);
  }

  @Override
  public void setShowCurrentlyOverLimitAlert(boolean visible) {
    currentlyOverLimitAlert.setVisible(visible);
  }

  @Override
  public void setUploadEnabled(boolean enabled) {
    if (enabled) {
      uploadDestinationContainer.addStyleName("disabled");
    } else {
      uploadDestinationContainer.removeStyleName("disabled");
    }
  }

  @Override
  public void setExternalUrl(String url) {
    pathField.setValue(url);
  }

  @Override
  public void setSelectedFilenames(String fileNames) {
    fileUploadLabel.setText(fileNames);
  }
}
