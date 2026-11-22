/*    */ package net.minecraft.advancements.predicates;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.Optional;
/*    */ import java.util.function.BiFunction;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.Holder;
/*    */ import net.minecraft.core.HolderSet;
/*    */ import net.minecraft.core.RegistryCodecs;
/*    */ import net.minecraft.core.registries.Registries;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.world.level.material.Fluid;
/*    */ import net.minecraft.world.level.material.FluidState;
/*    */ 
/*    */ public final class FluidPredicate extends Record {
/*    */   private final Optional<HolderSet<Fluid>> fluids;
/*    */   
/*    */   private final Optional<StatePropertiesPredicate> properties;
/*    */   
/*    */   public static final Codec<FluidPredicate> CODEC;
/*    */   
/*    */   public FluidPredicate(Optional<HolderSet<Fluid>> fluids, Optional<StatePropertiesPredicate> properties) {
/* 15 */     this.fluids = fluids;
/* 15 */     this.properties = properties;
/*    */   }
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/advancements/predicates/FluidPredicate;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #15	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/advancements/predicates/FluidPredicate;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/advancements/predicates/FluidPredicate;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #15	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/advancements/predicates/FluidPredicate;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/advancements/predicates/FluidPredicate;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #15	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/advancements/predicates/FluidPredicate;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */   }
/*    */   
/*    */   public Optional<HolderSet<Fluid>> fluids() {
/* 15 */     return this.fluids;
/*    */   }
/*    */   
/*    */   public Optional<StatePropertiesPredicate> properties() {
/* 15 */     return this.properties;
/*    */   }
/*    */   
/*    */   static {
/* 19 */     CODEC = RecordCodecBuilder.create(i -> i.group((App)RegistryCodecs.homogeneousList(Registries.FLUID).optionalFieldOf("fluids").forGetter(FluidPredicate::fluids), (App)StatePropertiesPredicate.CODEC.optionalFieldOf("state").forGetter(FluidPredicate::properties)).apply((Applicative)i, FluidPredicate::new));
/*    */   }
/*    */   
/*    */   public boolean matches(ServerLevel level, BlockPos pos) {
/* 25 */     if (!level.isLoaded(pos))
/* 26 */       return false; 
/* 28 */     FluidState state = level.getFluidState(pos);
/* 30 */     if (this.fluids.isPresent() && !state.is(this.fluids.get()))
/* 31 */       return false; 
/* 33 */     if (this.properties.isPresent() && !((StatePropertiesPredicate)this.properties.get()).matches(state))
/* 34 */       return false; 
/* 36 */     return true;
/*    */   }
/*    */   
/*    */   public static class Builder {
/* 40 */     private Optional<HolderSet<Fluid>> fluids = Optional.empty();
/*    */     
/* 41 */     private Optional<StatePropertiesPredicate> properties = Optional.empty();
/*    */     
/*    */     public static Builder fluid() {
/* 47 */       return new Builder();
/*    */     }
/*    */     
/*    */     public Builder of(Fluid fluid) {
/* 51 */       this.fluids = Optional.of(HolderSet.direct(new Holder[] { (Holder)fluid.builtInRegistryHolder() }));
/* 52 */       return this;
/*    */     }
/*    */     
/*    */     public Builder of(HolderSet<Fluid> fluids) {
/* 56 */       this.fluids = Optional.of(fluids);
/* 57 */       return this;
/*    */     }
/*    */     
/*    */     public Builder setProperties(StatePropertiesPredicate properties) {
/* 61 */       this.properties = Optional.of(properties);
/* 62 */       return this;
/*    */     }
/*    */     
/*    */     public FluidPredicate build() {
/* 66 */       return new FluidPredicate(this.fluids, this.properties);
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\advancements\predicates\FluidPredicate.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */