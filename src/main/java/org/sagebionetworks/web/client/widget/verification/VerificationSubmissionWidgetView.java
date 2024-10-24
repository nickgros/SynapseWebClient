package org.sagebionetworks.web.client.widget.verification;

import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import java.util.List;
import org.sagebionetworks.repo.model.verification.VerificationState;
import org.sagebionetworks.repo.model.verification.VerificationStateEnum;
import org.sagebionetworks.web.client.SynapseView;
import org.sagebionetworks.web.client.jsinterop.RejectProfileValidationRequestModalProps;

public interface VerificationSubmissionWidgetView
  extends SynapseView, IsWidget {
  void clear();

  void show();

  void hide();

  void setSynAlert(Widget w);

  void setFileHandleList(Widget w);

  void setPresenter(Presenter presenter);

  void setTitle(String title);

  void setEmails(List<String> emails);

  void setFirstName(String fname);

  String getFirstName();

  void setLastName(String lname);

  String getLastName();

  void setOrganization(String organization);

  String getOrganization();

  void setLocation(String location);

  String getLocation();

  void setOrcID(String href);

  void setProfileLink(String profileId, String href);

  void setSubmitButtonVisible(boolean visible);

  void setCancelButtonVisible(boolean visible);

  void setOKButtonVisible(boolean visible);

  void setDeleteButtonVisible(boolean visible);

  void setApproveButtonVisible(boolean visible);

  void setRejectButtonVisible(boolean visible);

  void setSuspendButtonVisible(boolean visible);

  void setResubmitButtonVisible(boolean visible);

  void setCloseButtonVisible(boolean visible);

  void setSuspendedReason(String reason);

  void setSuspendedAlertVisible(boolean visible);

  void setState(VerificationStateEnum state);

  void setACTStateHistoryVisible(boolean visible);
  void setACTStateHistory(List<VerificationState> stateHistory);
  void setShowSubmissionInModalButtonVisible(boolean visible);

  void setUploadedFilesUIVisible(boolean visible);

  void popupError(String message);

  void openWindow(String url);

  void setPromptModal(Widget w);

  void setProfileFieldsEditable(boolean editable);

  void showRejectModal(
    String submissionId,
    VerificationStateEnum currentState,
    RejectProfileValidationRequestModalProps.Callback onRejectSuccess
  );

  /**
   * Presenter interface
   */
  public interface Presenter {
    void submitVerification();

    void approveVerification();

    void rejectVerification();

    void suspendVerification();

    void deleteVerification();

    void recreateVerification();

    void showSubmissionInModal();
  }
}
