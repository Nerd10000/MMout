/*    */ package net.minecraft.world.level.storage.loot.providers.nbt;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.Set;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.advancements.predicates.NbtPredicate;
/*    */ import net.minecraft.core.HolderLookup;
/*    */ import net.minecraft.nbt.Tag;
/*    */ import net.minecraft.util.context.ContextKey;
/*    */ import net.minecraft.world.entity.Entity;
/*    */ import net.minecraft.world.level.block.entity.BlockEntity;
/*    */ import net.minecraft.world.level.storage.loot.LootContext;
/*    */ import net.minecraft.world.level.storage.loot.LootContextArg;
/*    */ 
/*    */ public class ContextNbtProvider implements NbtProvider {
/*    */   private static final Codec<LootContextArg<Tag>> GETTER_CODEC;
/*    */   
/*    */   public static final MapCodec<ContextNbtProvider> MAP_CODEC;
/*    */   
/*    */   public static final Codec<ContextNbtProvider> INLINE_CODEC;
/*    */   
/*    */   private final LootContextArg<Tag> source;
/*    */   
/*    */   static {
/* 18 */     GETTER_CODEC = LootContextArg.createArgCodec(builder -> builder.anyBlockEntity(BlockEntitySource::new).anyEntity(EntitySource::new));
/* 23 */     MAP_CODEC = RecordCodecBuilder.mapCodec(i -> i.group((App)GETTER_CODEC.fieldOf("target").forGetter(())).apply((Applicative)i, ContextNbtProvider::new));
/* 27 */     INLINE_CODEC = GETTER_CODEC.xmap(ContextNbtProvider::new, p -> p.source);
/*    */   }
/*    */   
/*    */   private ContextNbtProvider(LootContextArg<Tag> source) {
/* 32 */     this.source = source;
/*    */   }
/*    */   
/*    */   public MapCodec<ContextNbtProvider> codec() {
/* 37 */     return MAP_CODEC;
/*    */   }
/*    */   
/*    */   public Tag get(LootContext context) {
/* 42 */     return (Tag)this.source.get(context);
/*    */   }
/*    */   
/*    */   public Set<ContextKey<?>> getReferencedContextParams() {
/* 47 */     return Set.of(this.source.contextParam());
/*    */   }
/*    */   
/*    */   public static NbtProvider forContextEntity(LootContext.EntityTarget source) {
/* 51 */     return new ContextNbtProvider((LootContextArg<Tag>)new EntitySource(source.contextParam()));
/*    */   }
/*    */   
/*    */   private static final class BlockEntitySource extends Record implements LootContextArg.Getter<BlockEntity, Tag> {
/*    */     private final ContextKey<? extends BlockEntity> contextParam;
/*    */     
/*    */     private BlockEntitySource(ContextKey<? extends BlockEntity> contextParam) {
/* 54 */       this.contextParam = contextParam;
/*    */     }
/*    */     
/*    */     public final String toString() {
/*    */       // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> toString : (Lnet/minecraft/world/level/storage/loot/providers/nbt/ContextNbtProvider$BlockEntitySource;)Ljava/lang/String;
/*    */       //   6: areturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #54	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	7	0	this	Lnet/minecraft/world/level/storage/loot/providers/nbt/ContextNbtProvider$BlockEntitySource;
/*    */     }
/*    */     
/*    */     public final int hashCode() {
/*    */       // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/level/storage/loot/providers/nbt/ContextNbtProvider$BlockEntitySource;)I
/*    */       //   6: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #54	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	7	0	this	Lnet/minecraft/world/level/storage/loot/providers/nbt/ContextNbtProvider$BlockEntitySource;
/*    */     }
/*    */     
/*    */     public final boolean equals(Object o) {
/*    */       // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: aload_1
/*    */       //   2: <illegal opcode> equals : (Lnet/minecraft/world/level/storage/loot/providers/nbt/ContextNbtProvider$BlockEntitySource;Ljava/lang/Object;)Z
/*    */       //   7: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #54	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	8	0	this	Lnet/minecraft/world/level/storage/loot/providers/nbt/ContextNbtProvider$BlockEntitySource;
/*    */       //   0	8	1	o	Ljava/lang/Object;
/*    */     }
/*    */     
/*    */     public ContextKey<? extends BlockEntity> contextParam() {
/* 54 */       return this.contextParam;
/*    */     }
/*    */     
/*    */     public Tag get(BlockEntity blockEntity) {
/* 59 */       return (Tag)blockEntity.saveWithFullMetadata((HolderLookup.Provider)blockEntity.getLevel().registryAccess());
/*    */     }
/*    */   }
/*    */   
/*    */   private static final class EntitySource extends Record implements LootContextArg.Getter<Entity, Tag> {
/*    */     private final ContextKey<? extends Entity> contextParam;
/*    */     
/*    */     private EntitySource(ContextKey<? extends Entity> contextParam) {
/* 63 */       this.contextParam = contextParam;
/*    */     }
/*    */     
/*    */     public final String toString() {
/*    */       // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> toString : (Lnet/minecraft/world/level/storage/loot/providers/nbt/ContextNbtProvider$EntitySource;)Ljava/lang/String;
/*    */       //   6: areturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #63	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	7	0	this	Lnet/minecraft/world/level/storage/loot/providers/nbt/ContextNbtProvider$EntitySource;
/*    */     }
/*    */     
/*    */     public final int hashCode() {
/*    */       // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/level/storage/loot/providers/nbt/ContextNbtProvider$EntitySource;)I
/*    */       //   6: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #63	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	7	0	this	Lnet/minecraft/world/level/storage/loot/providers/nbt/ContextNbtProvider$EntitySource;
/*    */     }
/*    */     
/*    */     public final boolean equals(Object o) {
/*    */       // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: aload_1
/*    */       //   2: <illegal opcode> equals : (Lnet/minecraft/world/level/storage/loot/providers/nbt/ContextNbtProvider$EntitySource;Ljava/lang/Object;)Z
/*    */       //   7: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #63	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	8	0	this	Lnet/minecraft/world/level/storage/loot/providers/nbt/ContextNbtProvider$EntitySource;
/*    */       //   0	8	1	o	Ljava/lang/Object;
/*    */     }
/*    */     
/*    */     public ContextKey<? extends Entity> contextParam() {
/* 63 */       return this.contextParam;
/*    */     }
/*    */     
/*    */     public Tag get(Entity entity) {
/* 68 */       return (Tag)NbtPredicate.getEntityTagToCompare(entity);
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\storage\loot\providers\nbt\ContextNbtProvider.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */