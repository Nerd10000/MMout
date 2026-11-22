/*     */ package net.minecraft.world.level.lighting;
/*     */ 
/*     */ import com.google.common.annotations.VisibleForTesting;
/*     */ import it.unimi.dsi.fastutil.HashCommon;
/*     */ import it.unimi.dsi.fastutil.longs.Long2LongLinkedOpenHashMap;
/*     */ import java.util.NoSuchElementException;
/*     */ import net.minecraft.util.Mth;
/*     */ 
/*     */ @VisibleForTesting
/*     */ class InternalMap extends Long2LongLinkedOpenHashMap {
/*  24 */   private static final int X_BITS = Mth.log2(60000000);
/*     */   
/*  25 */   private static final int Z_BITS = Mth.log2(60000000);
/*     */   
/*  26 */   private static final int Y_BITS = 64 - X_BITS - Z_BITS;
/*     */   
/*     */   private static final int Y_OFFSET = 0;
/*     */   
/*  29 */   private static final int Z_OFFSET = Y_BITS;
/*     */   
/*  30 */   private static final int X_OFFSET = Y_BITS + Z_BITS;
/*     */   
/*  31 */   private static final long OUTER_MASK = 3L << X_OFFSET | 0x3L | 3L << Z_OFFSET;
/*     */   
/*  33 */   private int lastPos = -1;
/*     */   
/*     */   private long lastOuterKey;
/*     */   
/*     */   private final int minSize;
/*     */   
/*     */   public InternalMap(int expected, float f) {
/*  38 */     super(expected, f);
/*  39 */     this.minSize = expected;
/*     */   }
/*     */   
/*     */   @VisibleForTesting
/*     */   static long getOuterKey(long key) {
/*  45 */     return key & (OUTER_MASK ^ 0xFFFFFFFFFFFFFFFFL);
/*     */   }
/*     */   
/*     */   @VisibleForTesting
/*     */   static int getInnerKey(long key) {
/*  51 */     int innerX = (int)(key >>> X_OFFSET & 0x3L);
/*  52 */     int innerY = (int)(key >>> 0L & 0x3L);
/*  53 */     int innerZ = (int)(key >>> Z_OFFSET & 0x3L);
/*  54 */     return innerX << 4 | innerZ << 2 | innerY;
/*     */   }
/*     */   
/*     */   @VisibleForTesting
/*     */   static long getFullKey(long outerKey, int innerKey) {
/*  60 */     outerKey |= (innerKey >>> 4 & 0x3) << X_OFFSET;
/*  61 */     outerKey |= (innerKey >>> 2 & 0x3) << Z_OFFSET;
/*  62 */     outerKey |= (innerKey >>> 0 & 0x3) << 0L;
/*  63 */     return outerKey;
/*     */   }
/*     */   
/*     */   public boolean addBit(long key) {
/*     */     int pos;
/*  67 */     long outerKey = getOuterKey(key);
/*  68 */     int innerKey = getInnerKey(key);
/*  69 */     long bitMask = 1L << innerKey;
/*  71 */     if (outerKey == 0L) {
/*  72 */       if (this.containsNullKey)
/*  73 */         return replaceBit(this.n, bitMask); 
/*  75 */       this.containsNullKey = true;
/*  76 */       pos = this.n;
/*     */     } else {
/*  78 */       if (this.lastPos != -1 && outerKey == this.lastOuterKey)
/*  79 */         return replaceBit(this.lastPos, bitMask); 
/*  81 */       long[] keys = this.key;
/*  82 */       pos = (int)HashCommon.mix(outerKey) & this.mask;
/*  83 */       long curr = keys[pos];
/*  84 */       while (curr != 0L) {
/*  85 */         if (curr == outerKey) {
/*  86 */           this.lastPos = pos;
/*  87 */           this.lastOuterKey = outerKey;
/*  88 */           return replaceBit(pos, bitMask);
/*     */         } 
/*  90 */         pos = pos + 1 & this.mask;
/*  91 */         curr = keys[pos];
/*     */       } 
/*     */     } 
/*  94 */     this.key[pos] = outerKey;
/*  95 */     this.value[pos] = bitMask;
/*  96 */     if (this.size == 0) {
/*  97 */       this.first = this.last = pos;
/*  99 */       this.link[pos] = -1L;
/*     */     } else {
/* 101 */       this.link[this.last] = this.link[this.last] ^ (this.link[this.last] ^ pos & 0xFFFFFFFFL) & 0xFFFFFFFFL;
/* 102 */       this.link[pos] = (this.last & 0xFFFFFFFFL) << 32L | 0xFFFFFFFFL;
/* 103 */       this.last = pos;
/*     */     } 
/* 105 */     if (this.size++ >= this.maxFill)
/* 106 */       rehash(HashCommon.arraySize(this.size + 1, this.f)); 
/* 108 */     return false;
/*     */   }
/*     */   
/*     */   private boolean replaceBit(int pos, long bitMask) {
/* 112 */     boolean oldValue = ((this.value[pos] & bitMask) != 0L);
/* 113 */     this.value[pos] = this.value[pos] | bitMask;
/* 114 */     return oldValue;
/*     */   }
/*     */   
/*     */   public boolean removeBit(long key) {
/* 118 */     long outerKey = getOuterKey(key);
/* 119 */     int innerKey = getInnerKey(key);
/* 120 */     long bitMask = 1L << innerKey;
/* 121 */     if (outerKey == 0L) {
/* 122 */       if (this.containsNullKey)
/* 123 */         return removeFromNullEntry(bitMask); 
/* 125 */       return false;
/*     */     } 
/* 127 */     if (this.lastPos != -1 && outerKey == this.lastOuterKey)
/* 128 */       return removeFromEntry(this.lastPos, bitMask); 
/* 130 */     long[] keys = this.key;
/* 131 */     int pos = (int)HashCommon.mix(outerKey) & this.mask;
/* 132 */     long curr = keys[pos];
/*     */     while (true) {
/* 134 */       if (curr == 0L)
/* 135 */         return false; 
/* 137 */       if (outerKey == curr) {
/* 138 */         this.lastPos = pos;
/* 139 */         this.lastOuterKey = outerKey;
/* 140 */         return removeFromEntry(pos, bitMask);
/*     */       } 
/* 142 */       pos = pos + 1 & this.mask;
/* 143 */       curr = keys[pos];
/*     */     } 
/*     */   }
/*     */   
/*     */   private boolean removeFromNullEntry(long bitMask) {
/* 148 */     if ((this.value[this.n] & bitMask) == 0L)
/* 149 */       return false; 
/* 151 */     this.value[this.n] = this.value[this.n] & (bitMask ^ 0xFFFFFFFFFFFFFFFFL);
/* 152 */     if (this.value[this.n] != 0L)
/* 153 */       return true; 
/* 155 */     this.containsNullKey = false;
/* 156 */     this.size--;
/* 157 */     fixPointers(this.n);
/* 158 */     if (this.size < this.maxFill / 4 && this.n > 16)
/* 159 */       rehash(this.n / 2); 
/* 161 */     return true;
/*     */   }
/*     */   
/*     */   private boolean removeFromEntry(int pos, long bitMask) {
/* 165 */     if ((this.value[pos] & bitMask) == 0L)
/* 166 */       return false; 
/* 168 */     this.value[pos] = this.value[pos] & (bitMask ^ 0xFFFFFFFFFFFFFFFFL);
/* 169 */     if (this.value[pos] != 0L)
/* 170 */       return true; 
/* 172 */     this.lastPos = -1;
/* 173 */     this.size--;
/* 174 */     fixPointers(pos);
/* 175 */     shiftKeys(pos);
/* 176 */     if (this.size < this.maxFill / 4 && this.n > 16)
/* 177 */       rehash(this.n / 2); 
/* 179 */     return true;
/*     */   }
/*     */   
/*     */   public long removeFirstBit() {
/* 183 */     if (this.size == 0)
/* 184 */       throw new NoSuchElementException(); 
/* 186 */     int pos = this.first;
/* 187 */     long outerKey = this.key[pos];
/* 188 */     int innerKey = Long.numberOfTrailingZeros(this.value[pos]);
/* 189 */     this.value[pos] = this.value[pos] & (1L << innerKey ^ 0xFFFFFFFFFFFFFFFFL);
/* 190 */     if (this.value[pos] == 0L) {
/* 191 */       removeFirstLong();
/* 192 */       this.lastPos = -1;
/*     */     } 
/* 194 */     return getFullKey(outerKey, innerKey);
/*     */   }
/*     */   
/*     */   protected void rehash(int newN) {
/* 199 */     if (newN > this.minSize)
/* 200 */       super.rehash(newN); 
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\lighting\SpatialLongSet$InternalMap.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */