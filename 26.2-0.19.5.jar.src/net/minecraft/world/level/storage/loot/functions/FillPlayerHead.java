/*    */ package net.minecraft.world.level.storage.loot.functions;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.List;
/*    */ import java.util.Set;
/*    */ import java.util.function.BiFunction;
/*    */ import net.minecraft.core.component.DataComponents;
/*    */ import net.minecraft.util.context.ContextKey;
/*    */ import net.minecraft.world.entity.player.Player;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ import net.minecraft.world.item.Items;
/*    */ import net.minecraft.world.item.component.ResolvableProfile;
/*    */ import net.minecraft.world.level.storage.loot.LootContext;
/*    */ import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
/*    */ 
/*    */ public class FillPlayerHead extends LootItemConditionalFunction {
/*    */   public static final MapCodec<FillPlayerHead> MAP_CODEC;
/*    */   
/*    */   private final LootContext.EntityTarget entityTarget;
/*    */   
/*    */   static {
/* 18 */     MAP_CODEC = RecordCodecBuilder.mapCodec(i -> commonFields(i).and((App)LootContext.EntityTarget.CODEC.fieldOf("entity").forGetter(())).apply((Applicative)i, FillPlayerHead::new));
/*    */   }
/*    */   
/*    */   public FillPlayerHead(List<LootItemCondition> predicates, LootContext.EntityTarget entityTarget) {
/* 25 */     super(predicates);
/* 26 */     this.entityTarget = entityTarget;
/*    */   }
/*    */   
/*    */   public MapCodec<FillPlayerHead> codec() {
/* 31 */     return MAP_CODEC;
/*    */   }
/*    */   
/*    */   public Set<ContextKey<?>> getReferencedContextParams() {
/* 36 */     return Set.of(this.entityTarget.contextParam());
/*    */   }
/*    */   
/*    */   public ItemStack run(ItemStack itemStack, LootContext context) {
/* 41 */     if (itemStack.is(Items.PLAYER_HEAD)) {
/* 42 */       Object object = context.getOptionalParameter(this.entityTarget.contextParam());
/* 42 */       if (object instanceof Player) {
/* 42 */         Player dataDonor = (Player)object;
/* 43 */         itemStack.set(DataComponents.PROFILE, ResolvableProfile.createResolved(dataDonor.getGameProfile()));
/*    */       } 
/*    */     } 
/* 46 */     return itemStack;
/*    */   }
/*    */   
/*    */   public static LootItemConditionalFunction.Builder<?> fillPlayerHead(LootContext.EntityTarget entityTarget) {
/* 50 */     return simpleBuilder(conditions -> new FillPlayerHead(conditions, entityTarget));
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\storage\loot\functions\FillPlayerHead.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */