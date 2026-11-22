/*    */ package net.minecraft.world.level.saveddata.maps;
/*    */ 
/*    */ import com.mojang.serialization.Codec;
/*    */ import net.minecraft.core.Holder;
/*    */ import net.minecraft.core.registries.BuiltInRegistries;
/*    */ import net.minecraft.core.registries.Registries;
/*    */ import net.minecraft.network.RegistryFriendlyByteBuf;
/*    */ import net.minecraft.network.codec.ByteBufCodecs;
/*    */ import net.minecraft.network.codec.StreamCodec;
/*    */ import net.minecraft.resources.Identifier;
/*    */ 
/*    */ public final class MapDecorationType extends Record {
/*    */   private final Identifier assetId;
/*    */   
/*    */   private final boolean showOnItemFrame;
/*    */   
/*    */   private final int mapColor;
/*    */   
/*    */   private final boolean explorationMapElement;
/*    */   
/*    */   private final boolean trackCount;
/*    */   
/*    */   public static final int NO_MAP_COLOR = -1;
/*    */   
/*    */   public MapDecorationType(Identifier assetId, boolean showOnItemFrame, int mapColor, boolean explorationMapElement, boolean trackCount) {
/* 12 */     this.assetId = assetId;
/* 12 */     this.showOnItemFrame = showOnItemFrame;
/* 12 */     this.mapColor = mapColor;
/* 12 */     this.explorationMapElement = explorationMapElement;
/* 12 */     this.trackCount = trackCount;
/*    */   }
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/level/saveddata/maps/MapDecorationType;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #12	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/level/saveddata/maps/MapDecorationType;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/level/saveddata/maps/MapDecorationType;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #12	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/level/saveddata/maps/MapDecorationType;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/level/saveddata/maps/MapDecorationType;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #12	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/world/level/saveddata/maps/MapDecorationType;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */   }
/*    */   
/*    */   public Identifier assetId() {
/* 12 */     return this.assetId;
/*    */   }
/*    */   
/*    */   public boolean showOnItemFrame() {
/* 12 */     return this.showOnItemFrame;
/*    */   }
/*    */   
/*    */   public int mapColor() {
/* 12 */     return this.mapColor;
/*    */   }
/*    */   
/*    */   public boolean explorationMapElement() {
/* 12 */     return this.explorationMapElement;
/*    */   }
/*    */   
/*    */   public boolean trackCount() {
/* 12 */     return this.trackCount;
/*    */   }
/*    */   
/* 21 */   public static final Codec<Holder<MapDecorationType>> CODEC = BuiltInRegistries.MAP_DECORATION_TYPE.holderByNameCodec();
/*    */   
/* 22 */   public static final StreamCodec<RegistryFriendlyByteBuf, Holder<MapDecorationType>> STREAM_CODEC = ByteBufCodecs.holderRegistry(Registries.MAP_DECORATION_TYPE);
/*    */   
/*    */   public boolean hasMapColor() {
/* 25 */     return (this.mapColor != -1);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\saveddata\maps\MapDecorationType.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */