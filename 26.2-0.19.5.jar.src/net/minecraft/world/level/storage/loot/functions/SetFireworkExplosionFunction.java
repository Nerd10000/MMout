/*    */ package net.minecraft.world.level.storage.loot.functions;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.datafixers.util.Function6;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import it.unimi.dsi.fastutil.ints.IntList;
/*    */ import java.util.List;
/*    */ import java.util.Objects;
/*    */ import java.util.Optional;
/*    */ import net.minecraft.core.component.DataComponents;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ import net.minecraft.world.item.component.FireworkExplosion;
/*    */ import net.minecraft.world.level.storage.loot.LootContext;
/*    */ import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
/*    */ 
/*    */ public class SetFireworkExplosionFunction extends LootItemConditionalFunction {
/*    */   public static final MapCodec<SetFireworkExplosionFunction> MAP_CODEC;
/*    */   
/*    */   static {
/* 17 */     MAP_CODEC = RecordCodecBuilder.mapCodec(i -> commonFields(i).and(i.group((App)FireworkExplosion.Shape.CODEC.optionalFieldOf("shape").forGetter(()), (App)FireworkExplosion.COLOR_LIST_CODEC.optionalFieldOf("colors").forGetter(()), (App)FireworkExplosion.COLOR_LIST_CODEC.optionalFieldOf("fade_colors").forGetter(()), (App)Codec.BOOL.optionalFieldOf("trail").forGetter(()), (App)Codec.BOOL.optionalFieldOf("twinkle").forGetter(()))).apply((Applicative)i, SetFireworkExplosionFunction::new));
/*    */   }
/*    */   
/* 25 */   public static final FireworkExplosion DEFAULT_VALUE = new FireworkExplosion(FireworkExplosion.Shape.SMALL_BALL, IntList.of(), IntList.of(), false, false);
/*    */   
/*    */   private final Optional<FireworkExplosion.Shape> shape;
/*    */   
/*    */   private final Optional<IntList> colors;
/*    */   
/*    */   private final Optional<IntList> fadeColors;
/*    */   
/*    */   private final Optional<Boolean> trail;
/*    */   
/*    */   private final Optional<Boolean> twinkle;
/*    */   
/*    */   public SetFireworkExplosionFunction(List<LootItemCondition> predicates, Optional<FireworkExplosion.Shape> shape, Optional<IntList> colors, Optional<IntList> fadeColors, Optional<Boolean> hasTrail, Optional<Boolean> hasTwinkle) {
/* 34 */     super(predicates);
/* 35 */     this.shape = shape;
/* 36 */     this.colors = colors;
/* 37 */     this.fadeColors = fadeColors;
/* 38 */     this.trail = hasTrail;
/* 39 */     this.twinkle = hasTwinkle;
/*    */   }
/*    */   
/*    */   protected ItemStack run(ItemStack itemStack, LootContext context) {
/* 44 */     itemStack.update(DataComponents.FIREWORK_EXPLOSION, DEFAULT_VALUE, this::apply);
/* 45 */     return itemStack;
/*    */   }
/*    */   
/*    */   private FireworkExplosion apply(FireworkExplosion original) {
/* 50 */     Objects.requireNonNull(original);
/* 51 */     Objects.requireNonNull(original);
/* 52 */     Objects.requireNonNull(original);
/* 53 */     Objects.requireNonNull(original);
/* 54 */     Objects.requireNonNull(original);
/* 54 */     return new FireworkExplosion(this.shape.orElseGet(original::shape), this.colors.orElseGet(original::colors), this.fadeColors.orElseGet(original::fadeColors), ((Boolean)this.trail.orElseGet(original::hasTrail)).booleanValue(), ((Boolean)this.twinkle.orElseGet(original::hasTwinkle)).booleanValue());
/*    */   }
/*    */   
/*    */   public MapCodec<SetFireworkExplosionFunction> codec() {
/* 60 */     return MAP_CODEC;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\storage\loot\functions\SetFireworkExplosionFunction.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */