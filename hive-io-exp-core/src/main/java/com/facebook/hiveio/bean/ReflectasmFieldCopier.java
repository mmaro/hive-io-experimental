/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.facebook.hiveio.bean;

import com.esotericsoftware.reflectasm.FieldAccess;
import com.facebook.hiveio.record.HiveReadableRecord;
import com.google.common.base.Objects;

public abstract class ReflectasmFieldCopier extends FieldCopier {
  private int toObjectIndex;
  private FieldAccess fieldAccess;

  @Override public String toString() {
    return Objects.toStringHelper(this)
        .add("fromHiveIndex", getFromHiveIndex())
        .add("toObjectIndex", toObjectIndex)
        .add("copierClass", getClass())
        .toString();
  }

  protected int getToObjectIndex() {
    return toObjectIndex;
  }

  void setToObjectIndex(int toObjectIndex) {
    this.toObjectIndex = toObjectIndex;
  }

  protected FieldAccess getFieldAccess() {
    return fieldAccess;
  }

  void setFieldAccess(FieldAccess fieldAccess) {
    this.fieldAccess = fieldAccess;
  }

  public static ReflectasmFieldCopier fromType(Class<?> type) {
    ReflectasmFieldCopier fieldCopier;
    if (type.equals(boolean.class)) {
      fieldCopier = new BooleanFC();
    } else if (type.equals(byte.class)) {
      fieldCopier = new ByteFC();
    } else if (type.equals(short.class)) {
      fieldCopier = new ShortFC();
    } else if (type.equals(int.class)) {
      fieldCopier = new IntFC();
    } else if (type.equals(long.class)) {
      fieldCopier = new LongFC();
    } else if (type.equals(float.class)) {
      fieldCopier = new FloatFC();
    } else if (type.equals(double.class)) {
      fieldCopier = new DoubleFC();
    } else {
      fieldCopier = new ObjectFC();
    }
    return fieldCopier;
  }

  private static class BooleanFC extends ReflectasmFieldCopier {
    @Override
    public void setValue(HiveReadableRecord fromRecord, Object toObject) {
      getFieldAccess().setBoolean(toObject, getToObjectIndex(),
          fromRecord.getBoolean(getFromHiveIndex()));
    }
  }

  private static class ByteFC extends ReflectasmFieldCopier {
    @Override
    public void setValue(HiveReadableRecord fromRecord, Object toObject) {
      getFieldAccess().setByte(toObject, getToObjectIndex(),
          (byte) fromRecord.getLong(getFromHiveIndex()));
    }
  }

  private static class ShortFC extends ReflectasmFieldCopier {
    @Override
    public void setValue(HiveReadableRecord fromRecord, Object toObject) {
      getFieldAccess().setShort(toObject, getToObjectIndex(),
          (short) fromRecord.getLong(getFromHiveIndex()));
    }
  }

  private static class IntFC extends ReflectasmFieldCopier {
    @Override
    public void setValue(HiveReadableRecord fromRecord, Object toObject) {
      getFieldAccess().setInt(toObject, getToObjectIndex(),
          (int) fromRecord.getLong(getFromHiveIndex()));
    }
  }

  private static class LongFC extends ReflectasmFieldCopier {
    @Override
    public void setValue(HiveReadableRecord fromRecord, Object toObject) {
      getFieldAccess().setLong(toObject, getToObjectIndex(),
          fromRecord.getLong(getFromHiveIndex()));
    }
  }

  private static class FloatFC extends ReflectasmFieldCopier {
    @Override
    public void setValue(HiveReadableRecord fromRecord, Object toObject) {
      getFieldAccess().setFloat(toObject, getToObjectIndex(),
          (float) fromRecord.getDouble(getFromHiveIndex()));
    }
  }

  private static class DoubleFC extends ReflectasmFieldCopier {
    @Override
    public void setValue(HiveReadableRecord fromRecord, Object toObject) {
      getFieldAccess().setDouble(toObject, getToObjectIndex(),
          fromRecord.getDouble(getFromHiveIndex()));
    }
  }

  private static class ObjectFC extends ReflectasmFieldCopier {
    @Override
    protected void setValue(HiveReadableRecord fromRecord, Object toObject) {
      getFieldAccess().set(toObject, getToObjectIndex(),
          fromRecord.get(getFromHiveIndex()));
    }
  }
}
