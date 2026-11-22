/*    */ package net.minecraft.advancements.predicates.entity;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.Optional;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.world.entity.Entity;
/*    */ import net.minecraft.world.entity.projectile.FishingHook;
/*    */ import net.minecraft.world.phys.Vec3;
/*    */ 
/*    */ public final class FishingHookPredicate extends Record implements EntitySubPredicate {
/*    */   private final Optional<Boolean> inOpenWater;
/*    */   
/*    */   public FishingHookPredicate(Optional<Boolean> inOpenWater) {
/* 13 */     this.inOpenWater = inOpenWater;
/*    */   }
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/advancements/predicates/entity/FishingHookPredicate;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #13	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/advancements/predicates/entity/FishingHookPredicate;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/advancements/predicates/entity/FishingHookPredicate;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #13	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/advancements/predicates/entity/FishingHookPredicate;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/advancements/predicates/entity/FishingHookPredicate;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #13	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/advancements/predicates/entity/FishingHookPredicate;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */   }
/*    */   
/*    */   public Optional<Boolean> inOpenWater() {
/* 13 */     return this.inOpenWater;
/*    */   }
/*    */   
/* 16 */   public static final FishingHookPredicate ANY = new FishingHookPredicate(Optional.empty());
/*    */   
/*    */   public static final Codec<FishingHookPredicate> CODEC;
/*    */   
/*    */   static {
/* 18 */     CODEC = RecordCodecBuilder.create(i -> i.group((App)Codec.BOOL.optionalFieldOf("in_open_water").forGetter(FishingHookPredicate::inOpenWater)).apply((Applicative)i, FishingHookPredicate::new));
/*    */   }
/*    */   
/*    */   public static FishingHookPredicate inOpenWater(boolean requirement) {
/* 23 */     return new FishingHookPredicate(Optional.of(Boolean.valueOf(requirement)));
/*    */   }
/*    */   
/*    */   public boolean matches(Entity entity, ServerLevel level, Vec3 position) {
/* 28 */     if (this.inOpenWater.isEmpty())
/* 29 */       return true; 
/* 31 */     if (entity instanceof FishingHook) {
/* 31 */       FishingHook hook = (FishingHook)entity;
/* 32 */       return (((Boolean)this.inOpenWater.get()).booleanValue() == hook.isOpenWaterFishing());
/*    */     } 
/* 34 */     return false;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\advancements\predicates\entity\FishingHookPredicate.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */