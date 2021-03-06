package extension;

/*
 * Copyright 2002-2017 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */


import org.junit.jupiter.api.extension.*;
import org.junit.jupiter.api.extension.ExtensionContext.Namespace;
import org.junit.jupiter.api.extension.ExtensionContext.Store;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.test.context.TestContextManager;
import org.springframework.util.Assert;

import java.lang.reflect.Constructor;
import java.lang.reflect.Executable;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
/**
 * {@code SpringExtension} integrates the <em>Spring TestContext Framework</em>
 * into JUnit 5's <em>Jupiter</em> programming model.
 * <p>
 * <p>To use this class, simply annotate a JUnit Jupiter based test class with
 * {@code @ExtendWith(SpringExtension.class)}.
 *
 * @author Sam Brannen
 * @see org.springframework.test.context.junit.jupiter.SpringJUnitJupiterConfig
 * @see org.springframework.test.context.junit.jupiter.web.SpringJUnitJupiterWebConfig
 * @see org.springframework.test.context.TestContextManager
 * @since 5.0
 */
public class SpringExtension implements BeforeAllCallback, AfterAllCallback, TestInstancePostProcessor,
        BeforeEachCallback, AfterEachCallback, ParameterResolver {

     /**
      * {@link Namespace} in which {@code TestContextManagers} are stored,
      * keyed by test class.
      */
     private static final Namespace NAMESPACE = Namespace.create(SpringExtension.class);

     /**
      * Get the {@link ApplicationContext} associated with the supplied {@code ExtensionContext}.
      *
      * @param context the current {@code ExtensionContext} (never {@code null})
      * @return the application context
      * @throws IllegalStateException if an error occurs while retrieving the application context
      * @see org.springframework.test.context.TestContext#getApplicationContext()
      */
     public static ApplicationContext getApplicationContext(ExtensionContext context) {
          return getTestContextManager(context).getTestContext().getApplicationContext();
     }

     /**
      * Get the {@link TestContextManager} associated with the supplied {@code ExtensionContext}.
      *
      * @return the {@code TestContextManager} (never {@code null})
      */
     private static TestContextManager getTestContextManager(ExtensionContext context) {
          Assert.notNull(context, "ExtensionContext must not be null");
          Class<?> testClass = getRequiredTestClass(context);
          Store store = context.getStore(NAMESPACE);
          return store.getOrComputeIfAbsent(testClass, TestContextManager::new, TestContextManager.class);
     }

     /**
      * Get the test class associated with the supplied {@code ExtensionContext}.
      *
      * @return the test class
      * @throws IllegalStateException if the extension context does not contain
      *                               a test class
      */
     private static Class<?> getRequiredTestClass(ExtensionContext context) throws IllegalStateException {
          Assert.notNull(context, "ExtensionContext must not be null");
          return context.getTestClass().orElseThrow(
                  () -> new IllegalStateException("JUnit failed to supply the test class in the ExtensionContext"));
     }

     /**
      * Get the test instance associated with the supplied {@code ExtensionContext}.
      *
      * @return the test instance
      * @throws IllegalStateException if the extension context does not contain
      *                               a test instance
      */
     private static Object getRequiredTestInstance(ExtensionContext context) throws IllegalStateException {
          Assert.notNull(context, "ExtensionContext must not be null");
          return context.getTestInstance().orElseThrow(
                  () -> new IllegalStateException("JUnit failed to supply the test instance in the ExtensionContext"));
     }

     /**
      * Get the test method associated with the supplied {@code ExtensionContext}.
      *
      * @return the test method
      * @throws IllegalStateException if the extension context does not contain
      *                               a test method
      */
     private static Method getRequiredTestMethod(ExtensionContext context) throws IllegalStateException {
          Assert.notNull(context, "ExtensionContext must not be null");
          return context.getTestMethod().orElseThrow(
                  () -> new IllegalStateException("JUnit failed to supply the test method in the ExtensionContext"));
     }

     /**
      * Delegates to {@link TestContextManager#beforeTestClass}.
      */
     @Override
     public void beforeAll(ExtensionContext context) throws Exception {
          getTestContextManager(context).beforeTestClass();
     }

     /**
      * Delegates to {@link TestContextManager#afterTestClass}.
      */
     @Override
     public void afterAll(ExtensionContext context) throws Exception {
          try {
               getTestContextManager(context).afterTestClass();
          } finally {
               context.getStore(NAMESPACE).remove(getRequiredTestClass(context));
          }
     }

     /**
      * Delegates to {@link TestContextManager#prepareTestInstance}.
      */
     @Override
     public void postProcessTestInstance(Object testInstance, ExtensionContext context) throws Exception {
          getTestContextManager(context).prepareTestInstance(testInstance);
     }

     /**
      * Delegates to {@link TestContextManager#beforeTestMethod}.
      */
     @Override
     public void beforeEach(ExtensionContext context) throws Exception {
          Object testInstance = getRequiredTestInstance(context);
          Method testMethod = getRequiredTestMethod(context);
          getTestContextManager(context).beforeTestMethod(testInstance, testMethod);
     }

     /**
      * Delegates to {@link TestContextManager#afterTestMethod}.
      */
     @Override
     public void afterEach(ExtensionContext context) throws Exception {
          Object testInstance = getRequiredTestInstance(context);
          Method testMethod = getRequiredTestMethod(context);
          Throwable testException = context.getExecutionException().orElse(null);
          getTestContextManager(context).afterTestMethod(testInstance, testMethod, testException);
     }

     /**
      * Determine if the value for the {@link Parameter} in the supplied
      * {@link ParameterContext} should be autowired from the test's
      * {@link ApplicationContext}.
      * <p>Returns {@code true} if the parameter is declared in a {@link Constructor}
      * that is annotated with {@link Autowired @Autowired} and otherwise delegates
      * to {@link ParameterAutowireUtils#isAutowirable}.
      * <p><strong>WARNING</strong>: if the parameter is declared in a {@code Constructor}
      * that is annotated with {@code @Autowired}, Spring will assume the responsibility
      * for resolving all parameters in the constructor. Consequently, no other
      * registered {@link ParameterResolver} will be able to resolve parameters.
      *
      * @see #resolveParameter
      * @see ParameterAutowireUtils#isAutowirable
      */
     @Override
     public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext) {
          Parameter parameter = parameterContext.getParameter();
          Executable executable = parameter.getDeclaringExecutable();
          return (executable instanceof Constructor && AnnotatedElementUtils.hasAnnotation(executable, Autowired.class))
                  || ParameterAutowireUtils.isAutowirable(parameter);
     }

     /**
      * Resolve a value for the {@link Parameter} in the supplied
      * {@link ParameterContext} by retrieving the corresponding dependency
      * from the test's {@link ApplicationContext}.
      * <p>Delegates to {@link ParameterAutowireUtils#resolveDependency}.
      *
      * @see #supportsParameter
      * @see ParameterAutowireUtils#resolveDependency
      */
     @Override
     public Object resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext) {
          Parameter parameter = parameterContext.getParameter();
          Class<?> testClass = getRequiredTestClass(extensionContext);
          ApplicationContext applicationContext = getApplicationContext(extensionContext);
          return ParameterAutowireUtils.resolveDependency(parameter, testClass, applicationContext);
     }

}