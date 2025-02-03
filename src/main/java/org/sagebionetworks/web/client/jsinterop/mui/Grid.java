package org.sagebionetworks.web.client.jsinterop.mui;

import org.sagebionetworks.web.client.jsinterop.ReactComponentType;
import org.sagebionetworks.web.client.jsinterop.react.HasStyle;

public class Grid extends HasStyle<ReactComponentType<GridProps>, GridProps> {

  public Grid() {
    super(MaterialUI.Grid2, GridProps.create(false));
  }

  public void setId(String id) {
    props.id = id;
    this.render();
  }

  public void setContainer(boolean container) {
    props.container = container;
    this.render();
  }

  public void setXs(int xs) {
    props.size.xs = xs;
    this.render();
  }

  public void setSm(int sm) {
    props.size.sm = sm;
    this.render();
  }

  public void setMd(int md) {
    props.size.md = md;
    this.render();
  }

  public void setLg(int lg) {
    props.size.lg = lg;
    this.render();
  }

  public void setXl(int xl) {
    props.size.xl = xl;
    this.render();
  }

  public void setXsOffset(int xsOffset) {
    props.offset.xs = xsOffset;
    this.render();
  }

  public void setSmOffset(int smOffset) {
    props.offset.sm = smOffset;
    this.render();
  }

  public void setMdOffset(int mdOffset) {
    props.offset.md = mdOffset;
    this.render();
  }

  public void setLgOffset(int lgOffset) {
    props.offset.lg = lgOffset;
    this.render();
  }

  public void setXlOffset(int xlOffset) {
    props.offset.xl = xlOffset;
    this.render();
  }

  public void setMt(String mt) {
    props.sx.mt = mt;
    this.render();
  }

  public void setPl(String pl) {
    props.sx.pl = pl;
    this.render();
  }

  public void setRowSpacing(String rowSpacing) {
    props.rowSpacing = rowSpacing;
    this.render();
  }

  public void setColumnSpacing(String columnSpacing) {
    props.columnSpacing = columnSpacing;
    this.render();
  }
}
