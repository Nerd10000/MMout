/*    */ package net.minecraft.world.level.storage.loot.functions;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.datafixers.util.Function3;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.List;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ import net.minecraft.world.level.storage.loot.ContainerComponentManipulator;
/*    */ import net.minecraft.world.level.storage.loot.ContainerComponentManipulators;
/*    */ import net.minecraft.world.level.storage.loot.LootContext;
/*    */ import net.minecraft.world.level.storage.loot.Validatable;
/*    */ import net.minecraft.world.level.storage.loot.ValidationContext;
/*    */ import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
/*    */ 
/*    */ public class ModifyContainerContents extends LootItemConditionalFunction {
/*    */   public static final MapCodec<ModifyContainerContents> MAP_CODEC;
/*    */   
/*    */   private final ContainerComponentManipulator<?> component;
/*    */   
/*    */   private final LootItemFunction modifier;
/*    */   
/*    */   static {
/* 16 */     MAP_CODEC = RecordCodecBuilder.mapCodec(i -> commonFields(i).and(i.group((App)ContainerComponentManipulators.CODEC.fieldOf("component").forGetter(()), (App)LootItemFunctions.ROOT_CODEC.fieldOf("modifier").forGetter(()))).apply((Applicative)i, ModifyContainerContents::new));
/*    */   }
/*    */   
/*    */   private ModifyContainerContents(List<LootItemCondition> predicates, ContainerComponentManipulator<?> component, LootItemFunction modifier) {
/* 25 */     super(predicates);
/* 26 */     this.component = component;
/* 27 */     this.modifier = modifier;
/*    */   }
/*    */   
/*    */   public MapCodec<ModifyContainerContents> codec() {
/* 32 */     return MAP_CODEC;
/*    */   }
/*    */   
/*    */   public ItemStack run(ItemStack itemStack, LootContext context) {
/* 37 */     if (itemStack.isEmpty())
/* 38 */       return itemStack; 
/* 41 */     this.component.modifyItems(itemStack, c -> this.modifier.apply(c, context));
/* 43 */     return itemStack;
/*    */   }
/*    */   
/*    */   public void validate(ValidationContext context) {
/* 48 */     super.validate(context);
/* 50 */     Validatable.validate(context, "modifier", (Validatable)this.modifier);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\storage\loot\functions\ModifyContainerContents.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */