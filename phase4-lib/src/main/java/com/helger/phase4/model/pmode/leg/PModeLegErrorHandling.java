/*
 * Copyright (C) 2015-2024 Philip Helger (www.helger.com)
 * philip[at]helger[dot]com
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.helger.phase4.model.pmode.leg;

import java.io.Serializable;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.concurrent.NotThreadSafe;

import com.helger.commons.ValueEnforcer;
import com.helger.commons.annotation.MustImplementEqualsAndHashcode;
import com.helger.commons.equals.EqualsHelper;
import com.helger.commons.hashcode.HashCodeGenerator;
import com.helger.commons.state.EChange;
import com.helger.commons.state.ETriState;
import com.helger.commons.string.ToStringGenerator;

/**
 * Error handling - This P-Mode group concerns errors generated by the reception
 * of the message (for either a User message or a Signal message, unless
 * indicated otherwise) sent over leg 1 of the MEP.
 *
 * @author Philip Helger
 */
@NotThreadSafe
@MustImplementEqualsAndHashcode
public class PModeLegErrorHandling implements Serializable
{
  public static final boolean DEFAULT_REPORT_AS_RESPONSE = false;
  public static final boolean DEFAULT_REPORT_PROCESS_ERROR_NOTIFY_CONSUMER = false;
  public static final boolean DEFAULT_REPORT_PROCESS_ERROR_NOTIFY_PRDOUCER = false;
  public static final boolean DEFAULT_REPORT_DELIVERY_FAILURES_NOTIFY_PRODUCER = false;

  /**
   * This parameter indicates the address, or comma-separated list of addresses,
   * to which to send ebMS errors generated by the MSH that was trying to send
   * the message in error.
   */
  private PModeAddressList m_aReportSenderErrorsTo;

  /**
   * This parameter indicates the address, or comma-separated list of addresses,
   * to which to send ebMS errors generated by the MSH that receives the message
   * in error; e.g. this may be the address of the MSH sending the message in
   * error.
   */
  private PModeAddressList m_aReportReceiverErrorsTo;

  /**
   * This Boolean parameter indicates whether (if "true") errors generated from
   * receiving a message in error are sent over the back-channel of the
   * underlying protocol associated with the message in error, or not.
   */
  private ETriState m_eReportAsResponse = ETriState.UNDEFINED;

  /**
   * This Boolean parameter indicates whether (if "true") the Consumer
   * (application/party) of a User Message matching this P-Mode should be
   * notified when an error occurs in the Receiving MSH, during processing of
   * the received User message.
   */
  private ETriState m_eReportProcessErrorNotifyConsumer = ETriState.UNDEFINED;

  /**
   * This Boolean parameter indicates whether (if "true") the Producer
   * (application/party) of a User Message matching this P-Mode should be
   * notified when an error occurs in the Sending MSH, during processing of the
   * User Message to be sent.
   */
  private ETriState m_eReportProcessErrorNotifyProducer = ETriState.UNDEFINED;

  /**
   * This Boolean parameter indicates whether (if "true") the Producer
   * (application/party) of a User Message matching this P-Mode must always be
   * notified when the delivery to Consumer failed, or whether (if "false"), in
   * some cases, it is sufficient to notify the Consumer only
   * (Report.ProcessErrorNotifyConsumer="true"). This assumes that
   * Reliability.AtLeastOnce.Contract is "true". This also assumes that the
   * Sending MSH implementation has the ability to determine or to be made aware
   * of all cases of non-delivery that occur after the message has been received
   * by the Receiving MSH.
   */
  private ETriState m_eReportDeliveryFailuresNotifyProducer = ETriState.UNDEFINED;

  public PModeLegErrorHandling ()
  {}

  public PModeLegErrorHandling (@Nullable final PModeAddressList aReportSenderErrorsTo,
                                @Nullable final PModeAddressList aReportReceiverErrorsTo,
                                @Nonnull final ETriState eReportAsResponse,
                                @Nonnull final ETriState eReportProcessErrorNotifyConsumer,
                                @Nonnull final ETriState eReportProcessErrorNotifyProducer,
                                @Nonnull final ETriState eReportDeliveryFailuresNotifyProducer)
  {
    setReportSenderErrorsTo (aReportSenderErrorsTo);
    setReportReceiverErrorsTo (aReportReceiverErrorsTo);
    setReportAsResponse (eReportAsResponse);
    setReportProcessErrorNotifyConsumer (eReportProcessErrorNotifyConsumer);
    setReportProcessErrorNotifyProducer (eReportProcessErrorNotifyProducer);
    setReportDeliveryFailuresNotifyProducer (eReportDeliveryFailuresNotifyProducer);
  }

  @Nullable
  public final PModeAddressList getReportSenderErrorsTo ()
  {
    return m_aReportSenderErrorsTo;
  }

  public final boolean hasReportSenderErrorsTo ()
  {
    return m_aReportSenderErrorsTo != null;
  }

  @Nonnull
  public final EChange setReportSenderErrorsTo (@Nullable final PModeAddressList aReportSenderErrorsTo)
  {
    if (EqualsHelper.equals (m_aReportSenderErrorsTo, aReportSenderErrorsTo))
      return EChange.UNCHANGED;
    m_aReportSenderErrorsTo = aReportSenderErrorsTo;
    return EChange.CHANGED;
  }

  @Nullable
  public final PModeAddressList getReportReceiverErrorsTo ()
  {
    return m_aReportReceiverErrorsTo;
  }

  public final boolean hasReportReceiverErrorsTo ()
  {
    return m_aReportReceiverErrorsTo != null;
  }

  @Nonnull
  public final EChange setReportReceiverErrorsTo (@Nullable final PModeAddressList aReportReceiverErrorsTo)
  {
    if (EqualsHelper.equals (m_aReportReceiverErrorsTo, aReportReceiverErrorsTo))
      return EChange.UNCHANGED;
    m_aReportReceiverErrorsTo = aReportReceiverErrorsTo;
    return EChange.CHANGED;
  }

  public final boolean isReportAsResponseDefined ()
  {
    return m_eReportAsResponse.isDefined ();
  }

  public final boolean isReportAsResponse ()
  {
    return m_eReportAsResponse.getAsBooleanValue (DEFAULT_REPORT_AS_RESPONSE);
  }

  @Nonnull
  public final EChange setReportAsResponse (final boolean eReportAsResponse)
  {
    return setReportAsResponse (ETriState.valueOf (eReportAsResponse));
  }

  @Nonnull
  public final EChange setReportAsResponse (@Nonnull final ETriState eReportAsResponse)
  {
    ValueEnforcer.notNull (eReportAsResponse, "ReportAsResponse");
    if (eReportAsResponse.equals (m_eReportAsResponse))
      return EChange.UNCHANGED;
    m_eReportAsResponse = eReportAsResponse;
    return EChange.CHANGED;
  }

  public final boolean isReportProcessErrorNotifyConsumerDefined ()
  {
    return m_eReportProcessErrorNotifyConsumer.isDefined ();
  }

  public final boolean isReportProcessErrorNotifyConsumer ()
  {
    return m_eReportProcessErrorNotifyConsumer.getAsBooleanValue (DEFAULT_REPORT_PROCESS_ERROR_NOTIFY_CONSUMER);
  }

  @Nonnull
  public final EChange setReportProcessErrorNotifyConsumer (final boolean eReportProcessErrorNotifyConsumer)
  {
    return setReportProcessErrorNotifyConsumer (ETriState.valueOf (eReportProcessErrorNotifyConsumer));
  }

  @Nonnull
  public final EChange setReportProcessErrorNotifyConsumer (@Nonnull final ETriState eReportProcessErrorNotifyConsumer)
  {
    ValueEnforcer.notNull (eReportProcessErrorNotifyConsumer, "ReportProcessErrorNotifyConsumer");
    if (eReportProcessErrorNotifyConsumer.equals (m_eReportProcessErrorNotifyConsumer))
      return EChange.UNCHANGED;
    m_eReportProcessErrorNotifyConsumer = eReportProcessErrorNotifyConsumer;
    return EChange.CHANGED;
  }

  public final boolean isReportProcessErrorNotifyProducerDefined ()
  {
    return m_eReportProcessErrorNotifyProducer.isDefined ();
  }

  public final boolean isReportProcessErrorNotifyProducer ()
  {
    return m_eReportProcessErrorNotifyProducer.getAsBooleanValue (DEFAULT_REPORT_PROCESS_ERROR_NOTIFY_PRDOUCER);
  }

  @Nonnull
  public final EChange setReportProcessErrorNotifyProducer (final boolean eReportProcessErrorNotifyProducer)
  {
    return setReportProcessErrorNotifyProducer (ETriState.valueOf (eReportProcessErrorNotifyProducer));
  }

  @Nonnull
  public final EChange setReportProcessErrorNotifyProducer (@Nonnull final ETriState eReportProcessErrorNotifyProducer)
  {
    ValueEnforcer.notNull (eReportProcessErrorNotifyProducer, "ReportProcessErrorNotifyProducer");
    if (eReportProcessErrorNotifyProducer.equals (m_eReportProcessErrorNotifyProducer))
      return EChange.UNCHANGED;
    m_eReportProcessErrorNotifyProducer = eReportProcessErrorNotifyProducer;
    return EChange.CHANGED;
  }

  public final boolean isReportDeliveryFailuresNotifyProducerDefined ()
  {
    return m_eReportDeliveryFailuresNotifyProducer.isDefined ();
  }

  public final boolean isReportDeliveryFailuresNotifyProducer ()
  {
    return m_eReportDeliveryFailuresNotifyProducer.getAsBooleanValue (DEFAULT_REPORT_DELIVERY_FAILURES_NOTIFY_PRODUCER);
  }

  @Nonnull
  public final EChange setReportDeliveryFailuresNotifyProducer (final boolean eReportDeliveryFailuresNotifyProducer)
  {
    return setReportDeliveryFailuresNotifyProducer (ETriState.valueOf (eReportDeliveryFailuresNotifyProducer));
  }

  @Nonnull
  public final EChange setReportDeliveryFailuresNotifyProducer (@Nonnull final ETriState eReportDeliveryFailuresNotifyProducer)
  {
    ValueEnforcer.notNull (eReportDeliveryFailuresNotifyProducer, "ReportDeliveryFailuresNotifyProducer");
    if (eReportDeliveryFailuresNotifyProducer.equals (m_eReportDeliveryFailuresNotifyProducer))
      return EChange.UNCHANGED;
    m_eReportDeliveryFailuresNotifyProducer = eReportDeliveryFailuresNotifyProducer;
    return EChange.CHANGED;
  }

  @Override
  public boolean equals (final Object o)
  {
    if (o == this)
      return true;
    if (o == null || !getClass ().equals (o.getClass ()))
      return false;
    final PModeLegErrorHandling rhs = (PModeLegErrorHandling) o;
    return EqualsHelper.equals (m_aReportSenderErrorsTo, rhs.m_aReportSenderErrorsTo) &&
           EqualsHelper.equals (m_aReportReceiverErrorsTo, rhs.m_aReportReceiverErrorsTo) &&
           m_eReportAsResponse.equals (rhs.m_eReportAsResponse) &&
           m_eReportProcessErrorNotifyConsumer.equals (rhs.m_eReportProcessErrorNotifyConsumer) &&
           m_eReportProcessErrorNotifyProducer.equals (rhs.m_eReportProcessErrorNotifyProducer) &&
           m_eReportDeliveryFailuresNotifyProducer.equals (rhs.m_eReportDeliveryFailuresNotifyProducer);
  }

  @Override
  public int hashCode ()
  {
    return new HashCodeGenerator (this).append (m_aReportSenderErrorsTo)
                                       .append (m_aReportReceiverErrorsTo)
                                       .append (m_eReportAsResponse)
                                       .append (m_eReportProcessErrorNotifyConsumer)
                                       .append (m_eReportProcessErrorNotifyProducer)
                                       .append (m_eReportDeliveryFailuresNotifyProducer)
                                       .getHashCode ();
  }

  @Override
  public String toString ()
  {
    return new ToStringGenerator (this).append ("ReportSenderErrorsTo", m_aReportSenderErrorsTo)
                                       .append ("ReportReceiverErrorsTo", m_aReportReceiverErrorsTo)
                                       .append ("ReportAsResponse", m_eReportAsResponse)
                                       .append ("ReportProcessErrorNotifyConsumer", m_eReportProcessErrorNotifyConsumer)
                                       .append ("ReportProcessErrorNotifyProducer", m_eReportProcessErrorNotifyProducer)
                                       .append ("ReportDeliveryFailuresNotifyProducer",
                                                m_eReportDeliveryFailuresNotifyProducer)
                                       .getToString ();
  }

  /**
   * @return A new {@link PModeLegErrorHandling} that is totally undefined.
   *         Never <code>null</code>.
   */
  @Nonnull
  public static PModeLegErrorHandling createUndefined ()
  {
    return new PModeLegErrorHandling (null,
                                      null,
                                      ETriState.UNDEFINED,
                                      ETriState.UNDEFINED,
                                      ETriState.UNDEFINED,
                                      ETriState.UNDEFINED);
  }
}
