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

import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.xml.AbstractPullReader;
import com.thoughtworks.xstream.mapper.Mapper;

/**
 * Bundle all the components useful for unmarshalling.
 */
public class XstreamUnmarshalContext {

  /**
   * Convenience class for looking ahead in the reader.
   */
  public static class PeekableReader {

    private final AbstractPullReader peekable; // = srcContext.getPeekableReader();

    /**
     * Provide a reader with simple look ahead.
     *
     * @param peekable Underlying reader
     */
    public PeekableReader(AbstractPullReader peekable) {
      this.peekable = peekable;
    }

    /**
     * Indicate if the hierarchical component has more children components.
     *
     * @return {@code true} if reader has more children components
     */
    public boolean hasMoreChildren() {
      return peekable.hasMoreChildren();
    }

    /**
     * Provide the value of the next string without consuming it.
     *
     * @return Text of next child
     */
    public String peekNextChild() {
      return peekable.peekNextChild();
    }
  }

  private final HierarchicalStreamReader reader;

  private final UnmarshallingContext context;

  private final Mapper mapper;

  /**
   * Provide a complete context for unmarshaling.
   *
   * @param reader Reader for unmarshaling
   * @param context Context for unmarshalling
   * @param mapper Mapper for unmarshalling
   */
  public XstreamUnmarshalContext(HierarchicalStreamReader reader,
      UnmarshallingContext context, Mapper mapper) {
    this.reader = reader;
    this.context = context;
    this.mapper = mapper;
  }

  /**
   * Delegate to Reader {@code moveDown()}.
   */
  public void moveDown() {
    reader.moveDown();
  }

  /**
   * Delegate to Reader {@code moveUp()}.
   */
  public void moveUp() {
    reader.moveUp();
  }

  /**
   * Delegate to Reader {@code getNodeName()}.
   *
   * @return Name for node
   */
  public String getNodeName() {
    return reader.getNodeName();
  }

  /**
   * Delegate to Reader {@code getValue()}.
   *
   * @return Text content of the current node
   */
  public String getValue() {
    return reader.getValue();
  }

  /**
   * Delegate to Reader {@code hasMoreChildren()}.
   *
   * @return {@code true} if reader has more children components
   */
  public boolean hasMoreChildren() {
    return reader.hasMoreChildren();
  }

  /**
   * Delegate to UnmarshallingContext {@code convertAnother()}.
   *
   * @param object Receiving object
   * @param childClass Class to unmarshal
   * @return Restored instance
   */
  public Object convertAnother(Object object, Class<?> childClass) {
    return context.convertAnother(object, childClass);
  }

  /**
   * Delegate to Mapper {@code realClass()}.
   *
   * @param typeName Name for class
   * @return Type for the supplied type name
   */
  public Class<?> realClass(String typeName) {
    return mapper.realClass(typeName);
  }

  /**
   * Delegate to UnmarshallingContext {@code put()}.
   *
   * @param key Key for saved value
   * @param value Value to save
   */
  public void putContextValue(Object key, Object value) {
    context.put(key, value);
  }

  /**
   * Delegate to UnmarshallingContext {@code get()}.
   *
   * @param key Key for saved value
   * @return Saved value from context
   */
  public Object getContextValue(Object key) {
    return context.get(key);
  }

  /**
   * Provide a {@link PeekableReader} from the current reader.
   *
   * @return Peekable reader
   */
  public PeekableReader getPeekableReader() {
    return new PeekableReader((AbstractPullReader) reader.underlyingReader());
  }
}
