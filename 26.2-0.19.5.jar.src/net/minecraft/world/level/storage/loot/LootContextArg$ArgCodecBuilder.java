/*    */ package net.minecraft.world.level.storage.loot;
/*    */ 
/*    */ import com.mojang.serialization.Codec;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.util.ExtraCodecs;
/*    */ import net.minecraft.util.StringRepresentable;
/*    */ import net.minecraft.util.context.ContextKey;
/*    */ import net.minecraft.world.entity.Entity;
/*    */ import net.minecraft.world.item.ItemInstance;
/*    */ import net.minecraft.world.level.block.entity.BlockEntity;
/*    */ 
/*    */ public final class ArgCodecBuilder<R> {
/* 59 */   private final ExtraCodecs.LateBoundIdMapper<String, LootContextArg<R>> sources = new ExtraCodecs.LateBoundIdMapper();
/*    */   
/*    */   public <T> ArgCodecBuilder<R> anyOf(T[] targets, Function<T, String> nameGetter, Function<T, ? extends LootContextArg<R>> argFactory) {
/* 65 */     for (T target : targets)
/* 66 */       this.sources.put(nameGetter.apply(target), argFactory.apply(target)); 
/* 68 */     return this;
/*    */   }
/*    */   
/*    */   public <T extends StringRepresentable> ArgCodecBuilder<R> anyOf(T[] targets, Function<T, ? extends LootContextArg<R>> argFactory) {
/* 72 */     return anyOf(targets, StringRepresentable::getSerializedName, argFactory);
/*    */   }
/*    */   
/*    */   public <T extends StringRepresentable & LootContextArg<? extends R>> ArgCodecBuilder<R> anyOf(T[] targets) {
/* 76 */     return anyOf((StringRepresentable[])targets, x$0 -> LootContextArg.cast((LootContextArg)x$0));
/*    */   }
/*    */   
/*    */   public ArgCodecBuilder<R> anyEntity(Function<? super ContextKey<? extends Entity>, ? extends LootContextArg<R>> function) {
/* 80 */     return anyOf(LootContext.EntityTarget.values(), target -> (LootContextArg)function.apply(target.contextParam()));
/*    */   }
/*    */   
/*    */   public ArgCodecBuilder<R> anyBlockEntity(Function<? super ContextKey<? extends BlockEntity>, ? extends LootContextArg<R>> function) {
/* 84 */     return anyOf(LootContext.BlockEntityTarget.values(), target -> (LootContextArg)function.apply(target.contextParam()));
/*    */   }
/*    */   
/*    */   public ArgCodecBuilder<R> anyItemStack(Function<? super ContextKey<? extends ItemInstance>, ? extends LootContextArg<R>> function) {
/* 88 */     return anyOf(LootContext.ItemStackTarget.values(), target -> (LootContextArg)function.apply(target.contextParam()));
/*    */   }
/*    */   
/*    */   private Codec<LootContextArg<R>> build() {
/* 92 */     return this.sources.codec((Codec)Codec.STRING);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\storage\loot\LootContextArg$ArgCodecBuilder.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */