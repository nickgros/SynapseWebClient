package org.sagebionetworks.web.client.widget.entity.browse;

import java.util.List;

import org.sagebionetworks.repo.model.Reference;
import org.sagebionetworks.web.client.SynapseView;

import com.google.gwt.user.client.ui.Widget;

public interface EntityFinderV2View extends SynapseView {

	/**
	 * Set the presenter.
	 * 
	 * @param presenter
	 */
	void setPresenter(Presenter presenter);

	void show();

	void hide();

	void renderComponent(EntityFinderScope initialScope, String initialContainerId, boolean showVersions, boolean multiSelect, EntityFilter selectableTypes, EntityFilter visibleTypesInList, EntityFilter visibleTypesInTree, String selectedCopy);

	void setErrorMessage(String errorMessage);

	void clearError();

    void setModalTitle(String modalTitle);

	void setPromptCopy(String promptCopy);

	void setHelpMarkdown(String helpMarkdown);

	void setConfirmButtonCopy(String confirmButtonCopy);

	Widget asWidget();

	/**
	 * Presenter interface
	 */
	interface Presenter {

		void setSelectedEntity(Reference selected);

		void okClicked();

		void setSelectedEntities(List<Reference> selected);

		void clearSelectedEntities();

		void renderComponent();
	}


}
