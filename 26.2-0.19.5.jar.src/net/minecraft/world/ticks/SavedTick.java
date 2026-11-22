/*    */ package net.minecraft.world.ticks;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.datafixers.util.Function3;
/*    */ import com.mojang.datafixers.util.Function4;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import it.unimi.dsi.fastutil.Hash;
/*    */ import java.util.List;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.Vec3i;
/*    */ import net.minecraft.world.level.ChunkPos;
/*    */ 
/*    */ public final class SavedTick<T> extends Record {
/*    */   private final T type;
/*    */   
/*    */   private final BlockPos pos;
/*    */   
/*    */   private final int delay;
/*    */   
/*    */   private final TickPriority priority;
/*    */   
/*    */   public SavedTick(T type, BlockPos pos, int delay, TickPriority priority) {
/* 14 */     this.type = type;
/* 14 */     this.pos = pos;
/* 14 */     this.delay = delay;
/* 14 */     this.priority = priority;
/*    */   }
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/ticks/SavedTick;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #14	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/ticks/SavedTick;
/*    */     // Local variable type table:
/*    */     //   start	length	slot	name	signature
/*    */     //   0	7	0	this	Lnet/minecraft/world/ticks/SavedTick<TT;>;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/ticks/SavedTick;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #14	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/ticks/SavedTick;
/*    */     // Local variable type table:
/*    */     //   start	length	slot	name	signature
/*    */     //   0	7	0	this	Lnet/minecraft/world/ticks/SavedTick<TT;>;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/ticks/SavedTick;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #14	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/world/ticks/SavedTick;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */     // Local variable type table:
/*    */     //   start	length	slot	name	signature
/*    */     //   0	8	0	this	Lnet/minecraft/world/ticks/SavedTick<TT;>;
/*    */   }
/*    */   
/*    */   public T type() {
/* 14 */     return this.type;
/*    */   }
/*    */   
/*    */   public BlockPos pos() {
/* 14 */     return this.pos;
/*    */   }
/*    */   
/*    */   public int delay() {
/* 14 */     return this.delay;
/*    */   }
/*    */   
/*    */   public TickPriority priority() {
/* 14 */     return this.priority;
/*    */   }
/*    */   
/*    */   public static <T> Codec<SavedTick<T>> codec(Codec<T> typeCodec) {
/* 21 */     MapCodec<BlockPos> posCodec = RecordCodecBuilder.mapCodec(i -> i.group((App)Codec.INT.fieldOf("x").forGetter(Vec3i::getX), (App)Codec.INT.fieldOf("y").forGetter(Vec3i::getY), (App)Codec.INT.fieldOf("z").forGetter(Vec3i::getZ)).apply((Applicative)i, BlockPos::new));
/* 26 */     return RecordCodecBuilder.create(i -> i.group((App)typeCodec.fieldOf("i").forGetter(SavedTick::type), (App)posCodec.forGetter(SavedTick::pos), (App)Codec.INT.fieldOf("t").forGetter(SavedTick::delay), (App)TickPriority.CODEC.fieldOf("p").forGetter(SavedTick::priority)).apply((Applicative)i, SavedTick::new));
/*    */   }
/*    */   
/* 34 */   public static final Hash.Strategy<SavedTick<?>> UNIQUE_TICK_HASH = new Hash.Strategy<SavedTick<?>>() {
/*    */       public int hashCode(SavedTick<?> o) {
/* 37 */         return 31 * o.pos().hashCode() + o.type().hashCode();
/*    */       }
/*    */       
/*    */       public boolean equals(SavedTick<?> a, SavedTick<?> b) {
/* 42 */         if (a == b)
/* 43 */           return true; 
/* 45 */         if (a == null || b == null)
/* 46 */           return false; 
/* 48 */         return (a.type() == b.type() && a.pos().equals(b.pos()));
/*    */       }
/*    */     };
/*    */   
/*    */   public static <T> List<SavedTick<T>> filterTickListForChunk(List<SavedTick<T>> savedTicks, ChunkPos chunkPos) {
/* 53 */     long posKey = chunkPos.pack();
/* 54 */     return savedTicks.stream()
/* 55 */       .filter(tick -> (ChunkPos.pack(tick.pos()) == posKey))
/*    */       
/* 60 */       .toList();
/*    */   }
/*    */   
/*    */   public ScheduledTick<T> unpack(long currentTick, long currentSubTick) {
/* 64 */     return new ScheduledTick<>(this.type, this.pos, currentTick + this.delay, this.priority, currentSubTick);
/*    */   }
/*    */   
/*    */   public static <T> SavedTick<T> probe(T type, BlockPos pos) {
/* 68 */     return new SavedTick<>(type, pos, 0, TickPriority.NORMAL);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\ticks\SavedTick.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */