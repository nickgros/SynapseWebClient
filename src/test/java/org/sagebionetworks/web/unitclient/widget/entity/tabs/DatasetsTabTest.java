package org.sagebionetworks.web.unitclient.widget.entity.tabs;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyBoolean;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.Date;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import org.sagebionetworks.repo.model.EntityHeader;
import org.sagebionetworks.repo.model.EntityPath;
import org.sagebionetworks.repo.model.EntityType;
import org.sagebionetworks.repo.model.FileEntity;
import org.sagebionetworks.repo.model.Project;
import org.sagebionetworks.repo.model.auth.UserEntityPermissions;
import org.sagebionetworks.repo.model.entitybundle.v2.EntityBundle;
import org.sagebionetworks.repo.model.table.Dataset;
import org.sagebionetworks.repo.model.table.EntityView;
import org.sagebionetworks.repo.model.table.Query;
import org.sagebionetworks.repo.model.table.SortDirection;
import org.sagebionetworks.repo.model.table.SortItem;
import org.sagebionetworks.web.client.DisplayUtils;
import org.sagebionetworks.web.client.GlobalApplicationState;
import org.sagebionetworks.web.client.PlaceChanger;
import org.sagebionetworks.web.client.PortalGinInjector;
import org.sagebionetworks.web.client.SynapseJavascriptClient;
import org.sagebionetworks.web.client.cookie.CookieProvider;
import org.sagebionetworks.web.client.place.Synapse;
import org.sagebionetworks.web.client.place.Synapse.EntityArea;
import org.sagebionetworks.web.client.utils.CallbackP;
import org.sagebionetworks.web.client.widget.breadcrumb.Breadcrumb;
import org.sagebionetworks.web.client.widget.entity.EntityMetadata;
import org.sagebionetworks.web.client.widget.entity.ModifiedCreatedByWidget;
import org.sagebionetworks.web.client.widget.entity.controller.StuAlert;
import org.sagebionetworks.web.client.widget.entity.file.BasicTitleBar;
import org.sagebionetworks.web.client.widget.entity.menu.v2.ActionMenuWidget;
import org.sagebionetworks.web.client.widget.entity.tabs.DatasetsTab;
import org.sagebionetworks.web.client.widget.entity.tabs.DatasetsTabView;
import org.sagebionetworks.web.client.widget.entity.tabs.Tab;
import org.sagebionetworks.web.client.widget.entity.tabs.TablesTab;
import org.sagebionetworks.web.client.widget.provenance.ProvenanceWidget;
import org.sagebionetworks.web.client.widget.table.TableListWidget;
import org.sagebionetworks.web.client.widget.table.v2.QueryTokenProvider;
import org.sagebionetworks.web.client.widget.table.v2.TableEntityWidget;
import org.sagebionetworks.web.shared.WidgetConstants;

import com.google.gwt.place.shared.Place;
import com.google.gwt.user.client.ui.Widget;

@RunWith(MockitoJUnitRunner.class)
public class DatasetsTabTest {
	@Mock
	Tab mockTab;
	@Mock
	CallbackP<Tab> mockOnClickCallback;
	@Mock
	DatasetsTabView mockView;
	@Mock
	TableListWidget mockTableListWidget;
	@Mock
	BasicTitleBar mockTitleBar;
	@Mock
	Breadcrumb mockBreadcrumb;
	@Mock
	EntityMetadata mockEntityMetadata;
	@Mock
	ProvenanceWidget mockProvenanceWidget;
	@Mock
	StuAlert mockSynapseAlert;
	@Mock
	PortalGinInjector mockPortalGinInjector;

	@Mock
	EntityBundle mockProjectEntityBundle;
	@Mock
	EntityBundle mockDatasetBundle;
	@Mock
	Dataset mockDataset;
	@Mock
	EntityView mockFileViewEntity;
	@Mock
	Project mockProjectEntity;
	@Mock
	UserEntityPermissions mockPermissions;
	@Mock
	ActionMenuWidget mockActionMenuWidget;
	@Mock
	CallbackP<String> mockEntitySelectedCallback;
	@Mock
	TableEntityWidget mockTableEntityWidget;
	@Mock
	ModifiedCreatedByWidget mockModifiedCreatedBy;
	@Captor
	ArgumentCaptor<CallbackP> callbackPCaptor;
	@Mock
	EntityHeader mockEntityHeader;
	@Mock
	CookieProvider mockCookies;
	@Mock
	GlobalApplicationState mockGlobalApplicationState;
	@Mock
	PlaceChanger mockPlaceChanger;
	String projectEntityId = "syn666666";
	String projectName = "a test project";
	String datasetId = "syn22";
	String datasetName = "test dataset";
	@Mock
	QueryTokenProvider mockQueryTokenProvider;
	@Mock
	SynapseJavascriptClient mockJsClient;
	@Captor
	ArgumentCaptor<Map<String, String>> mapCaptor;
	@Captor
	ArgumentCaptor<Place> placeCaptor;
	DatasetsTab tab;
	Query query;

	@Before
	public void setUp() {
		tab = new DatasetsTab(mockTab, mockPortalGinInjector);
		when(mockTab.getEntityActionMenu()).thenReturn(mockActionMenuWidget);
		when(mockCookies.getCookie(eq(DisplayUtils.SYNAPSE_TEST_WEBSITE_COOKIE_KEY))).thenReturn("true");
		when(mockPortalGinInjector.getCookieProvider()).thenReturn(mockCookies);
		when(mockPortalGinInjector.getDatasetsTabView()).thenReturn(mockView);
		when(mockPortalGinInjector.getTableListWidget()).thenReturn(mockTableListWidget);
		when(mockPortalGinInjector.getBasicTitleBar()).thenReturn(mockTitleBar);
		when(mockPortalGinInjector.getBreadcrumb()).thenReturn(mockBreadcrumb);
		when(mockPortalGinInjector.getEntityMetadata()).thenReturn(mockEntityMetadata);
		when(mockPortalGinInjector.getQueryTokenProvider()).thenReturn(mockQueryTokenProvider);
		when(mockPortalGinInjector.getStuAlert()).thenReturn(mockSynapseAlert);
		when(mockPortalGinInjector.getModifiedCreatedByWidget()).thenReturn(mockModifiedCreatedBy);
		when(mockPortalGinInjector.getProvenanceRenderer()).thenReturn(mockProvenanceWidget);
		when(mockPortalGinInjector.getGlobalApplicationState()).thenReturn(mockGlobalApplicationState);
		when(mockPortalGinInjector.getSynapseJavascriptClient()).thenReturn(mockJsClient);

		when(mockGlobalApplicationState.getPlaceChanger()).thenReturn(mockPlaceChanger);
		tab.setEntitySelectedCallback(mockEntitySelectedCallback);
		when(mockProjectEntityBundle.getEntity()).thenReturn(mockProjectEntity);
		when(mockProjectEntity.getId()).thenReturn(projectEntityId);
		when(mockProjectEntity.getName()).thenReturn(projectName);
		when(mockProjectEntityBundle.getPermissions()).thenReturn(mockPermissions);

		when(mockPortalGinInjector.createNewTableEntityWidget()).thenReturn(mockTableEntityWidget);

		when(mockDatasetBundle.getEntity()).thenReturn(mockDataset);
		when(mockDataset.getId()).thenReturn(datasetId);
		when(mockDataset.getName()).thenReturn(datasetName);
		when(mockDatasetBundle.getPermissions()).thenReturn(mockPermissions);

		when(mockFileViewEntity.getId()).thenReturn(datasetId);
		when(mockFileViewEntity.getName()).thenReturn(datasetName);

		// setup a complex query.
		query = new Query();
		query.setSql("select one, two, three from syn22 where name=\"bar\" and type in('one','two','three'");
		query.setLimit(101L);
		query.setOffset(33L);
		SortItem one = new SortItem();
		one.setColumn("one");
		one.setDirection(SortDirection.ASC);
		SortItem two = new SortItem();
		two.setColumn("one");
		two.setDirection(SortDirection.DESC);
		query.setSort(Arrays.asList(one, two));
		tab.lazyInject();
	}

	@Test
	public void testSetTabClickedCallback() {
		tab.setTabClickedCallback(mockOnClickCallback);
		verify(mockTab).addTabClickedCallback(mockOnClickCallback);
	}

	@Test
	public void testClickTable() {
		// verify that clicking on an item in the datasets list sends the event back to the entity page top
		// (to get the new target entity)
		String newEntityId = "syn9839838";
		when(mockEntityHeader.getId()).thenReturn(newEntityId);
		verify(mockTableListWidget).setTableClickedCallback(callbackPCaptor.capture());
		callbackPCaptor.getValue().invoke(mockEntityHeader);
		verify(mockEntitySelectedCallback).invoke(newEntityId);
	}

	@Test
	public void testConfigure() {
		Long version = null;
		String areaToken = null;

		boolean canCertifiedUserEdit = true;
		boolean isCertifiedUser = false;
		when(mockPermissions.getCanCertifiedUserEdit()).thenReturn(canCertifiedUserEdit);
		when(mockPermissions.getIsCertifiedUser()).thenReturn(isCertifiedUser);

		tab.setProject(projectEntityId, mockProjectEntityBundle, null);
		tab.configure(mockDatasetBundle, version, areaToken);

		verifyTableConfiguration(version);
	}

	private void verifyTableConfiguration(Long version) {
		verify(mockBreadcrumb).configure(any(EntityPath.class), eq(EntityArea.DATASETS));
		verify(mockTitleBar).configure(mockDatasetBundle);
		verify(mockEntityMetadata).configure(mockDatasetBundle, version, mockActionMenuWidget);
		verify(mockTableEntityWidget).configure(mockDatasetBundle, version, true, tab, mockActionMenuWidget);
		verify(mockView).setTableEntityWidget(any(Widget.class));
		verify(mockModifiedCreatedBy).configure(any(Date.class), anyString(), any(Date.class), anyString());
		verify(mockActionMenuWidget).setTableDownloadOptionsVisible(true);
		verify(mockProvenanceWidget).configure(mapCaptor.capture());
		// verify configuration
		Map<String, String> provConfig = mapCaptor.getValue();
		String provEntityList = provConfig.get(WidgetConstants.PROV_WIDGET_ENTITY_LIST_KEY);
		String expectedProvEntityList = DisplayUtils.createEntityVersionString(mockDatasetBundle.getEntity().getId(), version);
		assertEquals(expectedProvEntityList, provEntityList);
		verify(mockView).setTableUIVisible(true);
		verify(mockView).setEntityMetadataVisible(true);
		verify(mockView).setBreadcrumbVisible(true);
		verify(mockView).setTableListVisible(false);
		verify(mockView).setTitlebarVisible(true);
		verify(mockView).clearTableEntityWidget();
		verify(mockModifiedCreatedBy).setVisible(false);

		ArgumentCaptor<Synapse> captor = ArgumentCaptor.forClass(Synapse.class);
		verify(mockTab).setEntityNameAndPlace(eq(datasetName), captor.capture());
		Synapse place = (Synapse) captor.getValue();
		assertEquals(datasetId, place.getEntityId());
		assertEquals(version, place.getVersionNumber());
		assertEquals(EntityArea.DATASETS, place.getArea());
		assertNull(place.getAreaToken());
	}

	@Test
	public void testConfigureUsingProject() {
		// configures using a project if the target entity is anything other than a dataset
		FileEntity file = mock(FileEntity.class);
		Long version = null;

		String areaToken = null;

		boolean canCertifiedUserAddChild = true;
		boolean isCertifiedUser = false;
		when(mockPermissions.getCanCertifiedUserAddChild()).thenReturn(canCertifiedUserAddChild);
		when(mockPermissions.getIsCertifiedUser()).thenReturn(isCertifiedUser);

		tab.setProject(projectEntityId, mockProjectEntityBundle, null);
		tab.configure(mockProjectEntityBundle, version, areaToken);
		verify(mockModifiedCreatedBy, Mockito.never()).configure(any(Date.class), anyString(), any(Date.class), anyString());
		verify(mockView).setEntityMetadataVisible(false);
		verify(mockView).setBreadcrumbVisible(false);
		verify(mockView).setTableListVisible(true);
		verify(mockView).setTitlebarVisible(false);
		verify(mockView).clearTableEntityWidget();
		verify(mockModifiedCreatedBy).setVisible(false);
		verify(mockView).setTableUIVisible(false);
		verify(mockView, never()).setTableUIVisible(true);
		verify(mockActionMenuWidget).setTableDownloadOptionsVisible(false);

		verify(mockTableListWidget).configure(mockProjectEntityBundle, Arrays.asList(EntityType.dataset));

		Synapse place = getNewPlace(projectName);
		assertEquals(projectEntityId, place.getEntityId());
		assertNull(place.getVersionNumber());
		assertEquals(EntityArea.DATASETS, place.getArea());
		assertNull(place.getAreaToken());
	}

	@Test
	public void testAsTab() {
		assertEquals(mockTab, tab.asTab());
	}

	private Synapse getNewPlace(String expectedName) {
		ArgumentCaptor<Synapse> captor = ArgumentCaptor.forClass(Synapse.class);
		verify(mockTab, atLeastOnce()).setEntityNameAndPlace(eq(expectedName), captor.capture());
		return (Synapse) captor.getValue();
	}

	@Test
	public void testSetTableQueryWithNoToken() {
		Long version = null;
		tab.setProject(projectEntityId, mockProjectEntityBundle, null);
		tab.configure(mockDatasetBundle, version, null);

		reset(mockTab);
		when(mockTab.isTabPaneVisible()).thenReturn(true);
		when(mockTableEntityWidget.getDefaultQuery()).thenReturn(query);
		tab.onQueryChange(query);

		Synapse place = getNewPlace(datasetName);
		assertEquals(EntityArea.DATASETS, place.getArea());
		assertTrue(place.getAreaToken().isEmpty());
	}

	@Test
	public void testSetQueryPaneNotVisible() {
		Long version = null;
		tab.setProject(projectEntityId, mockProjectEntityBundle, null);
		tab.configure(mockDatasetBundle, version, null);
		reset(mockTab);
		when(mockTab.isTabPaneVisible()).thenReturn(false);
		when(mockTableEntityWidget.getDefaultQuery()).thenReturn(query);
		tab.onQueryChange(query);

		verify(mockTab, never()).setEntityNameAndPlace(anyString(), any(Synapse.class));
		verify(mockTab, never()).showTab(anyBoolean());
	}

	@Test
	public void testSetTableQueryChangeVersion() {
		Long version = 9229L;
		tab.setProject(projectEntityId, mockProjectEntityBundle, null);
		tab.configure(mockDatasetBundle, version, null);

		reset(mockTab);
		when(mockTab.isTabPaneVisible()).thenReturn(true);
		when(mockTableEntityWidget.getDefaultQuery()).thenReturn(query);
		Long newVersion = 88888888L;
		query.setSql("select * from " + datasetId + "." + newVersion.toString() + " where x=1");
		tab.onQueryChange(query);

		verify(mockPlaceChanger).goTo(placeCaptor.capture());
		Synapse place = (Synapse) placeCaptor.getValue();
		assertEquals(EntityArea.DATASETS, place.getArea());
		assertTrue(place.getAreaToken().isEmpty());
		assertEquals(newVersion, place.getVersionNumber());
	}

	@Test
	public void testSetTableQueryChangeTableId() {
		Long version = null;
		tab.setProject(projectEntityId, mockProjectEntityBundle, null);
		tab.configure(mockDatasetBundle, version, null);

		reset(mockTab);
		when(mockTab.isTabPaneVisible()).thenReturn(true);
		when(mockTableEntityWidget.getDefaultQuery()).thenReturn(query);

		query.setSql("select * from syn837874873843 where x=1");
		tab.onQueryChange(query);

		verify(mockPlaceChanger).goTo(placeCaptor.capture());
		Synapse place = (Synapse) placeCaptor.getValue();
		assertEquals(EntityArea.DATASETS, place.getArea());
		assertTrue(place.getAreaToken().isEmpty());
		assertNull(place.getVersionNumber());
		assertEquals("syn837874873843", place.getEntityId());
	}

	@Test
	public void testSetTableQueryWithToken() {
		Long version = null;
		query.setOffset(1L);
		when(mockTableEntityWidget.getDefaultQuery()).thenReturn(new Query());
		String startToken = "start token";
		// Start with a token.
		String encodedToken = "encoded token";
		when(mockQueryTokenProvider.queryToToken(any(Query.class))).thenReturn(encodedToken);
		tab.setProject(projectEntityId, mockProjectEntityBundle, null);
		tab.configure(mockDatasetBundle, version, TablesTab.TABLE_QUERY_PREFIX + startToken);

		reset(mockTab);
		when(mockTab.isTabPaneVisible()).thenReturn(true);
		tab.onQueryChange(query);

		Synapse place = getNewPlace(datasetName);
		assertEquals(EntityArea.DATASETS, place.getArea());
		assertTrue(place.getAreaToken().contains(encodedToken));
	}


	@Test
	public void testGetTableQuery() {
		Long version = null;
		tab.setProject(projectEntityId, mockProjectEntityBundle, null);

		String queryAreaToken;
		Query query1 = null;
		queryAreaToken = null;
		tab.configure(mockDatasetBundle, version, queryAreaToken);
		query1 = tab.getQueryString();
		assertNull(query1);

		queryAreaToken = "something else";
		tab.configure(mockDatasetBundle, version, queryAreaToken);
		query1 = tab.getQueryString();
		assertNull(query1);
		String token = "encoded query token";
		queryAreaToken = "query/" + token;
		when(mockQueryTokenProvider.tokenToQuery(anyString())).thenReturn(query);
		tab.configure(mockDatasetBundle, version, queryAreaToken);
		query1 = tab.getQueryString();
		assertEquals(query, query1);
		query.setSql("SELECT 'query/' FROM syn123 LIMIT 1");
		token = "encoded query token 2";
		queryAreaToken = "query/" + token;
		tab.configure(mockDatasetBundle, version, queryAreaToken);
		query1 = tab.getQueryString();
		assertEquals(query, query1);
	}

	@Test
	public void testResetView() {
		tab.resetView();
		verify(mockSynapseAlert).clear();
		verify(mockView).setEntityMetadataVisible(false);
		verify(mockView).setBreadcrumbVisible(false);
		verify(mockView).setTableListVisible(false);
		verify(mockView).setTitlebarVisible(false);
		verify(mockView).clearTableEntityWidget();
		verify(mockModifiedCreatedBy).setVisible(false);
		verify(mockView).setTableUIVisible(false);
	}

	@Test
	public void testShowProjectLoadError() {
		Exception projectLoadError = new Exception("error loading project");
		tab.setProject(projectEntityId, null, projectLoadError);
		tab.showProjectLevelUI();
		Synapse expectedPlace = new Synapse(projectEntityId, null, EntityArea.DATASETS, null);
		verify(mockTab).setEntityNameAndPlace(projectEntityId, expectedPlace);
		verify(mockSynapseAlert).handleException(projectLoadError);
	}
}
