package org.sagebionetworks.web.unitclient.widget.table.v2.results;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.stubbing.Answer;
import org.sagebionetworks.repo.model.table.ColumnModel;
import org.sagebionetworks.repo.model.table.ColumnType;
import org.sagebionetworks.repo.model.table.FacetColumnRequest;
import org.sagebionetworks.repo.model.table.Query;
import org.sagebionetworks.repo.model.table.QueryResult;
import org.sagebionetworks.repo.model.table.QueryResultBundle;
import org.sagebionetworks.repo.model.table.Row;
import org.sagebionetworks.repo.model.table.RowSet;
import org.sagebionetworks.repo.model.table.SelectColumn;
import org.sagebionetworks.web.client.DateTimeUtils;
import org.sagebionetworks.web.client.PortalGinInjector;
import org.sagebionetworks.web.client.utils.CallbackP;
import org.sagebionetworks.web.client.widget.pagination.BasicPaginationWidget;
import org.sagebionetworks.web.client.widget.pagination.PageChangeListener;
import org.sagebionetworks.web.client.widget.table.modal.fileview.TableType;
import org.sagebionetworks.web.client.widget.table.modal.fileview.ViewDefaultColumns;
import org.sagebionetworks.web.client.widget.table.v2.results.PagingAndSortingListener;
import org.sagebionetworks.web.client.widget.table.v2.results.RowSelectionListener;
import org.sagebionetworks.web.client.widget.table.v2.results.RowWidget;
import org.sagebionetworks.web.client.widget.table.v2.results.SortableTableHeader;
import org.sagebionetworks.web.client.widget.table.v2.results.StaticTableHeader;
import org.sagebionetworks.web.client.widget.table.v2.results.TablePageView;
import org.sagebionetworks.web.client.widget.table.v2.results.TablePageWidget;
import org.sagebionetworks.web.client.widget.table.v2.results.cell.Cell;
import org.sagebionetworks.web.client.widget.table.v2.results.cell.CellFactory;
import org.sagebionetworks.web.unitclient.widget.table.v2.TableModelTestUtils;

/**
 * Business logic unit tests for the TablePageWidget.
 *
 */
@RunWith(MockitoJUnitRunner.Silent.class)
public class TablePageWidgetTest {

  @Mock
  CellFactory mockCellFactory;

  @Mock
  TablePageView mockView;

  @Mock
  PortalGinInjector mockGinInjector;

  @Mock
  RowSelectionListener mockListner;

  @Mock
  PagingAndSortingListener mockPageChangeListner;

  @Mock
  BasicPaginationWidget mockPaginationWidget;

  TablePageWidget widget;
  List<ColumnModel> schema;
  SelectColumn derivedColumn;
  List<SortableTableHeader> sortHeaders;
  List<StaticTableHeader> staticHeader;
  List<SelectColumn> headers;
  QueryResultBundle bundle;
  List<Row> rows;
  Query query;
  List<CellStub> cellStubs;
  TableType tableType;

  @Mock
  CallbackP<FacetColumnRequest> mockFacetChangedHandler;

  @Mock
  ViewDefaultColumns mockFileViewDefaultColumns;

  @Mock
  ColumnModel mockColumnModel;

  List<ColumnModel> defaultColumnModels;
  public static final String ENTITY_ID = "syn123";
  public static final String FRIENDLY_DATE_STRING = "01/23/2020 11:50 AM";

  @Mock
  DateTimeUtils mockDateTimeUtils;

  @Mock
  Date mockLastUpdatedDate;

  @Before
  public void before() {
    cellStubs = new LinkedList<CellStub>();

    // Use stubs for all cells.
    Answer<Cell> cellAnswer = new Answer<Cell>() {
      @Override
      public Cell answer(InvocationOnMock invocation) throws Throwable {
        CellStub stub = new CellStub();
        cellStubs.add(stub);
        return stub;
      }
    };
    when(mockCellFactory.createEditor(any(ColumnModel.class)))
      .thenAnswer(cellAnswer);
    when(mockCellFactory.createRenderer(any(ColumnModel.class)))
      .thenAnswer(cellAnswer);
    when(mockGinInjector.createRowWidget())
      .thenAnswer(
        new Answer<RowWidget>() {
          @Override
          public RowWidget answer(InvocationOnMock invocation)
            throws Throwable {
            return new RowWidget(
              new RowViewStub(),
              mockCellFactory,
              mockFileViewDefaultColumns
            );
          }
        }
      );
    when(mockGinInjector.getDateTimeUtils()).thenReturn(mockDateTimeUtils);
    sortHeaders = new LinkedList<SortableTableHeader>();
    when(mockGinInjector.createSortableTableHeader())
      .thenAnswer(
        new Answer<SortableTableHeader>() {
          @Override
          public SortableTableHeader answer(InvocationOnMock invocation)
            throws Throwable {
            SortableTableHeader header = Mockito.mock(
              SortableTableHeader.class
            );
            sortHeaders.add(header);
            return header;
          }
        }
      );
    staticHeader = new LinkedList<StaticTableHeader>();
    when(mockGinInjector.createStaticTableHeader())
      .thenAnswer(
        new Answer<StaticTableHeader>() {
          @Override
          public StaticTableHeader answer(InvocationOnMock invocation)
            throws Throwable {
            StaticTableHeader header = Mockito.mock(StaticTableHeader.class);
            staticHeader.add(header);
            return header;
          }
        }
      );
    defaultColumnModels = new ArrayList<ColumnModel>();
    when(mockFileViewDefaultColumns.getDefaultViewColumns(any(TableType.class)))
      .thenReturn(defaultColumnModels);
    when(mockFileViewDefaultColumns.deepColumnModel(any(ColumnModel.class)))
      .thenReturn(mockColumnModel);
    widget =
      new TablePageWidget(mockView, mockGinInjector, mockPaginationWidget);

    schema = TableModelTestUtils.createOneOfEachType();
    headers = TableModelTestUtils.buildSelectColumns(schema);
    // Include an aggregate result in the headers.
    derivedColumn = new SelectColumn();
    derivedColumn.setColumnType(ColumnType.DOUBLE);
    derivedColumn.setName("sum(four)");
    headers.add(derivedColumn);
    rows = TableModelTestUtils.createRows(schema, 3);
    // Add an additional column to the rows for the aggregate results
    int i = 0;
    for (Row row : rows) {
      row.getValues().add("agg" + i);
      i++;
    }
    RowSet set = new RowSet();
    set.setHeaders(headers);
    set.setRows(rows);
    set.setTableId(ENTITY_ID);
    bundle = new QueryResultBundle();
    QueryResult qr = new QueryResult();
    qr.setQueryResults(set);
    bundle.setQueryResult(qr);
    bundle.setSelectColumns(headers);
    bundle.setQueryCount(99L);
    bundle.setColumnModels(schema);
    bundle.setLastUpdatedOn(mockLastUpdatedDate);
    when(mockDateTimeUtils.getDateTimeString(any(Date.class)))
      .thenReturn(FRIENDLY_DATE_STRING);

    query = new Query();
    query.setLimit(100L);
    query.setOffset(0L);
    query.setSql("select * from " + ENTITY_ID);
    tableType = TableType.table;
  }

  @Test
  public void testConfigureRoundTrip() {
    widget.configure(
      bundle,
      query,
      null,
      tableType,
      null,
      mockPageChangeListner,
      mockFacetChangedHandler
    );
    List<Row> extracted = widget.extractRowSet();
    assertEquals(rows, extracted);
    List<ColumnModel> headers = widget.extractHeaders();
    List<ColumnModel> expected = new LinkedList<ColumnModel>(schema);
    // there should be a derived column at the end for the for the aggregate function.
    ColumnModel derived = new ColumnModel();
    derived.setColumnType(derivedColumn.getColumnType());
    derived.setName(derivedColumn.getName());
    expected.add(derived);
    assertEquals(expected, headers);
    // last updated date from bundle transformed and rendered?
    verify(mockView)
      .setLastUpdatedOn(TablePageWidget.LAST_UPDATED_ON + FRIENDLY_DATE_STRING);
  }

  @Test
  public void testConfigureWithPaging() {
    widget.configure(
      bundle,
      query,
      null,
      tableType,
      null,
      mockPageChangeListner,
      mockFacetChangedHandler
    );
    // Pagination should be setup since a page change listener was provided.
    long rowCount = rows.size();
    verify(mockPaginationWidget)
      .configure(
        query.getLimit(),
        query.getOffset(),
        rowCount,
        mockPageChangeListner
      );
    verify(mockView).setPaginationWidgetVisible(true);
  }

  @Test
  public void testConfigureEditable() {
    // Static headers should be used for edits
    assertTrue(staticHeader.isEmpty());
    widget.configure(
      bundle,
      query,
      null,
      tableType,
      null,
      mockPageChangeListner,
      mockFacetChangedHandler
    );
    long rowCount = rows.size();
    verify(mockPaginationWidget)
      .configure(
        query.getLimit(),
        query.getOffset(),
        rowCount,
        mockPageChangeListner
      );
    assertEquals(bundle.getColumnModels().size() + 1, staticHeader.size());
  }

  @Test
  public void testConfigureNoPaging() {
    widget.configure(
      bundle,
      null,
      null,
      tableType,
      null,
      null,
      mockFacetChangedHandler
    );
    verify(mockPaginationWidget, never())
      .configure(
        anyLong(),
        anyLong(),
        anyLong(),
        any(PageChangeListener.class)
      );
    verify(mockView).setPaginationWidgetVisible(false);
  }

  @Test
  public void testOnAddNewRow() {
    widget.configure(
      bundle,
      query,
      null,
      tableType,
      null,
      mockPageChangeListner,
      mockFacetChangedHandler
    );
    widget.onAddNewRow();
    widget.onAddNewRow();
    widget.onAddNewRow();
    List<Row> extracted = widget.extractRowSet();
    assertEquals(rows.size() + 3, extracted.size());
  }

  @Test
  public void testSelectAllAndDeleteSelected() {
    widget.configure(
      bundle,
      null,
      null,
      tableType,
      mockListner,
      null,
      mockFacetChangedHandler
    );
    widget.onSelectAll();
    // The handler should be called once
    verify(mockListner).onSelectionChanged();
    assertTrue(widget.isOneRowOrMoreRowsSelected());
    reset(mockListner);
    widget.onDeleteSelected();
    // The handler should be called once
    verify(mockListner).onSelectionChanged();
    assertFalse(widget.isOneRowOrMoreRowsSelected());
    List<Row> extracted = widget.extractRowSet();
    assertTrue(extracted.isEmpty());
  }

  @Test
  public void testSelectNone() {
    widget.configure(
      bundle,
      null,
      null,
      tableType,
      mockListner,
      null,
      mockFacetChangedHandler
    );
    widget.onSelectAll();
    // The handler should be called once
    verify(mockListner).onSelectionChanged();
    assertTrue(widget.isOneRowOrMoreRowsSelected());
    reset(mockListner);
    widget.onSelectNone();
    // The handler should be called once
    verify(mockListner).onSelectionChanged();
    assertFalse(widget.isOneRowOrMoreRowsSelected());
  }

  @Test
  public void testToggleSelect() {
    widget.configure(
      bundle,
      null,
      null,
      tableType,
      mockListner,
      null,
      mockFacetChangedHandler
    );
    widget.onSelectNone();
    // The handler should be called once
    verify(mockListner).onSelectionChanged();
    assertFalse(widget.isOneRowOrMoreRowsSelected());
    reset(mockListner);
    widget.onToggleSelect();
    // The handler should be called once
    verify(mockListner).onSelectionChanged();
    assertTrue(widget.isOneRowOrMoreRowsSelected());
    reset(mockListner);
    widget.onToggleSelect();
    // The handler should be called once
    verify(mockListner).onSelectionChanged();
    assertFalse(widget.isOneRowOrMoreRowsSelected());
  }

  @Test
  public void testIsValid() {
    widget.configure(
      bundle,
      null,
      null,
      tableType,
      mockListner,
      null,
      mockFacetChangedHandler
    );
    assertTrue(widget.isValid());
    // Set on cell to be invalid
    cellStubs.get(3).setIsValid(false);
    assertFalse(widget.isValid());
  }
}
