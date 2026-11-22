/*    */ package net.minecraft.world.level.storage.loot.predicates;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.datafixers.util.Function3;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.Optional;
/*    */ import java.util.Set;
/*    */ import java.util.function.BiFunction;
/*    */ import net.minecraft.advancements.predicates.LocationPredicate;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.Vec3i;
/*    */ import net.minecraft.util.context.ContextKey;
/*    */ import net.minecraft.world.level.storage.loot.LootContext;
/*    */ import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
/*    */ import net.minecraft.world.phys.Vec3;
/*    */ 
/*    */ public final class LocationCheck extends Record implements LootItemCondition {
/*    */   private final Optional<LocationPredicate> predicate;
/*    */   
/*    */   private final BlockPos offset;
/*    */   
/*    */   private static final MapCodec<BlockPos> OFFSET_CODEC;
/*    */   
/*    */   public static final MapCodec<LocationCheck> MAP_CODEC;
/*    */   
/*    */   public LocationCheck(Optional<LocationPredicate> predicate, BlockPos offset) {
/* 16 */     this.predicate = predicate;
/* 16 */     this.offset = offset;
/*    */   }
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/level/storage/loot/predicates/LocationCheck;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #16	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/level/storage/loot/predicates/LocationCheck;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/level/storage/loot/predicates/LocationCheck;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #16	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/level/storage/loot/predicates/LocationCheck;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/level/storage/loot/predicates/LocationCheck;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #16	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/world/level/storage/loot/predicates/LocationCheck;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */   }
/*    */   
/*    */   public Optional<LocationPredicate> predicate() {
/* 16 */     return this.predicate;
/*    */   }
/*    */   
/*    */   public BlockPos offset() {
/* 16 */     return this.offset;
/*    */   }
/*    */   
/*    */   static {
/* 20 */     OFFSET_CODEC = RecordCodecBuilder.mapCodec(i -> i.group((App)Codec.INT.optionalFieldOf("offsetX", Integer.valueOf(0)).forGetter(Vec3i::getX), (App)Codec.INT.optionalFieldOf("offsetY", Integer.valueOf(0)).forGetter(Vec3i::getY), (App)Codec.INT.optionalFieldOf("offsetZ", Integer.valueOf(0)).forGetter(Vec3i::getZ)).apply((Applicative)i, BlockPos::new));
/* 26 */     MAP_CODEC = RecordCodecBuilder.mapCodec(i -> i.group((App)LocationPredicate.CODEC.optionalFieldOf("predicate").forGetter(LocationCheck::predicate), (App)OFFSET_CODEC.forGetter(LocationCheck::offset)).apply((Applicative)i, LocationCheck::new));
/*    */   }
/*    */   
/*    */   public MapCodec<LocationCheck> codec() {
/* 33 */     return MAP_CODEC;
/*    */   }
/*    */   
/*    */   public boolean test(LootContext context) {
/* 38 */     Vec3 pos = (Vec3)context.getOptionalParameter(LootContextParams.ORIGIN);
/* 39 */     return (pos != null && (this.predicate.isEmpty() || ((LocationPredicate)this.predicate.get()).matches(context.getLevel(), pos.x() + this.offset.getX(), pos.y() + this.offset.getY(), pos.z() + this.offset.getZ())));
/*    */   }
/*    */   
/*    */   public Set<ContextKey<?>> getReferencedContextParams() {
/* 44 */     return Set.of(LootContextParams.ORIGIN);
/*    */   }
/*    */   
/*    */   public static LootItemCondition.Builder checkLocation(LocationPredicate.Builder predicate) {
/* 48 */     return () -> new LocationCheck(Optional.of(predicate.build()), BlockPos.ZERO);
/*    */   }
/*    */   
/*    */   public static LootItemCondition.Builder checkLocation(LocationPredicate.Builder predicate, BlockPos offset) {
/* 52 */     return () -> new LocationCheck(Optional.of(predicate.build()), offset);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\storage\loot\predicates\LocationCheck.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */