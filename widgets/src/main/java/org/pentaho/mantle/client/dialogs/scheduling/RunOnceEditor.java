/*!
 * This program is free software; you can redistribute it and/or modify it under the
 * terms of the GNU Lesser General Public License, version 2.1 as published by the Free Software
 * Foundation.
 *
 * You should have received a copy of the GNU Lesser General Public License along with this
 * program; if not, you can obtain a copy at http://www.gnu.org/licenses/old-licenses/lgpl-2.1.html
 * or from the Free Software Foundation, Inc.,
 * 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU Lesser General Public License for more details.
 *
 * Copyright (c) 2002-2023 Hitachi Vantara..  All rights reserved.
 */

package org.pentaho.mantle.client.dialogs.scheduling;

import org.pentaho.gwt.widgets.client.controls.DatePickerEx;
import org.pentaho.gwt.widgets.client.controls.ErrorLabel;
import org.pentaho.gwt.widgets.client.controls.TimePicker;
import org.pentaho.gwt.widgets.client.dialogs.MessageDialogBox;
import org.pentaho.gwt.widgets.client.panel.VerticalFlexPanel;
import org.pentaho.gwt.widgets.client.ui.ICallback;
import org.pentaho.gwt.widgets.client.ui.IChangeHandler;
import org.pentaho.gwt.widgets.client.utils.TimeUtil;
import org.pentaho.mantle.client.messages.Messages;

import java.util.Date;

import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.i18n.client.DateTimeFormat.PredefinedFormat;
import com.google.gwt.user.client.ui.CaptionPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.datepicker.client.DateBox.DefaultFormat;

/**
 * @author Steven Barkdull
 */

public class RunOnceEditor extends VerticalPanel implements IChangeHandler {

  private static final String SCHEDULER_CAPTION_PANEL = "schedule-editor-caption-panel"; //$NON-NLS-1$
  private static final String SCHEDULE_START_DATE_INPUT = "schedule-start-date-input";

  protected TimePicker startTimePicker = null;
  private DefaultFormat format = new DefaultFormat( DateTimeFormat.getFormat( PredefinedFormat.DATE_SHORT ) );
  protected DatePickerEx startDatePicker = new DatePickerEx( format );
  private ICallback<IChangeHandler> onChangeHandler = null;

  private ErrorLabel startLabel = null;
  private ErrorLabel detailLabel = null;

  private AdditionalDetailsPanel detailsPanel;

  private final MessageDialogBox errorBox =
      new MessageDialogBox( Messages.getString( "error" ), "", false, false, true );

  public RunOnceEditor( final TimePicker startTimePicker ) {
    setWidth( "100%" ); //$NON-NLS-1$

    VerticalFlexPanel outerVP = new VerticalFlexPanel();
    add( outerVP );
    detailsPanel = new AdditionalDetailsPanel();

    CaptionPanel startDateCaptionPanel = new CaptionPanel( Messages.getString( "schedule.startDate" ) );
    startDateCaptionPanel.setStyleName( SCHEDULER_CAPTION_PANEL );
    startDateCaptionPanel.getElement().setId( SCHEDULE_START_DATE_INPUT );
    startDateCaptionPanel.add( startDatePicker.getDatePicker() );
    startLabel = new ErrorLabel( startDateCaptionPanel );
    outerVP.add( startLabel );

    detailLabel = new ErrorLabel( detailsPanel );
    outerVP.add( detailLabel );

    this.startTimePicker = startTimePicker;
    configureOnChangeHandler();
  }

  public boolean getEnableSafeMode() {
    return detailsPanel.getEnableSafeMode();
  }

  public void setEnableSafeMode( boolean enableSafeMode ) {
    detailsPanel.setEnableSafeMode( enableSafeMode );
  }

  public boolean getGatherMetrics() {
    return detailsPanel.getGatherMetrics();
  }

  public void setGatherMetrics( boolean gatherMetrics ) {
    detailsPanel.setGatherMetrics( gatherMetrics );
  }

  public String getLogLevel() {
    return detailsPanel.getLogLevel();
  }

  public void setLogLevel( String logLevel ) {
    detailsPanel.setLogLevel( logLevel );
  }

  public Date getStartDate() {
    return startDatePicker.getSelectedDate();
  }

  public void setStartDate( Date d ) {
    startDatePicker.getDatePicker().setValue( d );
  }

  public String getStartTime() {
    return startTimePicker.getTime();
  }

  public void setStartTime( String strTime ) {
    startTimePicker.setTime( strTime );
  }

  @SuppressWarnings( "deprecation" )
  public void reset( Date d ) {
    startTimePicker.setTimeOfDay( TimeUtil.getTimeOfDayBy0To23Hour( d.getHours() ) );
    startTimePicker.setHour( TimeUtil.to12HourClock( d.getHours() ) );
    startTimePicker.setMinute( d.getMinutes() );
    startDatePicker.getDatePicker().setValue( d );
  }

  public void setOnChangeHandler( ICallback<IChangeHandler> handler ) {
    this.onChangeHandler = handler;
  }

  private void changeHandler() {
    if ( null != onChangeHandler ) {
      onChangeHandler.onHandle( this );
    }
  }

  private void configureOnChangeHandler() {
    final RunOnceEditor localThis = this;

    ICallback<IChangeHandler> handler = new ICallback<IChangeHandler>() {
      public void onHandle( IChangeHandler o ) {
        localThis.changeHandler();
      }
    };
    startTimePicker.setOnChangeHandler( handler );
    startDatePicker.setOnChangeHandler( handler );
  }
}
