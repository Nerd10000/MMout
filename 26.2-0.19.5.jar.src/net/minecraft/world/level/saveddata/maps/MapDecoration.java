/*    */ package net.minecraft.world.level.saveddata.maps;
/*    */ 
/*    */ import com.mojang.datafixers.util.Function5;
/*    */ import java.util.Optional;
/*    */ import net.minecraft.core.Holder;
/*    */ import net.minecraft.network.RegistryFriendlyByteBuf;
/*    */ import net.minecraft.network.chat.Component;
/*    */ import net.minecraft.network.chat.ComponentSerialization;
/*    */ import net.minecraft.network.codec.ByteBufCodecs;
/*    */ import net.minecraft.network.codec.StreamCodec;
/*    */ import net.minecraft.resources.Identifier;
/*    */ 
/*    */ public final class MapDecoration extends Record {
/*    */   private final Holder<MapDecorationType> type;
/*    */   
/*    */   private final byte x;
/*    */   
/*    */   private final byte y;
/*    */   
/*    */   private final byte rot;
/*    */   
/*    */   private final Optional<Component> name;
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/level/saveddata/maps/MapDecoration;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #13	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/level/saveddata/maps/MapDecoration;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/level/saveddata/maps/MapDecoration;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #13	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/level/saveddata/maps/MapDecoration;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/level/saveddata/maps/MapDecoration;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #13	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/world/level/saveddata/maps/MapDecoration;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */   }
/*    */   
/*    */   public Holder<MapDecorationType> type() {
/* 13 */     return this.type;
/*    */   }
/*    */   
/*    */   public byte x() {
/* 13 */     return this.x;
/*    */   }
/*    */   
/*    */   public byte y() {
/* 13 */     return this.y;
/*    */   }
/*    */   
/*    */   public byte rot() {
/* 13 */     return this.rot;
/*    */   }
/*    */   
/*    */   public Optional<Component> name() {
/* 13 */     return this.name;
/*    */   }
/*    */   
/* 14 */   public static final StreamCodec<RegistryFriendlyByteBuf, MapDecoration> STREAM_CODEC = StreamCodec.composite(MapDecorationType.STREAM_CODEC, MapDecoration::type, ByteBufCodecs.BYTE, MapDecoration::x, ByteBufCodecs.BYTE, MapDecoration::y, ByteBufCodecs.BYTE, MapDecoration::rot, ComponentSerialization.OPTIONAL_STREAM_CODEC, MapDecoration::name, MapDecoration::new);
/*    */   
/*    */   public MapDecoration(Holder<MapDecorationType> type, byte x, byte y, byte rot, Optional<Component> name) {
/* 24 */     rot = (byte)(rot & 0xF);
/*    */     this.type = type;
/*    */     this.x = x;
/*    */     this.y = y;
/*    */     this.rot = rot;
/*    */     this.name = name;
/*    */   }
/*    */   
/*    */   public Identifier getSpriteLocation() {
/* 28 */     return ((MapDecorationType)this.type.value()).assetId();
/*    */   }
/*    */   
/*    */   public boolean renderOnFrame() {
/* 32 */     return ((MapDecorationType)this.type.value()).showOnItemFrame();
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\saveddata\maps\MapDecoration.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */