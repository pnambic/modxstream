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

/**
 * Defines a converter for the parameterized type.
 */
public interface XstreamObjectConverter {

  /**
   * Indicate the primary type supported by this converter.
   *
   * @return Type for converter
   */
  Class<?> forType();

  /**
   * Marshal the source object to the destination context.
   *
   * @param dstContext Destination for object
   * @param source Object to marshal
   */
  void marshal(XstreamMarshalContext dstContext, Object source);

  /**
   * Unmarshal an object from the destination context.
   *
   * @param srcContext Source for persistent representation
   * @return Restored instance
   */
  Object unmarshal(XstreamUnmarshalContext srcContext);

  /**
   * Provide the tag to use for marshaling the type.
   *
   * @return Tag text
   */
  String getTag();

  /**
   * Provide the types that this converter will allow.
   *
   * @return Array of allowed types
   */
  Class<?>[] getAllowTypes();
}
