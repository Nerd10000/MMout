/*     */ package net.minecraft.world.level.storage.loot.functions;
/*     */ 
/*     */ import com.mojang.datafixers.kinds.App;
/*     */ import com.mojang.datafixers.kinds.Applicative;
/*     */ import com.mojang.serialization.Codec;
/*     */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*     */ import java.util.function.BiFunction;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.world.effect.MobEffect;
/*     */ import net.minecraft.world.level.storage.loot.LootContextUser;
/*     */ import net.minecraft.world.level.storage.loot.Validatable;
/*     */ import net.minecraft.world.level.storage.loot.ValidationContext;
/*     */ import net.minecraft.world.level.storage.loot.providers.number.NumberProvider;
/*     */ import net.minecraft.world.level.storage.loot.providers.number.NumberProviders;
/*     */ 
/*     */ final class EffectEntry extends Record implements LootContextUser {
/*     */   private final Holder<MobEffect> effect;
/*     */   
/*     */   private final NumberProvider duration;
/*     */   
/*     */   public static final Codec<EffectEntry> CODEC;
/*     */   
/*     */   private EffectEntry(Holder<MobEffect> effect, NumberProvider duration) {
/* 105 */     this.effect = effect;
/* 105 */     this.duration = duration;
/*     */   }
/*     */   
/*     */   public final String toString() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/level/storage/loot/functions/SetStewEffectFunction$EffectEntry;)Ljava/lang/String;
/*     */     //   6: areturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #105	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/world/level/storage/loot/functions/SetStewEffectFunction$EffectEntry;
/*     */   }
/*     */   
/*     */   public final int hashCode() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/level/storage/loot/functions/SetStewEffectFunction$EffectEntry;)I
/*     */     //   6: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #105	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/world/level/storage/loot/functions/SetStewEffectFunction$EffectEntry;
/*     */   }
/*     */   
/*     */   public final boolean equals(Object o) {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: aload_1
/*     */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/level/storage/loot/functions/SetStewEffectFunction$EffectEntry;Ljava/lang/Object;)Z
/*     */     //   7: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #105	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	8	0	this	Lnet/minecraft/world/level/storage/loot/functions/SetStewEffectFunction$EffectEntry;
/*     */     //   0	8	1	o	Ljava/lang/Object;
/*     */   }
/*     */   
/*     */   public Holder<MobEffect> effect() {
/* 105 */     return this.effect;
/*     */   }
/*     */   
/*     */   public NumberProvider duration() {
/* 105 */     return this.duration;
/*     */   }
/*     */   
/*     */   static {
/* 106 */     CODEC = RecordCodecBuilder.create(i -> i.group((App)MobEffect.CODEC.fieldOf("type").forGetter(EffectEntry::effect), (App)NumberProviders.CODEC.fieldOf("duration").forGetter(EffectEntry::duration)).apply((Applicative)i, EffectEntry::new));
/*     */   }
/*     */   
/*     */   public void validate(ValidationContext context) {
/* 113 */     super.validate(context);
/* 115 */     Validatable.validate(context, "duration", (Validatable)this.duration);
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\storage\loot\functions\SetStewEffectFunction$EffectEntry.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */