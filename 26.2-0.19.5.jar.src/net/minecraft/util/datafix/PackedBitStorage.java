/*    */ package net.minecraft.util.datafix;
/*    */ 
/*    */ import net.minecraft.util.Mth;
/*    */ import org.apache.commons.lang3.Validate;
/*    */ 
/*    */ public class PackedBitStorage {
/*    */   private static final int BIT_TO_LONG_SHIFT = 6;
/*    */   
/*    */   private final long[] data;
/*    */   
/*    */   private final int bits;
/*    */   
/*    */   private final long mask;
/*    */   
/*    */   private final int size;
/*    */   
/*    */   public PackedBitStorage(int bits, int size) {
/* 22 */     this(bits, size, new long[Mth.roundToward(size * bits, 64) / 64]);
/*    */   }
/*    */   
/*    */   public PackedBitStorage(int bits, int size, long[] data) {
/* 26 */     Validate.inclusiveBetween(1L, 32L, bits);
/* 28 */     this.size = size;
/* 29 */     this.bits = bits;
/* 30 */     this.data = data;
/* 31 */     this.mask = (1L << bits) - 1L;
/* 33 */     int requiredLength = Mth.roundToward(size * bits, 64) / 64;
/* 34 */     if (data.length != requiredLength)
/* 35 */       throw new IllegalArgumentException("Invalid length given for storage, got: " + data.length + " but expected: " + requiredLength); 
/*    */   }
/*    */   
/*    */   public void set(int index, int value) {
/* 40 */     Validate.inclusiveBetween(0L, (this.size - 1), index);
/* 41 */     Validate.inclusiveBetween(0L, this.mask, value);
/* 43 */     int position = index * this.bits;
/* 44 */     int startData = position >> 6;
/* 45 */     int endData = (index + 1) * this.bits - 1 >> 6;
/* 46 */     int startBit = position ^ startData << 6;
/* 48 */     this.data[startData] = this.data[startData] & (this.mask << startBit ^ 0xFFFFFFFFFFFFFFFFL) | (value & this.mask) << startBit;
/* 49 */     if (startData != endData) {
/* 50 */       int shiftBits = 64 - startBit;
/* 51 */       int wantedBits = this.bits - shiftBits;
/* 52 */       this.data[endData] = this.data[endData] >>> wantedBits << wantedBits | (value & this.mask) >> shiftBits;
/*    */     } 
/*    */   }
/*    */   
/*    */   public int get(int index) {
/* 57 */     Validate.inclusiveBetween(0L, (this.size - 1), index);
/* 59 */     int position = index * this.bits;
/* 60 */     int startData = position >> 6;
/* 61 */     int endData = (index + 1) * this.bits - 1 >> 6;
/* 62 */     int startBit = position ^ startData << 6;
/* 64 */     if (startData == endData)
/* 65 */       return (int)(this.data[startData] >>> startBit & this.mask); 
/* 67 */     int shiftBits = 64 - startBit;
/* 68 */     return (int)((this.data[startData] >>> startBit | this.data[endData] << shiftBits) & this.mask);
/*    */   }
/*    */   
/*    */   public long[] getRaw() {
/* 73 */     return this.data;
/*    */   }
/*    */   
/*    */   public int getBits() {
/* 77 */     return this.bits;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraf\\util\datafix\PackedBitStorage.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */