/*    */ package net.minecraft.network.chat.contents.data;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.function.Function;
/*    */ import java.util.stream.Stream;
/*    */ import net.minecraft.commands.CommandSourceStack;
/*    */ import net.minecraft.nbt.CompoundTag;
/*    */ import net.minecraft.resources.Identifier;
/*    */ 
/*    */ public final class StorageDataSource extends Record implements DataSource {
/*    */   private final Identifier id;
/*    */   
/*    */   public static final MapCodec<StorageDataSource> MAP_CODEC;
/*    */   
/*    */   public StorageDataSource(Identifier id) {
/* 11 */     this.id = id;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/network/chat/contents/data/StorageDataSource;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #11	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/network/chat/contents/data/StorageDataSource;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/network/chat/contents/data/StorageDataSource;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #11	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/network/chat/contents/data/StorageDataSource;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */   }
/*    */   
/*    */   public Identifier id() {
/* 11 */     return this.id;
/*    */   }
/*    */   
/*    */   static {
/* 12 */     MAP_CODEC = RecordCodecBuilder.mapCodec(i -> i.group((App)Identifier.CODEC.fieldOf("storage").forGetter(StorageDataSource::id)).apply((Applicative)i, StorageDataSource::new));
/*    */   }
/*    */   
/*    */   public Stream<CompoundTag> getData(CommandSourceStack sender) {
/* 18 */     CompoundTag tag = sender.getServer().getCommandStorage().get(this.id);
/* 19 */     return Stream.of(tag);
/*    */   }
/*    */   
/*    */   public MapCodec<StorageDataSource> codec() {
/* 24 */     return MAP_CODEC;
/*    */   }
/*    */   
/*    */   public String toString() {
/* 29 */     return "storage=" + String.valueOf(this.id);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\network\chat\contents\data\StorageDataSource.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */