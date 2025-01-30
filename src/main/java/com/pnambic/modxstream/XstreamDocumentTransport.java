/*
 * Copyright 2009, 2023 The Depan Project Authors
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
import com.thoughtworks.xstream.converters.DataHolder;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;
import com.thoughtworks.xstream.io.xml.XppDriver;

import java.io.Reader;
import java.io.Writer;

/**
 * Handle persistence of a document object to and from XML files.
 * This should correctly serialize any DepAn object that contains plugin
 * defined nodes and edges.
 * <p>
 * As tempting as it appears to re-write this with generics, simple approaches
 * fail to provide advantages due to erasure.  Adding a generic class to the
 * type fails to provide the necessary information to correctly cast the
 * results from {@code #load(URI)}.  The compiler will accept the cast, but
 * it doesn't do the right thing.  It could work if the constructor actually
 * accepted a type token (e.g. {@code Blix.class}), but that's a more heavy
 * handed implementation.
 *
 * @author <a href="mailto:leeca@google.com">Lee Carver</a>
 */
public class XstreamDocumentTransport {
  /**
   * The entity that converts objects to XML.
   */
  protected final XStream xstream;

  private final XppDriver streamDriver;

  private DataHolder context;

  /**
   * Create a serializer using the provided XStream.
   *
   * @param xstream {@code XStream} to use for serialization
   * @param streamDriver input parser
   */
  public XstreamDocumentTransport(XStream xstream, XppDriver streamDriver) {
    this.xstream = xstream;
    this.streamDriver = streamDriver;
  }

  /**
   * Install a well-known value as the context for persistence operations.
   *
   * @param key Typically the value's {@code .class} constant
   * @param value Useful data
   */
  public void addContextValue(Object key, Object value) {
    if (context == null) {
      context = xstream.newDataHolder();
    }
    context.put(key, value);
  }

  /**
   * Load an object from the provided URI.
   *
   * @param src Location of persistent representation
   * @return Restored instance
   */
  public Object load(Reader src) {
    // 'cuz no XStream.fromXml() method allows for supplied context.
    HierarchicalStreamReader reader = streamDriver.createReader(src);
    try {
      return xstream.unmarshal(reader, null, context);
    } finally {
      reader.close();
    }
  }

  /**
   * Save an object to the provided URI.
   *
   * @param dst Location for persistent representation
   * @param item Object to persist
   */
  public void save(Writer dst, Object item) {
    toXML(item, dst);
  }

  /**
   * Serialize an object to the given Writer as pretty-printed XML.
   * The Writer will be flushed afterwards and in case of an exception.
   *
   * Stolen from XStream, to add context for the marshal call.
   */
  private void toXML(Object obj, Writer out) {
      HierarchicalStreamWriter writer = streamDriver.createWriter(out);
      try {
          xstream.marshal(obj, writer, context);
      } finally {
          writer.flush();
      }
  }
}
