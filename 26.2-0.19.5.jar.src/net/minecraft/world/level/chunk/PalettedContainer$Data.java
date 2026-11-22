/*     */ package net.minecraft.world.level.chunk;
/*     */ 
/*     */ import net.minecraft.core.IdMap;
/*     */ import net.minecraft.network.FriendlyByteBuf;
/*     */ import net.minecraft.util.BitStorage;
/*     */ 
/*     */ final class Data<T> extends Record {
/*     */   private final Configuration configuration;
/*     */   
/*     */   private final BitStorage storage;
/*     */   
/*     */   private final Palette<T> palette;
/*     */   
/*     */   private Data(Configuration configuration, BitStorage storage, Palette<T> palette) {
/* 327 */     this.configuration = configuration;
/* 327 */     this.storage = storage;
/* 327 */     this.palette = palette;
/*     */   }
/*     */   
/*     */   public final String toString() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/level/chunk/PalettedContainer$Data;)Ljava/lang/String;
/*     */     //   6: areturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #327	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/world/level/chunk/PalettedContainer$Data;
/*     */     // Local variable type table:
/*     */     //   start	length	slot	name	signature
/*     */     //   0	7	0	this	Lnet/minecraft/world/level/chunk/PalettedContainer$Data<TT;>;
/*     */   }
/*     */   
/*     */   public final int hashCode() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/level/chunk/PalettedContainer$Data;)I
/*     */     //   6: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #327	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/world/level/chunk/PalettedContainer$Data;
/*     */     // Local variable type table:
/*     */     //   start	length	slot	name	signature
/*     */     //   0	7	0	this	Lnet/minecraft/world/level/chunk/PalettedContainer$Data<TT;>;
/*     */   }
/*     */   
/*     */   public final boolean equals(Object o) {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: aload_1
/*     */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/level/chunk/PalettedContainer$Data;Ljava/lang/Object;)Z
/*     */     //   7: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #327	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	8	0	this	Lnet/minecraft/world/level/chunk/PalettedContainer$Data;
/*     */     //   0	8	1	o	Ljava/lang/Object;
/*     */     // Local variable type table:
/*     */     //   start	length	slot	name	signature
/*     */     //   0	8	0	this	Lnet/minecraft/world/level/chunk/PalettedContainer$Data<TT;>;
/*     */   }
/*     */   
/*     */   public Configuration configuration() {
/* 327 */     return this.configuration;
/*     */   }
/*     */   
/*     */   public BitStorage storage() {
/* 327 */     return this.storage;
/*     */   }
/*     */   
/*     */   public Palette<T> palette() {
/* 327 */     return this.palette;
/*     */   }
/*     */   
/*     */   public void copyFrom(Palette<T> oldPalette, BitStorage oldStorage) {
/* 334 */     PaletteResize<T> dummyResizer = PaletteResize.noResizeExpected();
/* 335 */     for (int i = 0; i < oldStorage.getSize(); i++) {
/* 336 */       T value = oldPalette.valueFor(oldStorage.get(i));
/* 337 */       this.storage.set(i, this.palette.idFor(value, dummyResizer));
/*     */     } 
/*     */   }
/*     */   
/*     */   public int getSerializedSize(IdMap<T> globalMap) {
/* 342 */     return 1 + this.palette.getSerializedSize(globalMap) + (this.storage.getRaw()).length * 8;
/*     */   }
/*     */   
/*     */   public void write(FriendlyByteBuf buffer, IdMap<T> globalMap) {
/* 346 */     buffer.writeByte(this.storage.getBits());
/* 347 */     this.palette.write(buffer, globalMap);
/* 348 */     buffer.writeFixedSizeLongArray(this.storage.getRaw());
/*     */   }
/*     */   
/*     */   public Data<T> copy() {
/* 352 */     return new Data(this.configuration, this.storage.copy(), this.palette.copy());
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\chunk\PalettedContainer$Data.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */