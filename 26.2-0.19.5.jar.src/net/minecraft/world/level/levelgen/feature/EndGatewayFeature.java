/*    */ package net.minecraft.world.level.levelgen.feature;
/*    */ 
/*    */ import com.mojang.serialization.Codec;
/*    */ import java.util.Iterator;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.world.level.LevelWriter;
/*    */ import net.minecraft.world.level.WorldGenLevel;
/*    */ import net.minecraft.world.level.block.Blocks;
/*    */ import net.minecraft.world.level.block.entity.BlockEntity;
/*    */ import net.minecraft.world.level.block.entity.TheEndGatewayBlockEntity;
/*    */ import net.minecraft.world.level.levelgen.feature.configurations.EndGatewayConfiguration;
/*    */ 
/*    */ public class EndGatewayFeature extends Feature<EndGatewayConfiguration> {
/*    */   public EndGatewayFeature(Codec<EndGatewayConfiguration> codec) {
/* 13 */     super(codec);
/*    */   }
/*    */   
/*    */   public boolean place(FeaturePlaceContext<EndGatewayConfiguration> context) {
/* 18 */     BlockPos origin = context.origin();
/* 19 */     WorldGenLevel level = context.level();
/* 20 */     EndGatewayConfiguration config = context.config();
/* 21 */     for (Iterator<BlockPos> iterator = BlockPos.betweenClosed(origin.offset(-1, -2, -1), origin.offset(1, 2, 1)).iterator(); iterator.hasNext(); ) {
/* 21 */       BlockPos pos = iterator.next();
/* 22 */       boolean sameX = (pos.getX() == origin.getX());
/* 23 */       boolean sameY = (pos.getY() == origin.getY());
/* 24 */       boolean sameZ = (pos.getZ() == origin.getZ());
/* 25 */       boolean end = (Math.abs(pos.getY() - origin.getY()) == 2);
/* 27 */       if (sameX && sameY && sameZ) {
/* 28 */         BlockPos immutable = pos.immutable();
/* 29 */         setBlock((LevelWriter)level, immutable, Blocks.END_GATEWAY.defaultBlockState());
/* 30 */         config.getExit().ifPresent(targetPos -> {
/*    */               BlockEntity exitEntity = level.getBlockEntity(immutable);
/*    */               if (exitEntity instanceof TheEndGatewayBlockEntity) {
/*    */                 TheEndGatewayBlockEntity exitGateway = (TheEndGatewayBlockEntity)exitEntity;
/*    */                 exitGateway.setExitPosition(targetPos, config.isExitExact());
/*    */               } 
/*    */             });
/*    */         continue;
/*    */       } 
/* 36 */       if (sameY) {
/* 37 */         setBlock((LevelWriter)level, pos, Blocks.AIR.defaultBlockState());
/*    */         continue;
/*    */       } 
/* 38 */       if (end && sameX && sameZ) {
/* 39 */         setBlock((LevelWriter)level, pos, Blocks.BEDROCK.defaultBlockState());
/*    */         continue;
/*    */       } 
/* 40 */       if ((!sameX && !sameZ) || end) {
/* 41 */         setBlock((LevelWriter)level, pos, Blocks.AIR.defaultBlockState());
/*    */         continue;
/*    */       } 
/* 43 */       setBlock((LevelWriter)level, pos, Blocks.BEDROCK.defaultBlockState());
/*    */     } 
/* 46 */     return true;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\levelgen\feature\EndGatewayFeature.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */