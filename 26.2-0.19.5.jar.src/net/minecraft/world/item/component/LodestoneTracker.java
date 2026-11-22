/*    */ package net.minecraft.world.item.component;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import io.netty.buffer.ByteBuf;
/*    */ import java.util.Optional;
/*    */ import java.util.function.BiFunction;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.GlobalPos;
/*    */ import net.minecraft.network.codec.ByteBufCodecs;
/*    */ import net.minecraft.network.codec.StreamCodec;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.world.entity.ai.village.poi.PoiTypes;
/*    */ 
/*    */ public final class LodestoneTracker extends Record {
/*    */   private final Optional<GlobalPos> target;
/*    */   
/*    */   private final boolean tracked;
/*    */   
/*    */   public static final Codec<LodestoneTracker> CODEC;
/*    */   
/*    */   public LodestoneTracker(Optional<GlobalPos> target, boolean tracked) {
/* 15 */     this.target = target;
/* 15 */     this.tracked = tracked;
/*    */   }
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/item/component/LodestoneTracker;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #15	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/item/component/LodestoneTracker;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/item/component/LodestoneTracker;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #15	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/item/component/LodestoneTracker;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/item/component/LodestoneTracker;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #15	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/world/item/component/LodestoneTracker;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */   }
/*    */   
/*    */   public Optional<GlobalPos> target() {
/* 15 */     return this.target;
/*    */   }
/*    */   
/*    */   public boolean tracked() {
/* 15 */     return this.tracked;
/*    */   }
/*    */   
/*    */   static {
/* 16 */     CODEC = RecordCodecBuilder.create(i -> i.group((App)GlobalPos.CODEC.optionalFieldOf("target").forGetter(LodestoneTracker::target), (App)Codec.BOOL.optionalFieldOf("tracked", Boolean.valueOf(true)).forGetter(LodestoneTracker::tracked)).apply((Applicative)i, LodestoneTracker::new));
/*    */   }
/*    */   
/* 21 */   public static final StreamCodec<ByteBuf, LodestoneTracker> STREAM_CODEC = StreamCodec.composite(GlobalPos.STREAM_CODEC
/* 22 */       .apply(ByteBufCodecs::optional), LodestoneTracker::target, ByteBufCodecs.BOOL, LodestoneTracker::tracked, LodestoneTracker::new);
/*    */   
/*    */   public LodestoneTracker tick(ServerLevel level) {
/* 28 */     if (!this.tracked || this.target.isEmpty())
/* 29 */       return this; 
/* 31 */     if (((GlobalPos)this.target.get()).dimension() != level.dimension())
/* 32 */       return this; 
/* 34 */     BlockPos blockPos = ((GlobalPos)this.target.get()).pos();
/* 35 */     if (!level.isInWorldBounds(blockPos) || !level.getPoiManager().existsAtPosition(PoiTypes.LODESTONE, blockPos))
/* 36 */       return new LodestoneTracker(Optional.empty(), true); 
/* 38 */     return this;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\item\component\LodestoneTracker.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */