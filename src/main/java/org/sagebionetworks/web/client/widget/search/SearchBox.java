package org.sagebionetworks.web.client.widget.search;

import java.util.Arrays;

import org.sagebionetworks.repo.model.Entity;
import org.sagebionetworks.repo.model.search.query.SearchQuery;
import org.sagebionetworks.schema.adapter.AdapterFactory;
import org.sagebionetworks.schema.adapter.JSONObjectAdapterException;
import org.sagebionetworks.web.client.DisplayUtils;
import org.sagebionetworks.web.client.EntityTypeProvider;
import org.sagebionetworks.web.client.GlobalApplicationState;
import org.sagebionetworks.web.client.place.Search;
import org.sagebionetworks.web.client.security.AuthenticationController;
import org.sagebionetworks.web.client.transform.NodeModelCreator;
import org.sagebionetworks.web.client.widget.SynapseWidgetPresenter;

import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;

public class SearchBox implements SearchBoxView.Presenter, SynapseWidgetPresenter {
	
	private SearchBoxView view;
	private NodeModelCreator nodeModelCreator;
	private AuthenticationController authenticationController;
	private GlobalApplicationState globalApplicationState;
	private HandlerManager handlerManager = new HandlerManager(this);
	private Entity entity;
	private EntityTypeProvider entityTypeProvider;
	private AdapterFactory adapterFactory;
	private boolean searchAll = false;
	
	@Inject
	public SearchBox(SearchBoxView view, 
			NodeModelCreator nodeModelCreator,
			AuthenticationController authenticationController,
			EntityTypeProvider entityTypeProvider,
			GlobalApplicationState globalApplicationState,
			AdapterFactory adapterFactory) {
		this.view = view;
		this.nodeModelCreator = nodeModelCreator;
		this.authenticationController = authenticationController;
		this.entityTypeProvider = entityTypeProvider;
		this.globalApplicationState = globalApplicationState;
		this.adapterFactory = adapterFactory;
		view.setPresenter(this);
	}	
	
	@Override
	public Widget asWidget() {
		view.setPresenter(this);
		return view.asWidget();		
	}

	@SuppressWarnings("unchecked")
	public void clearState() {
		view.clear();
	}

	@Override
	public void search(String value) {		
		if(searchAll) {
			SearchQuery query = DisplayUtils.getAllTypesSearchQuery();
			query.setQueryTerm(Arrays.asList(value.split(" ")));
			try {
				value = query.writeToJSONObject(adapterFactory.createNew()).toJSONString();
			} catch (JSONObjectAdapterException e) {
				// if fail, fall back on regular search
			}
		}
		globalApplicationState.getPlaceChanger().goTo(new Search(value));
	}

	@Override
	public void setSearchAll(boolean searchAll) {
		this.searchAll = searchAll;
	}

	
	/*
	 * Private Methods
	 */
}
