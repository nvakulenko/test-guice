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

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotEquals;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Scopes;
import com.google.inject.name.Names;
import com.mycila.inject.jsr250.Jsr250;
import com.mycila.inject.jsr250.Jsr250Injector;
import com.xemantic.tadedon.guice.lifecycle.LifecycleManager;
import com.xemantic.tadedon.guice.lifecycle.jsr250.Jsr250LifecycleModule;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.Properties;

public class TestGuice
{
   private Injector injector;

   @BeforeMethod
   public void setUp()
   {
      injector = Guice.createInjector(new TestModule(), new Jsr250LifecycleModule());
   }

   @Test
   public void testFirstContainer()
   {
      FirstContainer containerInstance = injector.getInstance(FirstContainer.class);
      assertNotNull(containerInstance);
   }

   @Test
   public void testSecondContainer()
   {
      SecondContainer containerInstance = injector.getInstance(SecondContainer.class);
      assertNotNull(containerInstance);
   }

   @Test
   public void testNonSingletonResource()
   {
      FirstContainer fstContainerInstance = injector.getInstance(FirstContainer.class);
      SecondContainer sndContainerInstance = injector.getInstance(SecondContainer.class);

      assertNotEquals(fstContainerInstance.getSecondResource(), sndContainerInstance.getSecondResource());
   }

   @Test
   public void testSingletonResource()
   {
      FirstContainer fstContainerInstance = injector.getInstance(FirstContainer.class);
      SecondContainer sndContainerInstance = injector.getInstance(SecondContainer.class);

      assertEquals(fstContainerInstance.getFirstResource(), sndContainerInstance.getFirstResource());
   }

   @Test
   public void testSingletonContainer()
   {
      FirstContainer containerInstance1 = injector.getInstance(FirstContainer.class);
      FirstContainer containerInstance2 = injector.getInstance(FirstContainer.class);

      assertEquals(containerInstance1, containerInstance2);
   }

   @Test
   public void testNonSingletonContainer()
   {
      SecondContainer containerInstance1 = injector.getInstance(SecondContainer.class);
      SecondContainer containerInstance2 = injector.getInstance(SecondContainer.class);

      assertNotEquals(containerInstance1, containerInstance2);
   }

   @Test
   public void testPostConstruct()
   {
      LifecycleManager lifecycleManager = injector.getInstance(LifecycleManager.class);
      LifecycleContainer containerInstance = injector.getInstance(LifecycleContainer.class);

      lifecycleManager.initialize();

      assertTrue(containerInstance.isInit());
   }

   @Test
   public void testPreDestroy()
   {
      LifecycleManager lifecycleManager = injector.getInstance(LifecycleManager.class);
      LifecycleContainer containerInstance = injector.getInstance(LifecycleContainer.class);

      lifecycleManager.initialize();
      lifecycleManager.destroy();

      assertTrue(containerInstance.isDestroy());
   }

   @Test
   public void testMilinPostConstruct()
   {
      Injector injector = Jsr250.createInjector(new AbstractModule()
      {
         @Override
         protected void configure()
         {
            bind(LifecycleContainer.class);
         }
      });

      LifecycleContainer containerInstance = injector.getInstance(LifecycleContainer.class);

      assertTrue(containerInstance.isInit());
   }

   @Test
   public void testMilinPreDestroy()
   {
      Jsr250Injector injector = Jsr250.createInjector(new AbstractModule()
      {
         @Override
         protected void configure()
         {
            bind(LifecycleContainer.class).in(Scopes.SINGLETON);
         }
      });

      LifecycleContainer containerInstance = injector.getInstance(LifecycleContainer.class);
      injector.destroy();

      assertTrue(containerInstance.isDestroy());
   }

   @Test
   public void testPropertyInjection()
   {
      final Properties properties = new Properties();
      properties.put("property1", "value1");
      properties.put("property2", "value2");

      injector = Guice.createInjector(new AbstractModule()
      {
         @Override
         protected void configure()
         {
            bind(PropertyContainer.class);
            Names.bindProperties(binder(), properties);
         }
      });

      PropertyContainer containerInstance = injector.getInstance(PropertyContainer.class);
      assertEquals(containerInstance.getProperty1(), "value1");
      assertEquals(containerInstance.getProperty2(), "value2");
   }
}
