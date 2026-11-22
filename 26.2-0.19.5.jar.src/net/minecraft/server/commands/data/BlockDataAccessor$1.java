/*    */ package net.minecraft.server.commands.data;
/*    */ 
/*    */ import com.mojang.brigadier.arguments.ArgumentType;
/*    */ import com.mojang.brigadier.builder.ArgumentBuilder;
/*    */ import com.mojang.brigadier.context.CommandContext;
/*    */ import com.mojang.brigadier.exceptions.CommandSyntaxException;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.commands.CommandSourceStack;
/*    */ import net.minecraft.commands.Commands;
/*    */ import net.minecraft.commands.arguments.coordinates.BlockPosArgument;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.world.level.block.entity.BlockEntity;
/*    */ 
/*    */ class null implements DataCommands.DataProvider {
/*    */   public DataAccessor access(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
/* 38 */     BlockPos pos = BlockPosArgument.getLoadedBlockPos(context, argPrefix + "Pos");
/* 39 */     BlockEntity entity = ((CommandSourceStack)context.getSource()).getLevel().getBlockEntity(pos);
/* 40 */     if (entity == null)
/* 41 */       throw BlockDataAccessor.ERROR_NOT_A_BLOCK_ENTITY.create(); 
/* 43 */     return new BlockDataAccessor(entity, pos);
/*    */   }
/*    */   
/*    */   public ArgumentBuilder<CommandSourceStack, ?> wrap(ArgumentBuilder<CommandSourceStack, ?> parent, Function<ArgumentBuilder<CommandSourceStack, ?>, ArgumentBuilder<CommandSourceStack, ?>> function) {
/* 48 */     return parent.then(Commands.literal("block").then(function.apply(Commands.argument(argPrefix + "Pos", (ArgumentType)BlockPosArgument.blockPos()))));
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\server\commands\data\BlockDataAccessor$1.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */