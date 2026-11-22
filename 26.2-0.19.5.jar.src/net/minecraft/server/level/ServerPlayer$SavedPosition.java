/*      */ package net.minecraft.server.level;
/*      */ 
/*      */ import com.mojang.datafixers.kinds.App;
/*      */ import com.mojang.datafixers.kinds.Applicative;
/*      */ import com.mojang.datafixers.util.Function3;
/*      */ import com.mojang.serialization.MapCodec;
/*      */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*      */ import java.util.Optional;
/*      */ import net.minecraft.resources.ResourceKey;
/*      */ import net.minecraft.world.level.Level;
/*      */ import net.minecraft.world.phys.Vec2;
/*      */ import net.minecraft.world.phys.Vec3;
/*      */ 
/*      */ public final class SavedPosition extends Record {
/*      */   private final Optional<ResourceKey<Level>> dimension;
/*      */   
/*      */   private final Optional<Vec3> position;
/*      */   
/*      */   private final Optional<Vec2> rotation;
/*      */   
/*      */   public static final MapCodec<SavedPosition> MAP_CODEC;
/*      */   
/*      */   public SavedPosition(Optional<ResourceKey<Level>> dimension, Optional<Vec3> position, Optional<Vec2> rotation) {
/* 2420 */     this.dimension = dimension;
/* 2420 */     this.position = position;
/* 2420 */     this.rotation = rotation;
/*      */   }
/*      */   
/*      */   public final String toString() {
/*      */     // Byte code:
/*      */     //   0: aload_0
/*      */     //   1: <illegal opcode> toString : (Lnet/minecraft/server/level/ServerPlayer$SavedPosition;)Ljava/lang/String;
/*      */     //   6: areturn
/*      */     // Line number table:
/*      */     //   Java source line number -> byte code offset
/*      */     //   #2420	-> 0
/*      */     // Local variable table:
/*      */     //   start	length	slot	name	descriptor
/*      */     //   0	7	0	this	Lnet/minecraft/server/level/ServerPlayer$SavedPosition;
/*      */   }
/*      */   
/*      */   public final int hashCode() {
/*      */     // Byte code:
/*      */     //   0: aload_0
/*      */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/server/level/ServerPlayer$SavedPosition;)I
/*      */     //   6: ireturn
/*      */     // Line number table:
/*      */     //   Java source line number -> byte code offset
/*      */     //   #2420	-> 0
/*      */     // Local variable table:
/*      */     //   start	length	slot	name	descriptor
/*      */     //   0	7	0	this	Lnet/minecraft/server/level/ServerPlayer$SavedPosition;
/*      */   }
/*      */   
/*      */   public final boolean equals(Object o) {
/*      */     // Byte code:
/*      */     //   0: aload_0
/*      */     //   1: aload_1
/*      */     //   2: <illegal opcode> equals : (Lnet/minecraft/server/level/ServerPlayer$SavedPosition;Ljava/lang/Object;)Z
/*      */     //   7: ireturn
/*      */     // Line number table:
/*      */     //   Java source line number -> byte code offset
/*      */     //   #2420	-> 0
/*      */     // Local variable table:
/*      */     //   start	length	slot	name	descriptor
/*      */     //   0	8	0	this	Lnet/minecraft/server/level/ServerPlayer$SavedPosition;
/*      */     //   0	8	1	o	Ljava/lang/Object;
/*      */   }
/*      */   
/*      */   public Optional<ResourceKey<Level>> dimension() {
/* 2420 */     return this.dimension;
/*      */   }
/*      */   
/*      */   public Optional<Vec3> position() {
/* 2420 */     return this.position;
/*      */   }
/*      */   
/*      */   public Optional<Vec2> rotation() {
/* 2420 */     return this.rotation;
/*      */   }
/*      */   
/*      */   static {
/* 2425 */     MAP_CODEC = RecordCodecBuilder.mapCodec(i -> i.group((App)Level.RESOURCE_KEY_CODEC.optionalFieldOf("Dimension").forGetter(SavedPosition::dimension), (App)Vec3.CODEC.optionalFieldOf("Pos").forGetter(SavedPosition::position), (App)Vec2.CODEC.optionalFieldOf("Rotation").forGetter(SavedPosition::rotation)).apply((Applicative)i, SavedPosition::new));
/*      */   }
/*      */   
/* 2431 */   public static final SavedPosition EMPTY = new SavedPosition(Optional.empty(), Optional.empty(), Optional.empty());
/*      */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\server\level\ServerPlayer$SavedPosition.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */