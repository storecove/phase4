/*
 * Copyright (C) 2020-2025 Philip Helger (www.helger.com)
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
package com.helger.phase4.peppol.server.api;

import javax.annotation.Nonnull;

import com.helger.peppol.servicedomain.EPeppolNetwork;
import com.helger.phase4.peppol.server.APConfig;
import com.helger.photon.api.APIDescriptor;
import com.helger.photon.api.APIPath;
import com.helger.photon.api.IAPIExceptionMapper;
import com.helger.photon.api.IAPIRegistry;

/**
 * This class contains the API registration and global parameters.
 *
 * @author Philip Helger
 */
public final class Phase4API
{
  public static final String PARAM_SENDER_ID = "senderId";
  public static final String PARAM_RECEIVER_ID = "receiverId";
  public static final String PARAM_DOC_TYPE_ID = "docTypeId";
  public static final String PARAM_PROCESS_ID = "processId";
  public static final String PARAM_COUNTRY_CODE_C1 = "countryC1";

  public static final String PARAM_YEAR = "year";
  public static final String PARAM_MONTH = "month";

  private Phase4API ()
  {}

  public static void init (@Nonnull final IAPIRegistry aAPIRegistry)
  {
    final IAPIExceptionMapper aExceptionMapper = new APIExceptionMapper ();

    final EPeppolNetwork eStage = APConfig.getPeppolStage ();

    {
      final APIDescriptor aSendTest = new APIDescriptor (APIPath.post ("/sendas4/{" +
                                                                       PARAM_SENDER_ID +
                                                                       "}/{" +
                                                                       PARAM_RECEIVER_ID +
                                                                       "}/{" +
                                                                       PARAM_DOC_TYPE_ID +
                                                                       "}/{" +
                                                                       PARAM_PROCESS_ID +
                                                                       "}/{" +
                                                                       PARAM_COUNTRY_CODE_C1 +
                                                                       "}"), new APIPostSendDocument (eStage));
      aSendTest.setExceptionMapper (aExceptionMapper);
      aAPIRegistry.registerAPI (aSendTest);
    }

    {
      final APIDescriptor aSendTestSbdh = new APIDescriptor (APIPath.post ("/sendsbdh"), new APIPostSendSBDH (eStage));
      aSendTestSbdh.setExceptionMapper (aExceptionMapper);
      aAPIRegistry.registerAPI (aSendTestSbdh);
    }

    {
      final APIDescriptor aCreateTSR = new APIDescriptor (APIPath.get ("/create-tsr/{" +
                                                                       PARAM_YEAR +
                                                                       "}/{" +
                                                                       PARAM_MONTH +
                                                                       "}"), new APIGetCreateTSR ());
      aCreateTSR.setExceptionMapper (aExceptionMapper);
      aAPIRegistry.registerAPI (aCreateTSR);
    }

    {
      final APIDescriptor aCreateEUSR = new APIDescriptor (APIPath.get ("/create-eusr/{" +
                                                                        PARAM_YEAR +
                                                                        "}/{" +
                                                                        PARAM_MONTH +
                                                                        "}"), new APIGetCreateEUSR ());
      aCreateEUSR.setExceptionMapper (aExceptionMapper);
      aAPIRegistry.registerAPI (aCreateEUSR);
    }
  }
}
