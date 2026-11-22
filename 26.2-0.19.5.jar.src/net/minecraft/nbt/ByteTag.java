/*     */ package net.minecraft.nbt;
/*     */ 
/*     */ import java.io.DataInput;
/*     */ import java.io.DataOutput;
/*     */ import java.io.IOException;
/*     */ 
/*     */ public final class ByteTag extends Record implements NumericTag {
/*     */   private final byte value;
/*     */   
/*     */   private static final int SELF_SIZE_IN_BYTES = 9;
/*     */   
/*     */   public final int hashCode() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/nbt/ByteTag;)I
/*     */     //   6: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #7	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/nbt/ByteTag;
/*     */   }
/*     */   
/*     */   public final boolean equals(Object o) {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: aload_1
/*     */     //   2: <illegal opcode> equals : (Lnet/minecraft/nbt/ByteTag;Ljava/lang/Object;)Z
/*     */     //   7: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #7	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	8	0	this	Lnet/minecraft/nbt/ByteTag;
/*     */     //   0	8	1	o	Ljava/lang/Object;
/*     */   }
/*     */   
/*     */   public byte value() {
/*   7 */     return this.value;
/*     */   }
/*     */   
/*     */   private static class Cache {
/*  16 */     private static final ByteTag[] cache = new ByteTag[256];
/*     */     
/*     */     static {
/*  19 */       for (int i = 0; i < cache.length; i++)
/*  20 */         cache[i] = new ByteTag((byte)(i - 128)); 
/*     */     }
/*     */   }
/*     */   
/*  25 */   public static final TagType<ByteTag> TYPE = new TagType.StaticSize<ByteTag>() {
/*     */       public ByteTag load(DataInput input, NbtAccounter accounter) throws IOException {
/*  28 */         return ByteTag.valueOf(readAccounted(input, accounter));
/*     */       }
/*     */       
/*     */       public StreamTagVisitor.ValueResult parse(DataInput input, StreamTagVisitor output, NbtAccounter accounter) throws IOException {
/*  33 */         return output.visit(readAccounted(input, accounter));
/*     */       }
/*     */       
/*     */       private static byte readAccounted(DataInput input, NbtAccounter accounter) throws IOException {
/*  37 */         accounter.accountBytes(9L);
/*  38 */         return input.readByte();
/*     */       }
/*     */       
/*     */       public int size() {
/*  43 */         return 1;
/*     */       }
/*     */       
/*     */       public String getName() {
/*  48 */         return "BYTE";
/*     */       }
/*     */       
/*     */       public String getPrettyName() {
/*  53 */         return "TAG_Byte";
/*     */       }
/*     */     };
/*     */   
/*  57 */   public static final ByteTag ZERO = valueOf((byte)0);
/*     */   
/*  58 */   public static final ByteTag ONE = valueOf((byte)1);
/*     */   
/*     */   @Deprecated(forRemoval = true)
/*     */   public ByteTag(byte value) {
/*  62 */     this.value = value;
/*     */   }
/*     */   
/*     */   public static ByteTag valueOf(byte data) {
/*  66 */     return Cache.cache[128 + data];
/*     */   }
/*     */   
/*     */   public static ByteTag valueOf(boolean data) {
/*  70 */     return data ? ONE : ZERO;
/*     */   }
/*     */   
/*     */   public void write(DataOutput output) throws IOException {
/*  75 */     output.writeByte(this.value);
/*     */   }
/*     */   
/*     */   public int sizeInBytes() {
/*  80 */     return 9;
/*     */   }
/*     */   
/*     */   public byte getId() {
/*  85 */     return 1;
/*     */   }
/*     */   
/*     */   public TagType<ByteTag> getType() {
/*  90 */     return TYPE;
/*     */   }
/*     */   
/*     */   public ByteTag copy() {
/*  95 */     return this;
/*     */   }
/*     */   
/*     */   public void accept(TagVisitor visitor) {
/* 100 */     visitor.visitByte(this);
/*     */   }
/*     */   
/*     */   public long longValue() {
/* 105 */     return this.value;
/*     */   }
/*     */   
/*     */   public int intValue() {
/* 110 */     return this.value;
/*     */   }
/*     */   
/*     */   public short shortValue() {
/* 115 */     return (short)this.value;
/*     */   }
/*     */   
/*     */   public byte byteValue() {
/* 120 */     return this.value;
/*     */   }
/*     */   
/*     */   public double doubleValue() {
/* 125 */     return this.value;
/*     */   }
/*     */   
/*     */   public float floatValue() {
/* 130 */     return this.value;
/*     */   }
/*     */   
/*     */   public Number box() {
/* 135 */     return Byte.valueOf(this.value);
/*     */   }
/*     */   
/*     */   public StreamTagVisitor.ValueResult accept(StreamTagVisitor visitor) {
/* 140 */     return visitor.visit(this.value);
/*     */   }
/*     */   
/*     */   public String toString() {
/* 145 */     StringTagVisitor visitor = new StringTagVisitor();
/* 146 */     visitor.visitByte(this);
/* 147 */     return visitor.build();
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\nbt\ByteTag.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */