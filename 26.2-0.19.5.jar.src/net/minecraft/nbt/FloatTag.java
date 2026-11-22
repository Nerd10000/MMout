/*     */ package net.minecraft.nbt;
/*     */ 
/*     */ import java.io.DataInput;
/*     */ import java.io.DataOutput;
/*     */ import java.io.IOException;
/*     */ import net.minecraft.util.Mth;
/*     */ 
/*     */ public final class FloatTag extends Record implements NumericTag {
/*     */   private final float value;
/*     */   
/*     */   private static final int SELF_SIZE_IN_BYTES = 12;
/*     */   
/*     */   public final int hashCode() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/nbt/FloatTag;)I
/*     */     //   6: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #9	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/nbt/FloatTag;
/*     */   }
/*     */   
/*     */   public final boolean equals(Object o) {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: aload_1
/*     */     //   2: <illegal opcode> equals : (Lnet/minecraft/nbt/FloatTag;Ljava/lang/Object;)Z
/*     */     //   7: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #9	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	8	0	this	Lnet/minecraft/nbt/FloatTag;
/*     */     //   0	8	1	o	Ljava/lang/Object;
/*     */   }
/*     */   
/*     */   public float value() {
/*   9 */     return this.value;
/*     */   }
/*     */   
/*  17 */   public static final FloatTag ZERO = new FloatTag(0.0F);
/*     */   
/*  19 */   public static final TagType<FloatTag> TYPE = new TagType.StaticSize<FloatTag>() {
/*     */       public FloatTag load(DataInput input, NbtAccounter accounter) throws IOException {
/*  22 */         return FloatTag.valueOf(readAccounted(input, accounter));
/*     */       }
/*     */       
/*     */       public StreamTagVisitor.ValueResult parse(DataInput input, StreamTagVisitor output, NbtAccounter accounter) throws IOException {
/*  27 */         return output.visit(readAccounted(input, accounter));
/*     */       }
/*     */       
/*     */       private static float readAccounted(DataInput input, NbtAccounter accounter) throws IOException {
/*  31 */         accounter.accountBytes(12L);
/*  32 */         return input.readFloat();
/*     */       }
/*     */       
/*     */       public int size() {
/*  37 */         return 4;
/*     */       }
/*     */       
/*     */       public String getName() {
/*  42 */         return "FLOAT";
/*     */       }
/*     */       
/*     */       public String getPrettyName() {
/*  47 */         return "TAG_Float";
/*     */       }
/*     */     };
/*     */   
/*     */   @Deprecated(forRemoval = true)
/*     */   public FloatTag(float value) {
/*  53 */     this.value = value;
/*     */   }
/*     */   
/*     */   public static FloatTag valueOf(float data) {
/*  57 */     if (data == 0.0F)
/*  58 */       return ZERO; 
/*  60 */     return new FloatTag(data);
/*     */   }
/*     */   
/*     */   public void write(DataOutput output) throws IOException {
/*  65 */     output.writeFloat(this.value);
/*     */   }
/*     */   
/*     */   public int sizeInBytes() {
/*  70 */     return 12;
/*     */   }
/*     */   
/*     */   public byte getId() {
/*  75 */     return 5;
/*     */   }
/*     */   
/*     */   public TagType<FloatTag> getType() {
/*  80 */     return TYPE;
/*     */   }
/*     */   
/*     */   public FloatTag copy() {
/*  85 */     return this;
/*     */   }
/*     */   
/*     */   public void accept(TagVisitor visitor) {
/*  90 */     visitor.visitFloat(this);
/*     */   }
/*     */   
/*     */   public long longValue() {
/*  95 */     return (long)this.value;
/*     */   }
/*     */   
/*     */   public int intValue() {
/* 100 */     return Mth.floor(this.value);
/*     */   }
/*     */   
/*     */   public short shortValue() {
/* 105 */     return (short)(Mth.floor(this.value) & 0xFFFF);
/*     */   }
/*     */   
/*     */   public byte byteValue() {
/* 110 */     return (byte)(Mth.floor(this.value) & 0xFF);
/*     */   }
/*     */   
/*     */   public double doubleValue() {
/* 115 */     return this.value;
/*     */   }
/*     */   
/*     */   public float floatValue() {
/* 120 */     return this.value;
/*     */   }
/*     */   
/*     */   public Number box() {
/* 125 */     return Float.valueOf(this.value);
/*     */   }
/*     */   
/*     */   public StreamTagVisitor.ValueResult accept(StreamTagVisitor visitor) {
/* 130 */     return visitor.visit(this.value);
/*     */   }
/*     */   
/*     */   public String toString() {
/* 135 */     StringTagVisitor visitor = new StringTagVisitor();
/* 136 */     visitor.visitFloat(this);
/* 137 */     return visitor.build();
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\nbt\FloatTag.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */