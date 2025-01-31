/*
 * Copyright 2025 The ModXstream Project Authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.pnambic.modxstream;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.XppDriver;

/**
 * Gather the elements for XStream based document serialization.
 */
public class XstreamDocumentTransportBuilder {

  private final XppDriver streamDriver;

  private final XStream xstream;

  /**
   * Prepare a new builder.
   */
  public XstreamDocumentTransportBuilder() {
    streamDriver = new XppDriver();
    xstream = new XStream(streamDriver);
  }

  /**
   * Indicates that internal object references always indicate embedded
   * objects.
   */
  public void setNoReferences() {
    xstream.setMode(XStream.NO_REFERENCES);
  }

  /**
   * Delegate to XStream {@code alias()}.
   *
   * @param alias Short name
   * @param type Type to be aliased
   */
  public void addAlias(String alias, Class<?> type) {
    xstream.alias(alias, type);
  }

  /**
   * Delegate to XStream {@code aliasType()}.
   * Any class that is assignable to this type will be aliased to the same name.
   *
   * @param alias Short name
   * @param type Type to be aliased
   */
  public void addAliasType(String alias, Class<?> type) {
    xstream.aliasType(alias, type);
  }

  /**
   * Delegate to XStream {@code aliasField()}.
   *
   * @param alias Short name
   * @param fieldType Type that declares the field
   * @param fieldName Name of the field
   */
  public void addAliasField(
      String alias, Class<?> fieldType, String fieldName) {
    xstream.aliasField(alias, fieldType, fieldName);
  }

  /**
   * Delegate to XStream {@code allowTypes()}.
   *
   * @param allowedTypes Type to allow
   */
  public void addAllowedType(Class<?>[] allowedTypes) {
    xstream.allowTypes(allowedTypes);
  }

  /**
   * Delegate to XStream {@code registerConverter()}, with wrappers and
   * other setup.
   *
   * Add a {@link XstreamObjectConverter} to the {@link XStream}.
   * Configures the converter's alias, allowed types, and installs
   * it as a {@code xstream.registerConverter()} with a
   * {@link DelegateObjectXmlConverter} wrappter.
   *
   * @param contrib Converter to contribute.
   */
  public void addConverter(XstreamObjectConverter contrib) {
    addAlias(contrib.getTag(), contrib.forType());
    addAllowedType(contrib.getAllowTypes());
    xstream.registerConverter(
        new DelegateObjectXmlConverter(contrib, xstream.getMapper()));
  }

  /**
   * Delegate to XStream {@code addDefaultImplementation()}.
   * Typical use is to use {@code ArrayList} for anything that
   * is a {@code List}.
   *
   * @param useType Concrete type
   * @param forType Serialized type
   */
  public void addDefaultImplementation(Class<?> useType, Class<?> forType) {
    xstream.addDefaultImplementation(useType, forType);
  }

  /**
   * Delegate to XStream {@code addImplicitCollection()}.
   *
   * @param type Class owning the implicit array
   * @param fieldName Name of the array field
   */
  public void addImplicitCollection(Class<?> type, String fieldName) {
    xstream.addImplicitCollection(type, fieldName);
  }

  /**
   * Delegate to XStream {@code processAnnotations()}.
   *
   * @param type Type with XStream annotations
   */
  public void processAnnotations(Class<?> type) {
    xstream.processAnnotations(type);
  }

  /**
   * Provides a bundled {@link XstreamDocumentTransport} for loading
   * or saving objects.
   *
   * @return Configured transport
   */
  public XstreamDocumentTransport buildDocumentXmlPersist() {
    return new XstreamDocumentTransport(xstream, streamDriver);
  }
}
