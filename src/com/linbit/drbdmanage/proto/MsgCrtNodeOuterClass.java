// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: MsgCrtNode.proto

package com.linbit.drbdmanage.proto;

public final class MsgCrtNodeOuterClass {
  private MsgCrtNodeOuterClass() {}
  public static void registerAllExtensions(
      com.google.protobuf.ExtensionRegistryLite registry) {
  }

  public static void registerAllExtensions(
      com.google.protobuf.ExtensionRegistry registry) {
    registerAllExtensions(
        (com.google.protobuf.ExtensionRegistryLite) registry);
  }
  public interface MsgCrtNodeOrBuilder extends
      // @@protoc_insertion_point(interface_extends:com.linbit.drbdmanage.proto.MsgCrtNode)
      com.google.protobuf.MessageOrBuilder {

    /**
     * <pre>
     * Node name
     * </pre>
     *
     * <code>string node_name = 1;</code>
     */
    java.lang.String getNodeName();
    /**
     * <pre>
     * Node name
     * </pre>
     *
     * <code>string node_name = 1;</code>
     */
    com.google.protobuf.ByteString
        getNodeNameBytes();

    /**
     * <pre>
     * Node type
     * </pre>
     *
     * <code>string node_type = 2;</code>
     */
    java.lang.String getNodeType();
    /**
     * <pre>
     * Node type
     * </pre>
     *
     * <code>string node_type = 2;</code>
     */
    com.google.protobuf.ByteString
        getNodeTypeBytes();

    /**
     * <pre>
     * Node properties map
     * </pre>
     *
     * <code>map&lt;string, string&gt; node_props = 3;</code>
     */
    int getNodePropsCount();
    /**
     * <pre>
     * Node properties map
     * </pre>
     *
     * <code>map&lt;string, string&gt; node_props = 3;</code>
     */
    boolean containsNodeProps(
        java.lang.String key);
    /**
     * Use {@link #getNodePropsMap()} instead.
     */
    @java.lang.Deprecated
    java.util.Map<java.lang.String, java.lang.String>
    getNodeProps();
    /**
     * <pre>
     * Node properties map
     * </pre>
     *
     * <code>map&lt;string, string&gt; node_props = 3;</code>
     */
    java.util.Map<java.lang.String, java.lang.String>
    getNodePropsMap();
    /**
     * <pre>
     * Node properties map
     * </pre>
     *
     * <code>map&lt;string, string&gt; node_props = 3;</code>
     */

    java.lang.String getNodePropsOrDefault(
        java.lang.String key,
        java.lang.String defaultValue);
    /**
     * <pre>
     * Node properties map
     * </pre>
     *
     * <code>map&lt;string, string&gt; node_props = 3;</code>
     */

    java.lang.String getNodePropsOrThrow(
        java.lang.String key);
  }
  /**
   * <pre>
   * drbdmanageNG - Create node
   * </pre>
   *
   * Protobuf type {@code com.linbit.drbdmanage.proto.MsgCrtNode}
   */
  public  static final class MsgCrtNode extends
      com.google.protobuf.GeneratedMessageV3 implements
      // @@protoc_insertion_point(message_implements:com.linbit.drbdmanage.proto.MsgCrtNode)
      MsgCrtNodeOrBuilder {
    // Use MsgCrtNode.newBuilder() to construct.
    private MsgCrtNode(com.google.protobuf.GeneratedMessageV3.Builder<?> builder) {
      super(builder);
    }
    private MsgCrtNode() {
      nodeName_ = "";
      nodeType_ = "";
    }

    @java.lang.Override
    public final com.google.protobuf.UnknownFieldSet
    getUnknownFields() {
      return com.google.protobuf.UnknownFieldSet.getDefaultInstance();
    }
    private MsgCrtNode(
        com.google.protobuf.CodedInputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      this();
      int mutable_bitField0_ = 0;
      try {
        boolean done = false;
        while (!done) {
          int tag = input.readTag();
          switch (tag) {
            case 0:
              done = true;
              break;
            default: {
              if (!input.skipField(tag)) {
                done = true;
              }
              break;
            }
            case 10: {
              java.lang.String s = input.readStringRequireUtf8();

              nodeName_ = s;
              break;
            }
            case 18: {
              java.lang.String s = input.readStringRequireUtf8();

              nodeType_ = s;
              break;
            }
            case 26: {
              if (!((mutable_bitField0_ & 0x00000004) == 0x00000004)) {
                nodeProps_ = com.google.protobuf.MapField.newMapField(
                    NodePropsDefaultEntryHolder.defaultEntry);
                mutable_bitField0_ |= 0x00000004;
              }
              com.google.protobuf.MapEntry<java.lang.String, java.lang.String>
              nodeProps__ = input.readMessage(
                  NodePropsDefaultEntryHolder.defaultEntry.getParserForType(), extensionRegistry);
              nodeProps_.getMutableMap().put(
                  nodeProps__.getKey(), nodeProps__.getValue());
              break;
            }
          }
        }
      } catch (com.google.protobuf.InvalidProtocolBufferException e) {
        throw e.setUnfinishedMessage(this);
      } catch (java.io.IOException e) {
        throw new com.google.protobuf.InvalidProtocolBufferException(
            e).setUnfinishedMessage(this);
      } finally {
        makeExtensionsImmutable();
      }
    }
    public static final com.google.protobuf.Descriptors.Descriptor
        getDescriptor() {
      return com.linbit.drbdmanage.proto.MsgCrtNodeOuterClass.internal_static_com_linbit_drbdmanage_proto_MsgCrtNode_descriptor;
    }

    @SuppressWarnings({"rawtypes"})
    protected com.google.protobuf.MapField internalGetMapField(
        int number) {
      switch (number) {
        case 3:
          return internalGetNodeProps();
        default:
          throw new RuntimeException(
              "Invalid map field number: " + number);
      }
    }
    protected com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
        internalGetFieldAccessorTable() {
      return com.linbit.drbdmanage.proto.MsgCrtNodeOuterClass.internal_static_com_linbit_drbdmanage_proto_MsgCrtNode_fieldAccessorTable
          .ensureFieldAccessorsInitialized(
              com.linbit.drbdmanage.proto.MsgCrtNodeOuterClass.MsgCrtNode.class, com.linbit.drbdmanage.proto.MsgCrtNodeOuterClass.MsgCrtNode.Builder.class);
    }

    private int bitField0_;
    public static final int NODE_NAME_FIELD_NUMBER = 1;
    private volatile java.lang.Object nodeName_;
    /**
     * <pre>
     * Node name
     * </pre>
     *
     * <code>string node_name = 1;</code>
     */
    public java.lang.String getNodeName() {
      java.lang.Object ref = nodeName_;
      if (ref instanceof java.lang.String) {
        return (java.lang.String) ref;
      } else {
        com.google.protobuf.ByteString bs = 
            (com.google.protobuf.ByteString) ref;
        java.lang.String s = bs.toStringUtf8();
        nodeName_ = s;
        return s;
      }
    }
    /**
     * <pre>
     * Node name
     * </pre>
     *
     * <code>string node_name = 1;</code>
     */
    public com.google.protobuf.ByteString
        getNodeNameBytes() {
      java.lang.Object ref = nodeName_;
      if (ref instanceof java.lang.String) {
        com.google.protobuf.ByteString b = 
            com.google.protobuf.ByteString.copyFromUtf8(
                (java.lang.String) ref);
        nodeName_ = b;
        return b;
      } else {
        return (com.google.protobuf.ByteString) ref;
      }
    }

    public static final int NODE_TYPE_FIELD_NUMBER = 2;
    private volatile java.lang.Object nodeType_;
    /**
     * <pre>
     * Node type
     * </pre>
     *
     * <code>string node_type = 2;</code>
     */
    public java.lang.String getNodeType() {
      java.lang.Object ref = nodeType_;
      if (ref instanceof java.lang.String) {
        return (java.lang.String) ref;
      } else {
        com.google.protobuf.ByteString bs = 
            (com.google.protobuf.ByteString) ref;
        java.lang.String s = bs.toStringUtf8();
        nodeType_ = s;
        return s;
      }
    }
    /**
     * <pre>
     * Node type
     * </pre>
     *
     * <code>string node_type = 2;</code>
     */
    public com.google.protobuf.ByteString
        getNodeTypeBytes() {
      java.lang.Object ref = nodeType_;
      if (ref instanceof java.lang.String) {
        com.google.protobuf.ByteString b = 
            com.google.protobuf.ByteString.copyFromUtf8(
                (java.lang.String) ref);
        nodeType_ = b;
        return b;
      } else {
        return (com.google.protobuf.ByteString) ref;
      }
    }

    public static final int NODE_PROPS_FIELD_NUMBER = 3;
    private static final class NodePropsDefaultEntryHolder {
      static final com.google.protobuf.MapEntry<
          java.lang.String, java.lang.String> defaultEntry =
              com.google.protobuf.MapEntry
              .<java.lang.String, java.lang.String>newDefaultInstance(
                  com.linbit.drbdmanage.proto.MsgCrtNodeOuterClass.internal_static_com_linbit_drbdmanage_proto_MsgCrtNode_NodePropsEntry_descriptor, 
                  com.google.protobuf.WireFormat.FieldType.STRING,
                  "",
                  com.google.protobuf.WireFormat.FieldType.STRING,
                  "");
    }
    private com.google.protobuf.MapField<
        java.lang.String, java.lang.String> nodeProps_;
    private com.google.protobuf.MapField<java.lang.String, java.lang.String>
    internalGetNodeProps() {
      if (nodeProps_ == null) {
        return com.google.protobuf.MapField.emptyMapField(
            NodePropsDefaultEntryHolder.defaultEntry);
      }
      return nodeProps_;
    }

    public int getNodePropsCount() {
      return internalGetNodeProps().getMap().size();
    }
    /**
     * <pre>
     * Node properties map
     * </pre>
     *
     * <code>map&lt;string, string&gt; node_props = 3;</code>
     */

    public boolean containsNodeProps(
        java.lang.String key) {
      if (key == null) { throw new java.lang.NullPointerException(); }
      return internalGetNodeProps().getMap().containsKey(key);
    }
    /**
     * Use {@link #getNodePropsMap()} instead.
     */
    @java.lang.Deprecated
    public java.util.Map<java.lang.String, java.lang.String> getNodeProps() {
      return getNodePropsMap();
    }
    /**
     * <pre>
     * Node properties map
     * </pre>
     *
     * <code>map&lt;string, string&gt; node_props = 3;</code>
     */

    public java.util.Map<java.lang.String, java.lang.String> getNodePropsMap() {
      return internalGetNodeProps().getMap();
    }
    /**
     * <pre>
     * Node properties map
     * </pre>
     *
     * <code>map&lt;string, string&gt; node_props = 3;</code>
     */

    public java.lang.String getNodePropsOrDefault(
        java.lang.String key,
        java.lang.String defaultValue) {
      if (key == null) { throw new java.lang.NullPointerException(); }
      java.util.Map<java.lang.String, java.lang.String> map =
          internalGetNodeProps().getMap();
      return map.containsKey(key) ? map.get(key) : defaultValue;
    }
    /**
     * <pre>
     * Node properties map
     * </pre>
     *
     * <code>map&lt;string, string&gt; node_props = 3;</code>
     */

    public java.lang.String getNodePropsOrThrow(
        java.lang.String key) {
      if (key == null) { throw new java.lang.NullPointerException(); }
      java.util.Map<java.lang.String, java.lang.String> map =
          internalGetNodeProps().getMap();
      if (!map.containsKey(key)) {
        throw new java.lang.IllegalArgumentException();
      }
      return map.get(key);
    }

    private byte memoizedIsInitialized = -1;
    public final boolean isInitialized() {
      byte isInitialized = memoizedIsInitialized;
      if (isInitialized == 1) return true;
      if (isInitialized == 0) return false;

      memoizedIsInitialized = 1;
      return true;
    }

    public void writeTo(com.google.protobuf.CodedOutputStream output)
                        throws java.io.IOException {
      if (!getNodeNameBytes().isEmpty()) {
        com.google.protobuf.GeneratedMessageV3.writeString(output, 1, nodeName_);
      }
      if (!getNodeTypeBytes().isEmpty()) {
        com.google.protobuf.GeneratedMessageV3.writeString(output, 2, nodeType_);
      }
      com.google.protobuf.GeneratedMessageV3
        .serializeStringMapTo(
          output,
          internalGetNodeProps(),
          NodePropsDefaultEntryHolder.defaultEntry,
          3);
    }

    public int getSerializedSize() {
      int size = memoizedSize;
      if (size != -1) return size;

      size = 0;
      if (!getNodeNameBytes().isEmpty()) {
        size += com.google.protobuf.GeneratedMessageV3.computeStringSize(1, nodeName_);
      }
      if (!getNodeTypeBytes().isEmpty()) {
        size += com.google.protobuf.GeneratedMessageV3.computeStringSize(2, nodeType_);
      }
      for (java.util.Map.Entry<java.lang.String, java.lang.String> entry
           : internalGetNodeProps().getMap().entrySet()) {
        com.google.protobuf.MapEntry<java.lang.String, java.lang.String>
        nodeProps__ = NodePropsDefaultEntryHolder.defaultEntry.newBuilderForType()
            .setKey(entry.getKey())
            .setValue(entry.getValue())
            .build();
        size += com.google.protobuf.CodedOutputStream
            .computeMessageSize(3, nodeProps__);
      }
      memoizedSize = size;
      return size;
    }

    private static final long serialVersionUID = 0L;
    @java.lang.Override
    public boolean equals(final java.lang.Object obj) {
      if (obj == this) {
       return true;
      }
      if (!(obj instanceof com.linbit.drbdmanage.proto.MsgCrtNodeOuterClass.MsgCrtNode)) {
        return super.equals(obj);
      }
      com.linbit.drbdmanage.proto.MsgCrtNodeOuterClass.MsgCrtNode other = (com.linbit.drbdmanage.proto.MsgCrtNodeOuterClass.MsgCrtNode) obj;

      boolean result = true;
      result = result && getNodeName()
          .equals(other.getNodeName());
      result = result && getNodeType()
          .equals(other.getNodeType());
      result = result && internalGetNodeProps().equals(
          other.internalGetNodeProps());
      return result;
    }

    @java.lang.Override
    public int hashCode() {
      if (memoizedHashCode != 0) {
        return memoizedHashCode;
      }
      int hash = 41;
      hash = (19 * hash) + getDescriptor().hashCode();
      hash = (37 * hash) + NODE_NAME_FIELD_NUMBER;
      hash = (53 * hash) + getNodeName().hashCode();
      hash = (37 * hash) + NODE_TYPE_FIELD_NUMBER;
      hash = (53 * hash) + getNodeType().hashCode();
      if (!internalGetNodeProps().getMap().isEmpty()) {
        hash = (37 * hash) + NODE_PROPS_FIELD_NUMBER;
        hash = (53 * hash) + internalGetNodeProps().hashCode();
      }
      hash = (29 * hash) + unknownFields.hashCode();
      memoizedHashCode = hash;
      return hash;
    }

    public static com.linbit.drbdmanage.proto.MsgCrtNodeOuterClass.MsgCrtNode parseFrom(
        com.google.protobuf.ByteString data)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data);
    }
    public static com.linbit.drbdmanage.proto.MsgCrtNodeOuterClass.MsgCrtNode parseFrom(
        com.google.protobuf.ByteString data,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data, extensionRegistry);
    }
    public static com.linbit.drbdmanage.proto.MsgCrtNodeOuterClass.MsgCrtNode parseFrom(byte[] data)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data);
    }
    public static com.linbit.drbdmanage.proto.MsgCrtNodeOuterClass.MsgCrtNode parseFrom(
        byte[] data,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data, extensionRegistry);
    }
    public static com.linbit.drbdmanage.proto.MsgCrtNodeOuterClass.MsgCrtNode parseFrom(java.io.InputStream input)
        throws java.io.IOException {
      return com.google.protobuf.GeneratedMessageV3
          .parseWithIOException(PARSER, input);
    }
    public static com.linbit.drbdmanage.proto.MsgCrtNodeOuterClass.MsgCrtNode parseFrom(
        java.io.InputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return com.google.protobuf.GeneratedMessageV3
          .parseWithIOException(PARSER, input, extensionRegistry);
    }
    public static com.linbit.drbdmanage.proto.MsgCrtNodeOuterClass.MsgCrtNode parseDelimitedFrom(java.io.InputStream input)
        throws java.io.IOException {
      return com.google.protobuf.GeneratedMessageV3
          .parseDelimitedWithIOException(PARSER, input);
    }
    public static com.linbit.drbdmanage.proto.MsgCrtNodeOuterClass.MsgCrtNode parseDelimitedFrom(
        java.io.InputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return com.google.protobuf.GeneratedMessageV3
          .parseDelimitedWithIOException(PARSER, input, extensionRegistry);
    }
    public static com.linbit.drbdmanage.proto.MsgCrtNodeOuterClass.MsgCrtNode parseFrom(
        com.google.protobuf.CodedInputStream input)
        throws java.io.IOException {
      return com.google.protobuf.GeneratedMessageV3
          .parseWithIOException(PARSER, input);
    }
    public static com.linbit.drbdmanage.proto.MsgCrtNodeOuterClass.MsgCrtNode parseFrom(
        com.google.protobuf.CodedInputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return com.google.protobuf.GeneratedMessageV3
          .parseWithIOException(PARSER, input, extensionRegistry);
    }

    public Builder newBuilderForType() { return newBuilder(); }
    public static Builder newBuilder() {
      return DEFAULT_INSTANCE.toBuilder();
    }
    public static Builder newBuilder(com.linbit.drbdmanage.proto.MsgCrtNodeOuterClass.MsgCrtNode prototype) {
      return DEFAULT_INSTANCE.toBuilder().mergeFrom(prototype);
    }
    public Builder toBuilder() {
      return this == DEFAULT_INSTANCE
          ? new Builder() : new Builder().mergeFrom(this);
    }

    @java.lang.Override
    protected Builder newBuilderForType(
        com.google.protobuf.GeneratedMessageV3.BuilderParent parent) {
      Builder builder = new Builder(parent);
      return builder;
    }
    /**
     * <pre>
     * drbdmanageNG - Create node
     * </pre>
     *
     * Protobuf type {@code com.linbit.drbdmanage.proto.MsgCrtNode}
     */
    public static final class Builder extends
        com.google.protobuf.GeneratedMessageV3.Builder<Builder> implements
        // @@protoc_insertion_point(builder_implements:com.linbit.drbdmanage.proto.MsgCrtNode)
        com.linbit.drbdmanage.proto.MsgCrtNodeOuterClass.MsgCrtNodeOrBuilder {
      public static final com.google.protobuf.Descriptors.Descriptor
          getDescriptor() {
        return com.linbit.drbdmanage.proto.MsgCrtNodeOuterClass.internal_static_com_linbit_drbdmanage_proto_MsgCrtNode_descriptor;
      }

      @SuppressWarnings({"rawtypes"})
      protected com.google.protobuf.MapField internalGetMapField(
          int number) {
        switch (number) {
          case 3:
            return internalGetNodeProps();
          default:
            throw new RuntimeException(
                "Invalid map field number: " + number);
        }
      }
      @SuppressWarnings({"rawtypes"})
      protected com.google.protobuf.MapField internalGetMutableMapField(
          int number) {
        switch (number) {
          case 3:
            return internalGetMutableNodeProps();
          default:
            throw new RuntimeException(
                "Invalid map field number: " + number);
        }
      }
      protected com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
          internalGetFieldAccessorTable() {
        return com.linbit.drbdmanage.proto.MsgCrtNodeOuterClass.internal_static_com_linbit_drbdmanage_proto_MsgCrtNode_fieldAccessorTable
            .ensureFieldAccessorsInitialized(
                com.linbit.drbdmanage.proto.MsgCrtNodeOuterClass.MsgCrtNode.class, com.linbit.drbdmanage.proto.MsgCrtNodeOuterClass.MsgCrtNode.Builder.class);
      }

      // Construct using com.linbit.drbdmanage.proto.MsgCrtNodeOuterClass.MsgCrtNode.newBuilder()
      private Builder() {
        maybeForceBuilderInitialization();
      }

      private Builder(
          com.google.protobuf.GeneratedMessageV3.BuilderParent parent) {
        super(parent);
        maybeForceBuilderInitialization();
      }
      private void maybeForceBuilderInitialization() {
        if (com.google.protobuf.GeneratedMessageV3
                .alwaysUseFieldBuilders) {
        }
      }
      public Builder clear() {
        super.clear();
        nodeName_ = "";

        nodeType_ = "";

        internalGetMutableNodeProps().clear();
        return this;
      }

      public com.google.protobuf.Descriptors.Descriptor
          getDescriptorForType() {
        return com.linbit.drbdmanage.proto.MsgCrtNodeOuterClass.internal_static_com_linbit_drbdmanage_proto_MsgCrtNode_descriptor;
      }

      public com.linbit.drbdmanage.proto.MsgCrtNodeOuterClass.MsgCrtNode getDefaultInstanceForType() {
        return com.linbit.drbdmanage.proto.MsgCrtNodeOuterClass.MsgCrtNode.getDefaultInstance();
      }

      public com.linbit.drbdmanage.proto.MsgCrtNodeOuterClass.MsgCrtNode build() {
        com.linbit.drbdmanage.proto.MsgCrtNodeOuterClass.MsgCrtNode result = buildPartial();
        if (!result.isInitialized()) {
          throw newUninitializedMessageException(result);
        }
        return result;
      }

      public com.linbit.drbdmanage.proto.MsgCrtNodeOuterClass.MsgCrtNode buildPartial() {
        com.linbit.drbdmanage.proto.MsgCrtNodeOuterClass.MsgCrtNode result = new com.linbit.drbdmanage.proto.MsgCrtNodeOuterClass.MsgCrtNode(this);
        int from_bitField0_ = bitField0_;
        int to_bitField0_ = 0;
        result.nodeName_ = nodeName_;
        result.nodeType_ = nodeType_;
        result.nodeProps_ = internalGetNodeProps();
        result.nodeProps_.makeImmutable();
        result.bitField0_ = to_bitField0_;
        onBuilt();
        return result;
      }

      public Builder clone() {
        return (Builder) super.clone();
      }
      public Builder setField(
          com.google.protobuf.Descriptors.FieldDescriptor field,
          Object value) {
        return (Builder) super.setField(field, value);
      }
      public Builder clearField(
          com.google.protobuf.Descriptors.FieldDescriptor field) {
        return (Builder) super.clearField(field);
      }
      public Builder clearOneof(
          com.google.protobuf.Descriptors.OneofDescriptor oneof) {
        return (Builder) super.clearOneof(oneof);
      }
      public Builder setRepeatedField(
          com.google.protobuf.Descriptors.FieldDescriptor field,
          int index, Object value) {
        return (Builder) super.setRepeatedField(field, index, value);
      }
      public Builder addRepeatedField(
          com.google.protobuf.Descriptors.FieldDescriptor field,
          Object value) {
        return (Builder) super.addRepeatedField(field, value);
      }
      public Builder mergeFrom(com.google.protobuf.Message other) {
        if (other instanceof com.linbit.drbdmanage.proto.MsgCrtNodeOuterClass.MsgCrtNode) {
          return mergeFrom((com.linbit.drbdmanage.proto.MsgCrtNodeOuterClass.MsgCrtNode)other);
        } else {
          super.mergeFrom(other);
          return this;
        }
      }

      public Builder mergeFrom(com.linbit.drbdmanage.proto.MsgCrtNodeOuterClass.MsgCrtNode other) {
        if (other == com.linbit.drbdmanage.proto.MsgCrtNodeOuterClass.MsgCrtNode.getDefaultInstance()) return this;
        if (!other.getNodeName().isEmpty()) {
          nodeName_ = other.nodeName_;
          onChanged();
        }
        if (!other.getNodeType().isEmpty()) {
          nodeType_ = other.nodeType_;
          onChanged();
        }
        internalGetMutableNodeProps().mergeFrom(
            other.internalGetNodeProps());
        onChanged();
        return this;
      }

      public final boolean isInitialized() {
        return true;
      }

      public Builder mergeFrom(
          com.google.protobuf.CodedInputStream input,
          com.google.protobuf.ExtensionRegistryLite extensionRegistry)
          throws java.io.IOException {
        com.linbit.drbdmanage.proto.MsgCrtNodeOuterClass.MsgCrtNode parsedMessage = null;
        try {
          parsedMessage = PARSER.parsePartialFrom(input, extensionRegistry);
        } catch (com.google.protobuf.InvalidProtocolBufferException e) {
          parsedMessage = (com.linbit.drbdmanage.proto.MsgCrtNodeOuterClass.MsgCrtNode) e.getUnfinishedMessage();
          throw e.unwrapIOException();
        } finally {
          if (parsedMessage != null) {
            mergeFrom(parsedMessage);
          }
        }
        return this;
      }
      private int bitField0_;

      private java.lang.Object nodeName_ = "";
      /**
       * <pre>
       * Node name
       * </pre>
       *
       * <code>string node_name = 1;</code>
       */
      public java.lang.String getNodeName() {
        java.lang.Object ref = nodeName_;
        if (!(ref instanceof java.lang.String)) {
          com.google.protobuf.ByteString bs =
              (com.google.protobuf.ByteString) ref;
          java.lang.String s = bs.toStringUtf8();
          nodeName_ = s;
          return s;
        } else {
          return (java.lang.String) ref;
        }
      }
      /**
       * <pre>
       * Node name
       * </pre>
       *
       * <code>string node_name = 1;</code>
       */
      public com.google.protobuf.ByteString
          getNodeNameBytes() {
        java.lang.Object ref = nodeName_;
        if (ref instanceof String) {
          com.google.protobuf.ByteString b = 
              com.google.protobuf.ByteString.copyFromUtf8(
                  (java.lang.String) ref);
          nodeName_ = b;
          return b;
        } else {
          return (com.google.protobuf.ByteString) ref;
        }
      }
      /**
       * <pre>
       * Node name
       * </pre>
       *
       * <code>string node_name = 1;</code>
       */
      public Builder setNodeName(
          java.lang.String value) {
        if (value == null) {
    throw new NullPointerException();
  }
  
        nodeName_ = value;
        onChanged();
        return this;
      }
      /**
       * <pre>
       * Node name
       * </pre>
       *
       * <code>string node_name = 1;</code>
       */
      public Builder clearNodeName() {
        
        nodeName_ = getDefaultInstance().getNodeName();
        onChanged();
        return this;
      }
      /**
       * <pre>
       * Node name
       * </pre>
       *
       * <code>string node_name = 1;</code>
       */
      public Builder setNodeNameBytes(
          com.google.protobuf.ByteString value) {
        if (value == null) {
    throw new NullPointerException();
  }
  checkByteStringIsUtf8(value);
        
        nodeName_ = value;
        onChanged();
        return this;
      }

      private java.lang.Object nodeType_ = "";
      /**
       * <pre>
       * Node type
       * </pre>
       *
       * <code>string node_type = 2;</code>
       */
      public java.lang.String getNodeType() {
        java.lang.Object ref = nodeType_;
        if (!(ref instanceof java.lang.String)) {
          com.google.protobuf.ByteString bs =
              (com.google.protobuf.ByteString) ref;
          java.lang.String s = bs.toStringUtf8();
          nodeType_ = s;
          return s;
        } else {
          return (java.lang.String) ref;
        }
      }
      /**
       * <pre>
       * Node type
       * </pre>
       *
       * <code>string node_type = 2;</code>
       */
      public com.google.protobuf.ByteString
          getNodeTypeBytes() {
        java.lang.Object ref = nodeType_;
        if (ref instanceof String) {
          com.google.protobuf.ByteString b = 
              com.google.protobuf.ByteString.copyFromUtf8(
                  (java.lang.String) ref);
          nodeType_ = b;
          return b;
        } else {
          return (com.google.protobuf.ByteString) ref;
        }
      }
      /**
       * <pre>
       * Node type
       * </pre>
       *
       * <code>string node_type = 2;</code>
       */
      public Builder setNodeType(
          java.lang.String value) {
        if (value == null) {
    throw new NullPointerException();
  }
  
        nodeType_ = value;
        onChanged();
        return this;
      }
      /**
       * <pre>
       * Node type
       * </pre>
       *
       * <code>string node_type = 2;</code>
       */
      public Builder clearNodeType() {
        
        nodeType_ = getDefaultInstance().getNodeType();
        onChanged();
        return this;
      }
      /**
       * <pre>
       * Node type
       * </pre>
       *
       * <code>string node_type = 2;</code>
       */
      public Builder setNodeTypeBytes(
          com.google.protobuf.ByteString value) {
        if (value == null) {
    throw new NullPointerException();
  }
  checkByteStringIsUtf8(value);
        
        nodeType_ = value;
        onChanged();
        return this;
      }

      private com.google.protobuf.MapField<
          java.lang.String, java.lang.String> nodeProps_;
      private com.google.protobuf.MapField<java.lang.String, java.lang.String>
      internalGetNodeProps() {
        if (nodeProps_ == null) {
          return com.google.protobuf.MapField.emptyMapField(
              NodePropsDefaultEntryHolder.defaultEntry);
        }
        return nodeProps_;
      }
      private com.google.protobuf.MapField<java.lang.String, java.lang.String>
      internalGetMutableNodeProps() {
        onChanged();;
        if (nodeProps_ == null) {
          nodeProps_ = com.google.protobuf.MapField.newMapField(
              NodePropsDefaultEntryHolder.defaultEntry);
        }
        if (!nodeProps_.isMutable()) {
          nodeProps_ = nodeProps_.copy();
        }
        return nodeProps_;
      }

      public int getNodePropsCount() {
        return internalGetNodeProps().getMap().size();
      }
      /**
       * <pre>
       * Node properties map
       * </pre>
       *
       * <code>map&lt;string, string&gt; node_props = 3;</code>
       */

      public boolean containsNodeProps(
          java.lang.String key) {
        if (key == null) { throw new java.lang.NullPointerException(); }
        return internalGetNodeProps().getMap().containsKey(key);
      }
      /**
       * Use {@link #getNodePropsMap()} instead.
       */
      @java.lang.Deprecated
      public java.util.Map<java.lang.String, java.lang.String> getNodeProps() {
        return getNodePropsMap();
      }
      /**
       * <pre>
       * Node properties map
       * </pre>
       *
       * <code>map&lt;string, string&gt; node_props = 3;</code>
       */

      public java.util.Map<java.lang.String, java.lang.String> getNodePropsMap() {
        return internalGetNodeProps().getMap();
      }
      /**
       * <pre>
       * Node properties map
       * </pre>
       *
       * <code>map&lt;string, string&gt; node_props = 3;</code>
       */

      public java.lang.String getNodePropsOrDefault(
          java.lang.String key,
          java.lang.String defaultValue) {
        if (key == null) { throw new java.lang.NullPointerException(); }
        java.util.Map<java.lang.String, java.lang.String> map =
            internalGetNodeProps().getMap();
        return map.containsKey(key) ? map.get(key) : defaultValue;
      }
      /**
       * <pre>
       * Node properties map
       * </pre>
       *
       * <code>map&lt;string, string&gt; node_props = 3;</code>
       */

      public java.lang.String getNodePropsOrThrow(
          java.lang.String key) {
        if (key == null) { throw new java.lang.NullPointerException(); }
        java.util.Map<java.lang.String, java.lang.String> map =
            internalGetNodeProps().getMap();
        if (!map.containsKey(key)) {
          throw new java.lang.IllegalArgumentException();
        }
        return map.get(key);
      }

      public Builder clearNodeProps() {
        internalGetMutableNodeProps().getMutableMap()
            .clear();
        return this;
      }
      /**
       * <pre>
       * Node properties map
       * </pre>
       *
       * <code>map&lt;string, string&gt; node_props = 3;</code>
       */

      public Builder removeNodeProps(
          java.lang.String key) {
        if (key == null) { throw new java.lang.NullPointerException(); }
        internalGetMutableNodeProps().getMutableMap()
            .remove(key);
        return this;
      }
      /**
       * Use alternate mutation accessors instead.
       */
      @java.lang.Deprecated
      public java.util.Map<java.lang.String, java.lang.String>
      getMutableNodeProps() {
        return internalGetMutableNodeProps().getMutableMap();
      }
      /**
       * <pre>
       * Node properties map
       * </pre>
       *
       * <code>map&lt;string, string&gt; node_props = 3;</code>
       */
      public Builder putNodeProps(
          java.lang.String key,
          java.lang.String value) {
        if (key == null) { throw new java.lang.NullPointerException(); }
        if (value == null) { throw new java.lang.NullPointerException(); }
        internalGetMutableNodeProps().getMutableMap()
            .put(key, value);
        return this;
      }
      /**
       * <pre>
       * Node properties map
       * </pre>
       *
       * <code>map&lt;string, string&gt; node_props = 3;</code>
       */

      public Builder putAllNodeProps(
          java.util.Map<java.lang.String, java.lang.String> values) {
        internalGetMutableNodeProps().getMutableMap()
            .putAll(values);
        return this;
      }
      public final Builder setUnknownFields(
          final com.google.protobuf.UnknownFieldSet unknownFields) {
        return this;
      }

      public final Builder mergeUnknownFields(
          final com.google.protobuf.UnknownFieldSet unknownFields) {
        return this;
      }


      // @@protoc_insertion_point(builder_scope:com.linbit.drbdmanage.proto.MsgCrtNode)
    }

    // @@protoc_insertion_point(class_scope:com.linbit.drbdmanage.proto.MsgCrtNode)
    private static final com.linbit.drbdmanage.proto.MsgCrtNodeOuterClass.MsgCrtNode DEFAULT_INSTANCE;
    static {
      DEFAULT_INSTANCE = new com.linbit.drbdmanage.proto.MsgCrtNodeOuterClass.MsgCrtNode();
    }

    public static com.linbit.drbdmanage.proto.MsgCrtNodeOuterClass.MsgCrtNode getDefaultInstance() {
      return DEFAULT_INSTANCE;
    }

    private static final com.google.protobuf.Parser<MsgCrtNode>
        PARSER = new com.google.protobuf.AbstractParser<MsgCrtNode>() {
      public MsgCrtNode parsePartialFrom(
          com.google.protobuf.CodedInputStream input,
          com.google.protobuf.ExtensionRegistryLite extensionRegistry)
          throws com.google.protobuf.InvalidProtocolBufferException {
          return new MsgCrtNode(input, extensionRegistry);
      }
    };

    public static com.google.protobuf.Parser<MsgCrtNode> parser() {
      return PARSER;
    }

    @java.lang.Override
    public com.google.protobuf.Parser<MsgCrtNode> getParserForType() {
      return PARSER;
    }

    public com.linbit.drbdmanage.proto.MsgCrtNodeOuterClass.MsgCrtNode getDefaultInstanceForType() {
      return DEFAULT_INSTANCE;
    }

  }

  private static final com.google.protobuf.Descriptors.Descriptor
    internal_static_com_linbit_drbdmanage_proto_MsgCrtNode_descriptor;
  private static final 
    com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internal_static_com_linbit_drbdmanage_proto_MsgCrtNode_fieldAccessorTable;
  private static final com.google.protobuf.Descriptors.Descriptor
    internal_static_com_linbit_drbdmanage_proto_MsgCrtNode_NodePropsEntry_descriptor;
  private static final 
    com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internal_static_com_linbit_drbdmanage_proto_MsgCrtNode_NodePropsEntry_fieldAccessorTable;

  public static com.google.protobuf.Descriptors.FileDescriptor
      getDescriptor() {
    return descriptor;
  }
  private static  com.google.protobuf.Descriptors.FileDescriptor
      descriptor;
  static {
    java.lang.String[] descriptorData = {
      "\n\020MsgCrtNode.proto\022\033com.linbit.drbdmanag" +
      "e.proto\"\260\001\n\nMsgCrtNode\022\021\n\tnode_name\030\001 \001(" +
      "\t\022\021\n\tnode_type\030\002 \001(\t\022J\n\nnode_props\030\003 \003(\013" +
      "26.com.linbit.drbdmanage.proto.MsgCrtNod" +
      "e.NodePropsEntry\0320\n\016NodePropsEntry\022\013\n\003ke" +
      "y\030\001 \001(\t\022\r\n\005value\030\002 \001(\t:\0028\001b\006proto3"
    };
    com.google.protobuf.Descriptors.FileDescriptor.InternalDescriptorAssigner assigner =
        new com.google.protobuf.Descriptors.FileDescriptor.    InternalDescriptorAssigner() {
          public com.google.protobuf.ExtensionRegistry assignDescriptors(
              com.google.protobuf.Descriptors.FileDescriptor root) {
            descriptor = root;
            return null;
          }
        };
    com.google.protobuf.Descriptors.FileDescriptor
      .internalBuildGeneratedFileFrom(descriptorData,
        new com.google.protobuf.Descriptors.FileDescriptor[] {
        }, assigner);
    internal_static_com_linbit_drbdmanage_proto_MsgCrtNode_descriptor =
      getDescriptor().getMessageTypes().get(0);
    internal_static_com_linbit_drbdmanage_proto_MsgCrtNode_fieldAccessorTable = new
      com.google.protobuf.GeneratedMessageV3.FieldAccessorTable(
        internal_static_com_linbit_drbdmanage_proto_MsgCrtNode_descriptor,
        new java.lang.String[] { "NodeName", "NodeType", "NodeProps", });
    internal_static_com_linbit_drbdmanage_proto_MsgCrtNode_NodePropsEntry_descriptor =
      internal_static_com_linbit_drbdmanage_proto_MsgCrtNode_descriptor.getNestedTypes().get(0);
    internal_static_com_linbit_drbdmanage_proto_MsgCrtNode_NodePropsEntry_fieldAccessorTable = new
      com.google.protobuf.GeneratedMessageV3.FieldAccessorTable(
        internal_static_com_linbit_drbdmanage_proto_MsgCrtNode_NodePropsEntry_descriptor,
        new java.lang.String[] { "Key", "Value", });
  }

  // @@protoc_insertion_point(outer_class_scope)
}
