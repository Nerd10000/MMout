/*     */ package net.minecraft.nbt;
/*     */ 
/*     */ import java.io.DataInput;
/*     */ import java.io.DataOutput;
/*     */ import java.io.IOException;
/*     */ 
/*     */ public final class LongTag extends Record implements NumericTag {
/*     */   private final long value;
/*     */   
/*     */   private static final int SELF_SIZE_IN_BYTES = 16;
/*     */   
/*     */   public final int hashCode() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/nbt/LongTag;)I
/*     */     //   6: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #7	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/nbt/LongTag;
/*     */   }
/*     */   
/*     */   public final boolean equals(Object o) {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: aload_1
/*     */     //   2: <illegal opcode> equals : (Lnet/minecraft/nbt/LongTag;Ljava/lang/Object;)Z
/*     */     //   7: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #7	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	8	0	this	Lnet/minecraft/nbt/LongTag;
/*     */     //   0	8	1	o	Ljava/lang/Object;
/*     */   }
/*     */   
/*     */   public long value() {
/*   7 */     return this.value;
/*     */   }
/*     */   
/*     */   private static class Cache {
/*     */     private static final int HIGH = 1024;
/*     */     
/*     */     private static final int LOW = -128;
/*     */     
/*  18 */     private static final LongTag[] cache = new LongTag[1153];
/*     */     
/*     */     static {
/*  21 */       for (int i = 0; i < cache.length; i++)
/*  22 */         cache[i] = new LongTag((-128 + i)); 
/*     */     }
/*     */   }
/*     */   
/*  27 */   public static final TagType<LongTag> TYPE = new TagType.StaticSize<LongTag>() {
/*     */       public LongTag load(DataInput input, NbtAccounter accounter) throws IOException {
/*  30 */         return LongTag.valueOf(readAccounted(input, accounter));
/*     */       }
/*     */       
/*     */       public StreamTagVisitor.ValueResult parse(DataInput input, StreamTagVisitor output, NbtAccounter accounter) throws IOException {
/*  35 */         return output.visit(readAccounted(input, accounter));
/*     */       }
/*     */       
/*     */       private static long readAccounted(DataInput input, NbtAccounter accounter) throws IOException {
/*  39 */         accounter.accountBytes(16L);
/*  40 */         return input.readLong();
/*     */       }
/*     */       
/*     */       public int size() {
/*  45 */         return 8;
/*     */       }
/*     */       
/*     */       public String getName() {
/*  50 */         return "LONG";
/*     */       }
/*     */       
/*     */       public String getPrettyName() {
/*  55 */         return "TAG_Long";
/*     */       }
/*     */     };
/*     */   
/*     */   @Deprecated(forRemoval = true)
/*     */   public LongTag(long value) {
/*  61 */     this.value = value;
/*     */   }
/*     */   
/*     */   public static LongTag valueOf(long i) {
/*  65 */     if (i >= -128L && i <= 1024L)
/*  66 */       return Cache.cache[(int)i - -128]; 
/*  68 */     return new LongTag(i);
/*     */   }
/*     */   
/*     */   public void write(DataOutput output) throws IOException {
/*  73 */     output.writeLong(this.value);
/*     */   }
/*     */   
/*     */   public int sizeInBytes() {
/*  78 */     return 16;
/*     */   }
/*     */   
/*     */   public byte getId() {
/*  83 */     return 4;
/*     */   }
/*     */   
/*     */   public TagType<LongTag> getType() {
/*  88 */     return TYPE;
/*     */   }
/*     */   
/*     */   public LongTag copy() {
/*  93 */     return this;
/*     */   }
/*     */   
/*     */   public void accept(TagVisitor visitor) {
/*  98 */     visitor.visitLong(this);
/*     */   }
/*     */   
/*     */   public long longValue() {
/* 103 */     return this.value;
/*     */   }
/*     */   
/*     */   public int intValue() {
/* 108 */     return (int)(this.value & 0xFFFFFFFFFFFFFFFFL);
/*     */   }
/*     */   
/*     */   public short shortValue() {
/* 113 */     return (short)(int)(this.value & 0xFFFFL);
/*     */   }
/*     */   
/*     */   public byte byteValue() {
/* 118 */     return (byte)(int)(this.value & 0xFFL);
/*     */   }
/*     */   
/*     */   public double doubleValue() {
/* 123 */     return this.value;
/*     */   }
/*     */   
/*     */   public float floatValue() {
/* 128 */     return (float)this.value;
/*     */   }
/*     */   
/*     */   public Number box() {
/* 133 */     return Long.valueOf(this.value);
/*     */   }
/*     */   
/*     */   public StreamTagVisitor.ValueResult accept(StreamTagVisitor visitor) {
/* 138 */     return visitor.visit(this.value);
/*     */   }
/*     */   
/*     */   public String toString() {
/* 143 */     StringTagVisitor visitor = new StringTagVisitor();
/* 144 */     visitor.visitLong(this);
/* 145 */     return visitor.build();
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\nbt\LongTag.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */