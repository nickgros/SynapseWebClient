package org.sagebionetworks.web.client.widget.entity;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.sagebionetworks.web.client.GlobalApplicationState;
import org.sagebionetworks.web.client.PlaceChanger;
import org.sagebionetworks.web.client.jsinterop.SqlDefinedTableEditorModalProps;

@RunWith(MockitoJUnitRunner.Silent.class)
public class SqlDefinedEditorModalWidgetTest {

  @Mock
  SqlDefinedEditorModalWidgetView mockView;

  @Mock
  SqlDefinedTableEditorModalProps.Callback mockOnUpdate;

  @Mock
  SqlDefinedTableEditorModalProps.Callback mockOnCancel;

  @Mock
  GlobalApplicationState mockGlobalAppState;

  @Mock
  PlaceChanger mockPlaceChanger;

  @Captor
  ArgumentCaptor<SqlDefinedTableEditorModalProps> propsCaptor;

  SqlDefinedEditorModalWidget widget;

  public static final String ENTITY_ID = "syn42";

  @Before
  public void before() {
    widget = new SqlDefinedEditorModalWidget(mockView, mockGlobalAppState);
    when(mockGlobalAppState.getPlaceChanger()).thenReturn(mockPlaceChanger);
  }

  @After
  public void validate() {
    validateMockitoUsage();
  }

  @Test
  public void testConstruction() {
    widget.configure(ENTITY_ID, mockOnUpdate, mockOnCancel);
    verify(mockView).renderComponent(propsCaptor.capture());
    SqlDefinedTableEditorModalProps capturedProps = propsCaptor.getValue();

    assertEquals(ENTITY_ID, capturedProps.entityId);
    assertEquals(mockOnUpdate, capturedProps.onUpdate);
    assertEquals(mockOnCancel, capturedProps.onCancel);
  }

  @Test
  public void testSetOpen() {
    widget.setOpen(true);
    verify(mockGlobalAppState).setIsEditing(true);
    verify(mockView).renderComponent(propsCaptor.capture());
    assertTrue(propsCaptor.getValue().open);
    widget.setOpen(false);
    verify(mockGlobalAppState).setIsEditing(false);
    verify(mockView, times(2)).renderComponent(propsCaptor.capture());
    assertFalse(propsCaptor.getValue().open);
  }

  @Test
  public void testAsWidget() {
    widget.asWidget();
    verify(mockView).asWidget();
  }
}
