package net.minecraft.nbt;

public interface TagVisitor {
  void visitString(StringTag paramStringTag);
  
  void visitByte(ByteTag paramByteTag);
  
  void visitShort(ShortTag paramShortTag);
  
  void visitInt(IntTag paramIntTag);
  
  void visitLong(LongTag paramLongTag);
  
  void visitFloat(FloatTag paramFloatTag);
  
  void visitDouble(DoubleTag paramDoubleTag);
  
  void visitByteArray(ByteArrayTag paramByteArrayTag);
  
  void visitIntArray(IntArrayTag paramIntArrayTag);
  
  void visitLongArray(LongArrayTag paramLongArrayTag);
  
  void visitList(ListTag paramListTag);
  
  void visitCompound(CompoundTag paramCompoundTag);
  
  void visitEnd(EndTag paramEndTag);
}


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\nbt\TagVisitor.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */