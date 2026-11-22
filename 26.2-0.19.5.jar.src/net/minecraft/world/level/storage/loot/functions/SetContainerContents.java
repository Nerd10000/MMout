/*    */ package net.minecraft.world.level.storage.loot.functions;
/*    */ 
/*    */ import com.google.common.collect.ImmutableList;
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.datafixers.util.Function3;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.List;
/*    */ import java.util.Objects;
/*    */ import java.util.stream.Stream;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ import net.minecraft.world.level.storage.loot.ContainerComponentManipulator;
/*    */ import net.minecraft.world.level.storage.loot.ContainerComponentManipulators;
/*    */ import net.minecraft.world.level.storage.loot.LootContext;
/*    */ import net.minecraft.world.level.storage.loot.LootTable;
/*    */ import net.minecraft.world.level.storage.loot.Validatable;
/*    */ import net.minecraft.world.level.storage.loot.ValidationContext;
/*    */ import net.minecraft.world.level.storage.loot.entries.LootPoolEntries;
/*    */ import net.minecraft.world.level.storage.loot.entries.LootPoolEntry;
/*    */ import net.minecraft.world.level.storage.loot.entries.LootPoolEntryContainer;
/*    */ import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
/*    */ 
/*    */ public class SetContainerContents extends LootItemConditionalFunction {
/*    */   public static final MapCodec<SetContainerContents> MAP_CODEC;
/*    */   
/*    */   private final ContainerComponentManipulator<?> component;
/*    */   
/*    */   private final List<LootPoolEntryContainer> entries;
/*    */   
/*    */   static {
/* 21 */     MAP_CODEC = RecordCodecBuilder.mapCodec(i -> commonFields(i).and(i.group((App)ContainerComponentManipulators.CODEC.fieldOf("component").forGetter(()), (App)LootPoolEntries.CODEC.listOf().fieldOf("entries").forGetter(()))).apply((Applicative)i, SetContainerContents::new));
/*    */   }
/*    */   
/*    */   private SetContainerContents(List<LootItemCondition> predicates, ContainerComponentManipulator<?> component, List<LootPoolEntryContainer> entries) {
/* 30 */     super(predicates);
/* 31 */     this.component = component;
/* 32 */     this.entries = List.copyOf(entries);
/*    */   }
/*    */   
/*    */   public MapCodec<SetContainerContents> codec() {
/* 37 */     return MAP_CODEC;
/*    */   }
/*    */   
/*    */   public ItemStack run(ItemStack itemStack, LootContext context) {
/* 42 */     if (itemStack.isEmpty())
/* 43 */       return itemStack; 
/* 46 */     Stream.Builder<ItemStack> contents = Stream.builder();
/* 47 */     this.entries.forEach(e -> e.expand(context, ()));
/* 48 */     this.component.setContents(itemStack, contents.build());
/* 50 */     return itemStack;
/*    */   }
/*    */   
/*    */   public void validate(ValidationContext context) {
/* 55 */     super.validate(context);
/* 57 */     Validatable.validate(context, "entries", this.entries);
/*    */   }
/*    */   
/*    */   public static class Builder extends LootItemConditionalFunction.Builder<Builder> {
/* 61 */     private final ImmutableList.Builder<LootPoolEntryContainer> entries = ImmutableList.builder();
/*    */     
/*    */     private final ContainerComponentManipulator<?> component;
/*    */     
/*    */     public Builder(ContainerComponentManipulator<?> component) {
/* 65 */       this.component = component;
/*    */     }
/*    */     
/*    */     protected Builder getThis() {
/* 70 */       return this;
/*    */     }
/*    */     
/*    */     public Builder withEntry(LootPoolEntryContainer.Builder<?> entry) {
/* 74 */       this.entries.add(entry.build());
/* 75 */       return this;
/*    */     }
/*    */     
/*    */     public LootItemFunction build() {
/* 80 */       return new SetContainerContents(getConditions(), this.component, (List<LootPoolEntryContainer>)this.entries.build());
/*    */     }
/*    */   }
/*    */   
/*    */   public static Builder setContents(ContainerComponentManipulator<?> component) {
/* 85 */     return new Builder(component);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\storage\loot\functions\SetContainerContents.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */