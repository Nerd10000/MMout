/*    */ package net.minecraft.world.item.component;
/*    */ 
/*    */ import com.mojang.datafixers.util.Pair;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.DataResult;
/*    */ import com.mojang.serialization.DynamicOps;
/*    */ import net.minecraft.nbt.CompoundTag;
/*    */ import net.minecraft.nbt.NbtOps;
/*    */ import net.minecraft.nbt.Tag;
/*    */ import net.minecraft.resources.RegistryOps;
/*    */ 
/*    */ class null implements Codec<TypedEntityData<T>> {
/*    */   public <V> DataResult<Pair<TypedEntityData<T>, V>> decode(DynamicOps<V> ops, V input) {
/* 42 */     return CustomData.COMPOUND_TAG_CODEC.decode(ops, input).flatMap(pair -> {
/*    */           CompoundTag tagWithoutType = ((CompoundTag)pair.getFirst()).copy();
/*    */           Tag typeTag = tagWithoutType.remove("id");
/*    */           return (typeTag == null) ? DataResult.error(()) : typeCodec.parse(asNbtOps(ops), typeTag).map(());
/*    */         });
/*    */   }
/*    */   
/*    */   public <V> DataResult<V> encode(TypedEntityData<T> input, DynamicOps<V> ops, V prefix) {
/* 54 */     return typeCodec.encodeStart(asNbtOps(ops), input.type).flatMap(typeTag -> {
/*    */           CompoundTag tag = input.tag.copy();
/*    */           tag.put("id", typeTag);
/*    */           return CustomData.COMPOUND_TAG_CODEC.encode(tag, ops, prefix);
/*    */         });
/*    */   }
/*    */   
/*    */   private static <T> DynamicOps<Tag> asNbtOps(DynamicOps<T> ops) {
/* 62 */     if (ops instanceof RegistryOps) {
/* 62 */       RegistryOps<T> registryOps = (RegistryOps<T>)ops;
/* 63 */       return (DynamicOps<Tag>)registryOps.withParent((DynamicOps)NbtOps.INSTANCE);
/*    */     } 
/* 65 */     return (DynamicOps<Tag>)NbtOps.INSTANCE;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\item\component\TypedEntityData$1.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */