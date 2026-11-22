/*    */ package net.minecraft.world.level.storage.loot.functions;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.datafixers.util.Function3;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.List;
/*    */ import java.util.Objects;
/*    */ import java.util.Optional;
/*    */ import net.minecraft.core.component.DataComponents;
/*    */ import net.minecraft.util.ExtraCodecs;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ import net.minecraft.world.item.component.FireworkExplosion;
/*    */ import net.minecraft.world.item.component.Fireworks;
/*    */ import net.minecraft.world.level.storage.loot.LootContext;
/*    */ import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
/*    */ 
/*    */ public class SetFireworksFunction extends LootItemConditionalFunction {
/*    */   public static final MapCodec<SetFireworksFunction> MAP_CODEC;
/*    */   
/*    */   static {
/* 17 */     MAP_CODEC = RecordCodecBuilder.mapCodec(i -> commonFields(i).and(i.group((App)ListOperation.StandAlone.<T>codec(FireworkExplosion.CODEC, 256).optionalFieldOf("explosions").forGetter(()), (App)ExtraCodecs.UNSIGNED_BYTE.optionalFieldOf("flight_duration").forGetter(()))).apply((Applicative)i, SetFireworksFunction::new));
/*    */   }
/*    */   
/* 22 */   public static final Fireworks DEFAULT_VALUE = new Fireworks(0, List.of());
/*    */   
/*    */   private final Optional<ListOperation.StandAlone<FireworkExplosion>> explosions;
/*    */   
/*    */   private final Optional<Integer> flightDuration;
/*    */   
/*    */   protected SetFireworksFunction(List<LootItemCondition> predicates, Optional<ListOperation.StandAlone<FireworkExplosion>> explosions, Optional<Integer> flightDuration) {
/* 28 */     super(predicates);
/* 29 */     this.explosions = explosions;
/* 30 */     this.flightDuration = flightDuration;
/*    */   }
/*    */   
/*    */   protected ItemStack run(ItemStack itemStack, LootContext context) {
/* 35 */     itemStack.update(DataComponents.FIREWORKS, DEFAULT_VALUE, this::apply);
/* 36 */     return itemStack;
/*    */   }
/*    */   
/*    */   private Fireworks apply(Fireworks old) {
/* 41 */     Objects.requireNonNull(old);
/* 41 */     return new Fireworks(((Integer)this.flightDuration.orElseGet(old::flightDuration)).intValue(), this.explosions
/* 42 */         .<List>map(operation -> operation.apply(old.explosions())).orElse(old.explosions()));
/*    */   }
/*    */   
/*    */   public MapCodec<SetFireworksFunction> codec() {
/* 48 */     return MAP_CODEC;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\storage\loot\functions\SetFireworksFunction.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */