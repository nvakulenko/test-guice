/*
 * Copyright (C) 2012 eXo Platform SAS.
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
package guice;

import com.google.inject.Inject;
import com.google.inject.name.Named;

import javax.annotation.Nullable;

public class PropertyContainer
{
   private final String property1;

   private final String property2;

   @Inject
   public PropertyContainer(@Named("property1") String property1, @Nullable @Named("property2") String property2)
   {
      this.property1 = property1;
      this.property2 = property2;
   }

   /**
    * @return the property1
    */
   public String getProperty1()
   {
      return property1;
   }

   /**
    * @return the property2
    */
   public String getProperty2()
   {
      return property2;
   }

}
