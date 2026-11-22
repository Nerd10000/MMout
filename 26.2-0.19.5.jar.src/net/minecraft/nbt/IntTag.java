/*     */ package net.minecraft.nbt;
/*     */ 
/*     */ import java.io.DataInput;
/*     */ import java.io.DataOutput;
/*     */ import java.io.IOException;
/*     */ 
/*     */ public final class IntTag extends Record implements NumericTag {
/*     */   private final int value;
/*     */   
/*     */   private static final int SELF_SIZE_IN_BYTES = 12;
/*     */   
/*     */   public final int hashCode() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/nbt/IntTag;)I
/*     */     //   6: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #7	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/nbt/IntTag;
/*     */   }
/*     */   
/*     */   public final boolean equals(Object o) {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: aload_1
/*     */     //   2: <illegal opcode> equals : (Lnet/minecraft/nbt/IntTag;Ljava/lang/Object;)Z
/*     */     //   7: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #7	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	8	0	this	Lnet/minecraft/nbt/IntTag;
/*     */     //   0	8	1	o	Ljava/lang/Object;
/*     */   }
/*     */   
/*     */   public int value() {
/*   7 */     return this.value;
/*     */   }
/*     */   
/*     */   private static class Cache {
/*     */     private static final int HIGH = 1024;
/*     */     
/*     */     private static final int LOW = -128;
/*     */     
/*  18 */     private static final IntTag[] cache = new IntTag[1153];
/*     */     
/*     */     static {
/*  21 */       for (int i = 0; i < cache.length; i++)
/*  22 */         cache[i] = new IntTag(-128 + i); 
/*     */     }
/*     */   }
/*     */   
/*  27 */   public static final TagType<IntTag> TYPE = new TagType.StaticSize<IntTag>() {
/*     */       public IntTag load(DataInput input, NbtAccounter accounter) throws IOException {
/*  30 */         return IntTag.valueOf(readAccounted(input, accounter));
/*     */       }
/*     */       
/*     */       public StreamTagVisitor.ValueResult parse(DataInput input, StreamTagVisitor output, NbtAccounter accounter) throws IOException {
/*  35 */         return output.visit(readAccounted(input, accounter));
/*     */       }
/*     */       
/*     */       private static int readAccounted(DataInput input, NbtAccounter accounter) throws IOException {
/*  39 */         accounter.accountBytes(12L);
/*  40 */         return input.readInt();
/*     */       }
/*     */       
/*     */       public int size() {
/*  45 */         return 4;
/*     */       }
/*     */       
/*     */       public String getName() {
/*  50 */         return "INT";
/*     */       }
/*     */       
/*     */       public String getPrettyName() {
/*  55 */         return "TAG_Int";
/*     */       }
/*     */     };
/*     */   
/*     */   @Deprecated(forRemoval = true)
/*     */   public IntTag(int value) {
/*  61 */     this.value = value;
/*     */   }
/*     */   
/*     */   public static IntTag valueOf(int i) {
/*  65 */     if (i >= -128 && i <= 1024)
/*  66 */       return Cache.cache[i - -128]; 
/*  68 */     return new IntTag(i);
/*     */   }
/*     */   
/*     */   public void write(DataOutput output) throws IOException {
/*  73 */     output.writeInt(this.value);
/*     */   }
/*     */   
/*     */   public int sizeInBytes() {
/*  78 */     return 12;
/*     */   }
/*     */   
/*     */   public byte getId() {
/*  83 */     return 3;
/*     */   }
/*     */   
/*     */   public TagType<IntTag> getType() {
/*  88 */     return TYPE;
/*     */   }
/*     */   
/*     */   public IntTag copy() {
/*  93 */     return this;
/*     */   }
/*     */   
/*     */   public void accept(TagVisitor visitor) {
/*  98 */     visitor.visitInt(this);
/*     */   }
/*     */   
/*     */   public long longValue() {
/* 103 */     return this.value;
/*     */   }
/*     */   
/*     */   public int intValue() {
/* 108 */     return this.value;
/*     */   }
/*     */   
/*     */   public short shortValue() {
/* 113 */     return (short)(this.value & 0xFFFF);
/*     */   }
/*     */   
/*     */   public byte byteValue() {
/* 118 */     return (byte)(this.value & 0xFF);
/*     */   }
/*     */   
/*     */   public double doubleValue() {
/* 123 */     return this.value;
/*     */   }
/*     */   
/*     */   public float floatValue() {
/* 128 */     return this.value;
/*     */   }
/*     */   
/*     */   public Number box() {
/* 133 */     return Integer.valueOf(this.value);
/*     */   }
/*     */   
/*     */   public StreamTagVisitor.ValueResult accept(StreamTagVisitor visitor) {
/* 138 */     return visitor.visit(this.value);
/*     */   }
/*     */   
/*     */   public String toString() {
/* 143 */     StringTagVisitor visitor = new StringTagVisitor();
/* 144 */     visitor.visitInt(this);
/* 145 */     return visitor.build();
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\nbt\IntTag.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */