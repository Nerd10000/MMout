/*    */ package net.minecraft.world.level.chunk;
/*    */ 
/*    */ import com.google.common.annotations.VisibleForTesting;
/*    */ import com.mojang.serialization.DataResult;
/*    */ import java.util.List;
/*    */ import java.util.Optional;
/*    */ import java.util.function.Consumer;
/*    */ import java.util.function.Predicate;
/*    */ import java.util.stream.LongStream;
/*    */ import net.minecraft.network.FriendlyByteBuf;
/*    */ 
/*    */ public interface PalettedContainerRO<T> {
/*    */   T get(int paramInt1, int paramInt2, int paramInt3);
/*    */   
/*    */   void getAll(Consumer<T> paramConsumer);
/*    */   
/*    */   void write(FriendlyByteBuf paramFriendlyByteBuf);
/*    */   
/*    */   int getSerializedSize();
/*    */   
/*    */   @VisibleForTesting
/*    */   int bitsPerEntry();
/*    */   
/*    */   boolean maybeHas(Predicate<T> paramPredicate);
/*    */   
/*    */   void forEachInPalette(Consumer<T> paramConsumer);
/*    */   
/*    */   void count(PalettedContainer.CountConsumer<T> paramCountConsumer);
/*    */   
/*    */   PalettedContainer<T> copy();
/*    */   
/*    */   PalettedContainer<T> recreate();
/*    */   
/*    */   PackedData<T> pack(Strategy<T> paramStrategy);
/*    */   
/*    */   public static interface Unpacker<T, C extends PalettedContainerRO<T>> {
/*    */     DataResult<C> read(Strategy<T> param1Strategy, PalettedContainerRO.PackedData<T> param1PackedData);
/*    */   }
/*    */   
/*    */   public static final class PackedData<T> extends Record {
/*    */     private final List<T> paletteEntries;
/*    */     
/*    */     private final Optional<LongStream> storage;
/*    */     
/*    */     private final int bitsPerEntry;
/*    */     
/*    */     public static final int UNKNOWN_BITS_PER_ENTRY = -1;
/*    */     
/*    */     public int bitsPerEntry() {
/* 37 */       return this.bitsPerEntry;
/*    */     }
/*    */     
/*    */     public Optional<LongStream> storage() {
/* 37 */       return this.storage;
/*    */     }
/*    */     
/*    */     public List<T> paletteEntries() {
/* 37 */       return this.paletteEntries;
/*    */     }
/*    */     
/*    */     public final boolean equals(Object o) {
/*    */       // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: aload_1
/*    */       //   2: <illegal opcode> equals : (Lnet/minecraft/world/level/chunk/PalettedContainerRO$PackedData;Ljava/lang/Object;)Z
/*    */       //   7: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #37	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	8	0	this	Lnet/minecraft/world/level/chunk/PalettedContainerRO$PackedData;
/*    */       //   0	8	1	o	Ljava/lang/Object;
/*    */       // Local variable type table:
/*    */       //   start	length	slot	name	signature
/*    */       //   0	8	0	this	Lnet/minecraft/world/level/chunk/PalettedContainerRO$PackedData<TT;>;
/*    */     }
/*    */     
/*    */     public final int hashCode() {
/*    */       // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/level/chunk/PalettedContainerRO$PackedData;)I
/*    */       //   6: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #37	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	7	0	this	Lnet/minecraft/world/level/chunk/PalettedContainerRO$PackedData;
/*    */       // Local variable type table:
/*    */       //   start	length	slot	name	signature
/*    */       //   0	7	0	this	Lnet/minecraft/world/level/chunk/PalettedContainerRO$PackedData<TT;>;
/*    */     }
/*    */     
/*    */     public final String toString() {
/*    */       // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> toString : (Lnet/minecraft/world/level/chunk/PalettedContainerRO$PackedData;)Ljava/lang/String;
/*    */       //   6: areturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #37	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	7	0	this	Lnet/minecraft/world/level/chunk/PalettedContainerRO$PackedData;
/*    */       // Local variable type table:
/*    */       //   start	length	slot	name	signature
/*    */       //   0	7	0	this	Lnet/minecraft/world/level/chunk/PalettedContainerRO$PackedData<TT;>;
/*    */     }
/*    */     
/*    */     public PackedData(List<T> paletteEntries, Optional<LongStream> storage, int bitsPerEntry) {
/* 37 */       this.paletteEntries = paletteEntries;
/* 37 */       this.storage = storage;
/* 37 */       this.bitsPerEntry = bitsPerEntry;
/*    */     }
/*    */     
/*    */     public PackedData(List<T> paletteEntries, Optional<LongStream> storage) {
/* 46 */       this(paletteEntries, storage, -1);
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\chunk\PalettedContainerRO.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */