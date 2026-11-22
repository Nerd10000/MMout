/*    */ package net.minecraft.network.chat.contents.data;
/*    */ 
/*    */ import com.mojang.brigadier.exceptions.CommandSyntaxException;
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.List;
/*    */ import java.util.function.Function;
/*    */ import java.util.stream.Stream;
/*    */ import net.minecraft.advancements.predicates.NbtPredicate;
/*    */ import net.minecraft.commands.CommandSourceStack;
/*    */ import net.minecraft.commands.arguments.selector.EntitySelector;
/*    */ import net.minecraft.nbt.CompoundTag;
/*    */ import net.minecraft.util.CompilableString;
/*    */ import net.minecraft.world.entity.Entity;
/*    */ 
/*    */ public final class EntityDataSource extends Record implements DataSource {
/*    */   private final CompilableString<EntitySelector> selector;
/*    */   
/*    */   public static final MapCodec<EntityDataSource> MAP_CODEC;
/*    */   
/*    */   public EntityDataSource(CompilableString<EntitySelector> selector) {
/* 16 */     this.selector = selector;
/*    */   }
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/network/chat/contents/data/EntityDataSource;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #16	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/network/chat/contents/data/EntityDataSource;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/network/chat/contents/data/EntityDataSource;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #16	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/network/chat/contents/data/EntityDataSource;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/network/chat/contents/data/EntityDataSource;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #16	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/network/chat/contents/data/EntityDataSource;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */   }
/*    */   
/*    */   public CompilableString<EntitySelector> selector() {
/* 16 */     return this.selector;
/*    */   }
/*    */   
/*    */   static {
/* 17 */     MAP_CODEC = RecordCodecBuilder.mapCodec(i -> i.group((App)EntitySelector.COMPILABLE_CODEC.fieldOf("entity").forGetter(EntityDataSource::selector)).apply((Applicative)i, EntityDataSource::new));
/*    */   }
/*    */   
/*    */   public Stream<CompoundTag> getData(CommandSourceStack sender) throws CommandSyntaxException {
/* 23 */     List<? extends Entity> entities = ((EntitySelector)this.selector.compiled()).findEntities(sender);
/* 24 */     return entities.stream().map(NbtPredicate::getEntityTagToCompare);
/*    */   }
/*    */   
/*    */   public MapCodec<EntityDataSource> codec() {
/* 29 */     return MAP_CODEC;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\network\chat\contents\data\EntityDataSource.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */