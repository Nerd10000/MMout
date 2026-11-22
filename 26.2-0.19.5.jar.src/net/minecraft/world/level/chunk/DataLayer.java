/*     */ package net.minecraft.world.level.chunk;
/*     */ 
/*     */ import java.util.Arrays;
/*     */ import net.minecraft.util.Util;
/*     */ import net.minecraft.util.VisibleForDebug;
/*     */ 
/*     */ public class DataLayer {
/*     */   public static final int LAYER_COUNT = 16;
/*     */   
/*     */   public static final int LAYER_SIZE = 128;
/*     */   
/*     */   public static final int SIZE = 2048;
/*     */   
/*     */   private static final int NIBBLE_SIZE = 4;
/*     */   
/*     */   protected byte[] data;
/*     */   
/*     */   private int defaultValue;
/*     */   
/*     */   public DataLayer() {
/*  20 */     this(0);
/*     */   }
/*     */   
/*     */   public DataLayer(int defaultValue) {
/*  24 */     this.defaultValue = defaultValue;
/*     */   }
/*     */   
/*     */   public DataLayer(byte[] data) {
/*  28 */     this.data = data;
/*  29 */     this.defaultValue = 0;
/*  31 */     if (data.length != 2048)
/*  32 */       throw (IllegalArgumentException)Util.pauseInIde(new IllegalArgumentException("DataLayer should be 2048 bytes not: " + data.length)); 
/*     */   }
/*     */   
/*     */   public int get(int x, int y, int z) {
/*  37 */     return get(getIndex(x, y, z));
/*     */   }
/*     */   
/*     */   public void set(int x, int y, int z, int val) {
/*  41 */     set(getIndex(x, y, z), val);
/*     */   }
/*     */   
/*     */   private static int getIndex(int x, int y, int z) {
/*  45 */     return y << 8 | z << 4 | x;
/*     */   }
/*     */   
/*     */   private int get(int index) {
/*  49 */     if (this.data == null)
/*  50 */       return this.defaultValue; 
/*  52 */     int position = getByteIndex(index);
/*  53 */     int nibble = getNibbleIndex(index);
/*  54 */     return this.data[position] >> 4 * nibble & 0xF;
/*     */   }
/*     */   
/*     */   private void set(int index, int val) {
/*  58 */     byte[] data = getData();
/*  59 */     int position = getByteIndex(index);
/*  60 */     int nibble = getNibbleIndex(index);
/*  62 */     int mask = 15 << 4 * nibble ^ 0xFFFFFFFF;
/*  63 */     int valueToSet = (val & 0xF) << 4 * nibble;
/*  64 */     data[position] = (byte)(data[position] & mask | valueToSet);
/*     */   }
/*     */   
/*     */   private static int getNibbleIndex(int index) {
/*  68 */     return index & 0x1;
/*     */   }
/*     */   
/*     */   private static int getByteIndex(int position) {
/*  72 */     return position >> 1;
/*     */   }
/*     */   
/*     */   public void fill(int value) {
/*  76 */     this.defaultValue = value;
/*  77 */     this.data = null;
/*     */   }
/*     */   
/*     */   private static byte packFilled(int value) {
/*  81 */     byte packed = (byte)value;
/*  82 */     for (int i = 4; i < 8; i += 4)
/*  83 */       packed = (byte)(packed | value << i); 
/*  85 */     return packed;
/*     */   }
/*     */   
/*     */   public byte[] getData() {
/*  89 */     if (this.data == null) {
/*  90 */       this.data = new byte[2048];
/*  91 */       if (this.defaultValue != 0)
/*  92 */         Arrays.fill(this.data, packFilled(this.defaultValue)); 
/*     */     } 
/*  95 */     return this.data;
/*     */   }
/*     */   
/*     */   public DataLayer copy() {
/*  99 */     if (this.data == null)
/* 100 */       return new DataLayer(this.defaultValue); 
/* 102 */     return new DataLayer((byte[])this.data.clone());
/*     */   }
/*     */   
/*     */   public String toString() {
/* 107 */     StringBuilder builder = new StringBuilder();
/* 108 */     for (int i = 0; i < 4096; i++) {
/* 109 */       builder.append(Integer.toHexString(get(i)));
/* 110 */       if ((i & 0xF) == 15)
/* 111 */         builder.append("\n"); 
/* 113 */       if ((i & 0xFF) == 255)
/* 114 */         builder.append("\n"); 
/*     */     } 
/* 117 */     return builder.toString();
/*     */   }
/*     */   
/*     */   @VisibleForDebug
/*     */   public String layerToString(int layer) {
/* 123 */     StringBuilder builder = new StringBuilder();
/* 124 */     for (int i = 0; i < 256; i++) {
/* 125 */       builder.append(Integer.toHexString(get(i)));
/* 126 */       if ((i & 0xF) == 15)
/* 127 */         builder.append("\n"); 
/*     */     } 
/* 130 */     return builder.toString();
/*     */   }
/*     */   
/*     */   public boolean isDefinitelyHomogenous() {
/* 134 */     return (this.data == null);
/*     */   }
/*     */   
/*     */   public boolean isDefinitelyFilledWith(int value) {
/* 138 */     return (this.data == null && this.defaultValue == value);
/*     */   }
/*     */   
/*     */   public boolean isEmpty() {
/* 142 */     return (this.data == null && this.defaultValue == 0);
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\chunk\DataLayer.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */