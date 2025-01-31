# `modxstream`

The `modxstream` project provides a JMS compliant wrapper for
[XStream](https://x-stream.github.io/).
The published `.jar` file has an `Automatic-Module-Name`,
allowing it access classpath components and provide module path components.

As a classpath component, XStream can poke into the internals of JVM types.
This is great for serializing any conceivable type,
but it also presents problems for XStream's use in a modular Java application.

The current `modxstream` implementation supports many popular XStream
configurations options, but not all.
Patches to extend support to other XStream features are welcome.

## `modxstream` Releases

The `modxstream` artifacts are available on Maven Central.

## `modxstream` Features

In order to hide the XStream types from the Java modularity system,
the various XStream components are hidden largely within
the `XstreamDocumentTransport` type.

The `XstreamDocumentTransport` type offers a very lightweight
abstraction on the capabilities of the XStream library.

* Simple `load()` and `save()` methods to encapsulate persistence.
* Guarantees the use of an XppDriver that supports look ahead.
* Includes a DataHolder to accommodate user defined context.

The `XstreamDocumentTransportBuilder` type encapsulates
the construction of an `XstreamDocumentTransport` instance.
Many of its methods simply delegate their behaviors to the matching
XStream methods.  Its `buildDocumentXmlPersist()` completes the
configuration stage, providing a `XstreamDocumentTransport` instance
that has been configured for the expected serialization types.

## `modxstream` Converters

The underlying XStream type for custom converters seem intended
for context independent serialization.
This presents a problem for serialization based on the runtime context,
such as references in a user's workspace.

The `XstreamObjectConverter` type bundles a few configuration options
together with richer context objects.
These capabilities simplify the implementation of type specific converters

* Direct manipulation of heterogeneous dependency information.
* Analysis and visualization of very large applications.
* For Java, dependency discovery at the class member level.
* Selection of nodes by type, edge-count, and paths.

The `XstreamMarshalContext` and `XstreamUnmarshalContext`
instances provided to the `marshal()` and `unmarshal()`
of `XstreamObjectConverter`
provide a richer set of context for serialization operations.
These include access to context, peek ahead, data serialization utilities.


## Information for Contributors
Contributions to `modxstream` are welcome.
The obvious areas to improve include:
* More complete access to the underlying XStream capabilities.
* More complete user documentation, including a user manual.
