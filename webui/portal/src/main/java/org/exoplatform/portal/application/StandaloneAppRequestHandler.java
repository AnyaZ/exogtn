/**
 * Copyright (C) 2009 eXo Platform SAS.
 * 
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 * 
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */

package org.exoplatform.portal.application;

import javax.servlet.ServletConfig;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.exoplatform.container.xml.InitParams;
import org.exoplatform.container.xml.ValueParam;
import org.exoplatform.web.WebAppController;


public class StandaloneAppRequestHandler extends PortalRequestHandler
{
   
   private String webuiConfigPath;

   public StandaloneAppRequestHandler(InitParams params)
   {
      ValueParam valueParam = params.getValueParam("webui.configuration");
      if (valueParam != null)
      {
         webuiConfigPath = valueParam.getValue();
      }
   }

   private String[] PATHS = {"/standalone"};

   public String[] getPath()
   {
      return PATHS;
   }

   @Override
   public void onInit(WebAppController controller, ServletConfig sConfig) throws Exception
   {
      StandaloneApplication standaloneApplication = new StandaloneApplication(sConfig);
      standaloneApplication.setWebUIConfigPath(webuiConfigPath);
      standaloneApplication.onInit();
      controller.addApplication(standaloneApplication);
   }
   
   public void execute(WebAppController controller, HttpServletRequest req, HttpServletResponse res) throws Exception
   {
      log.debug("Session ID = " + req.getSession().getId());
      res.setHeader("Cache-Control", "no-cache");

      StandaloneApplication app = controller.getApplication(StandaloneApplication.STANDALONE_APPLICATION_ID);
      StandaloneAppRequestContext context = new StandaloneAppRequestContext(app, req, res);

      processRequest(context, app);
   }     
}
