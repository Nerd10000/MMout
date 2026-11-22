/*    */ package net.minecraft.network.chat.contents.data;
/*    */ 
/*    */ import com.mojang.brigadier.StringReader;
/*    */ import com.mojang.brigadier.exceptions.CommandSyntaxException;
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.function.Function;
/*    */ import java.util.stream.Stream;
/*    */ import net.minecraft.commands.CommandSourceStack;
/*    */ import net.minecraft.commands.arguments.coordinates.BlockPosArgument;
/*    */ import net.minecraft.commands.arguments.coordinates.Coordinates;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.HolderLookup;
/*    */ import net.minecraft.nbt.CompoundTag;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.util.CompilableString;
/*    */ import net.minecraft.world.level.block.entity.BlockEntity;
/*    */ 
/*    */ public final class BlockDataSource extends Record implements DataSource {
/*    */   private final CompilableString<Coordinates> coordinates;
/*    */   
/*    */   public BlockDataSource(CompilableString<Coordinates> coordinates) {
/* 19 */     this.coordinates = coordinates;
/*    */   }
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/network/chat/contents/data/BlockDataSource;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #19	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/network/chat/contents/data/BlockDataSource;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/network/chat/contents/data/BlockDataSource;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #19	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/network/chat/contents/data/BlockDataSource;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/network/chat/contents/data/BlockDataSource;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #19	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/network/chat/contents/data/BlockDataSource;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */   }
/*    */   
/*    */   public CompilableString<Coordinates> coordinates() {
/* 19 */     return this.coordinates;
/*    */   }
/*    */   
/* 20 */   public static final Codec<CompilableString<Coordinates>> BLOCK_POS_CODEC = CompilableString.codec((Function)new CompilableString.CommandParserHelper<Coordinates>() {
/*    */         protected Coordinates parse(StringReader reader) throws CommandSyntaxException {
/* 23 */           return BlockPosArgument.blockPos().parse(reader);
/*    */         }
/*    */         
/*    */         protected String errorMessage(String original, CommandSyntaxException exception) {
/* 28 */           return "Invalid coordinates path: " + original + ": " + exception.getMessage();
/*    */         }
/*    */       });
/*    */   
/*    */   public static final MapCodec<BlockDataSource> MAP_CODEC;
/*    */   
/*    */   static {
/* 32 */     MAP_CODEC = RecordCodecBuilder.mapCodec(i -> i.group((App)BLOCK_POS_CODEC.fieldOf("block").forGetter(BlockDataSource::coordinates)).apply((Applicative)i, BlockDataSource::new));
/*    */   }
/*    */   
/*    */   public Stream<CompoundTag> getData(CommandSourceStack sender) {
/* 38 */     ServerLevel level = sender.getLevel();
/* 39 */     BlockPos pos = ((Coordinates)this.coordinates.compiled()).getBlockPos(sender);
/* 40 */     if (level.isLoaded(pos)) {
/* 41 */       BlockEntity entity = level.getBlockEntity(pos);
/* 43 */       if (entity != null)
/* 44 */         return Stream.of(entity.saveWithFullMetadata((HolderLookup.Provider)sender.registryAccess())); 
/*    */     } 
/* 47 */     return Stream.empty();
/*    */   }
/*    */   
/*    */   public MapCodec<BlockDataSource> codec() {
/* 52 */     return MAP_CODEC;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\network\chat\contents\data\BlockDataSource.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */