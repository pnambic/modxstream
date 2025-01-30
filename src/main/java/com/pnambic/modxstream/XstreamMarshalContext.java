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

import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;
import com.thoughtworks.xstream.mapper.Mapper;

/**
 * Module safe wrapper for XStream marshaling components, such as
 * {@link HierarchicalStreamWriter}, {@link MarshallingContext},
 * and {@link Mapper}.
 */
public class XstreamMarshalContext {

  private final HierarchicalStreamWriter writer;

  private final MarshallingContext context;

  private final Mapper mapper;

  /**
   * Prepare a marshaling context from the supplied components.
   *
   * @param writer Destination for persistent representation
   * @param context Context for marshaling
   * @param mapper Mapper for marshaling
   */
  public XstreamMarshalContext(
      HierarchicalStreamWriter writer,
      MarshallingContext context,
      Mapper mapper) {
    this.writer = writer;
    this.context = context;
    this.mapper = mapper;
  }

  /**
   * Delegate to Mapper {@code serializedClass()}.
   * 
   * @param type Type to serialize
   * @return {@link String} to used for serialized type
   */
  public String serializedClass(Class<?> type) {
    return mapper.serializedClass(type);
  }

  /**
   * Delegate to HierarchicalStreamWriter {@code startNode()}.
   *
   * @param tag Text for node label
   */
  public void startNode(String tag) {
    writer.startNode(tag);
  }

  /**
   * Delegate to HierarchicalStreamWriter {@code endNode()}.
   */
  public void endNode() {
    writer.endNode();
  }

  /**
   * Delegate to MarshallingContext {@code convertAnother()}.
   *
   * @param value Value to marshal
   */
  public void convertAnother(Object value) {
    context.convertAnother(value);
  }

  /**
   * Delegate to MarshallingContext {@code put()}.
   *
   * @param key Key for saved value
   * @param value Value to save
   */
  public void putContextValue(Object key, Object value) {
    context.put(key, value);
  }

  /**
   * Delegate to MarshallingContext {@code get()}.
   *
   * @param key Key for saved value
   * @return Previously saved value
   */
  public Object getContextValue(Object key) {
    return context.get(key);
  }
}
