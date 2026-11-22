/*    */ package net.minecraft.client.renderer.special;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.function.BiFunction;
/*    */ import net.minecraft.client.model.geom.ModelLayerLocation;
/*    */ import net.minecraft.client.model.geom.ModelLayers;
/*    */ import net.minecraft.client.model.object.statue.CopperGolemStatueModel;
/*    */ import net.minecraft.resources.Identifier;
/*    */ import net.minecraft.world.entity.animal.golem.CopperGolemOxidationLevels;
/*    */ import net.minecraft.world.level.block.CopperGolemStatueBlock;
/*    */ import net.minecraft.world.level.block.WeatheringCopper;
/*    */ 
/*    */ public final class Unbaked extends Record implements NoDataSpecialModelRenderer.Unbaked {
/*    */   private final Identifier texture;
/*    */   
/*    */   private final CopperGolemStatueBlock.Pose pose;
/*    */   
/*    */   public static final MapCodec<Unbaked> MAP_CODEC;
/*    */   
/*    */   public Unbaked(Identifier texture, CopperGolemStatueBlock.Pose pose) {
/* 28 */     this.texture = texture;
/* 28 */     this.pose = pose;
/*    */   }
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/client/renderer/special/CopperGolemStatueSpecialRenderer$Unbaked;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #28	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/client/renderer/special/CopperGolemStatueSpecialRenderer$Unbaked;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/renderer/special/CopperGolemStatueSpecialRenderer$Unbaked;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #28	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/client/renderer/special/CopperGolemStatueSpecialRenderer$Unbaked;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/client/renderer/special/CopperGolemStatueSpecialRenderer$Unbaked;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #28	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/client/renderer/special/CopperGolemStatueSpecialRenderer$Unbaked;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */   }
/*    */   
/*    */   public Identifier texture() {
/* 28 */     return this.texture;
/*    */   }
/*    */   
/*    */   public CopperGolemStatueBlock.Pose pose() {
/* 28 */     return this.pose;
/*    */   }
/*    */   
/*    */   static {
/* 29 */     MAP_CODEC = RecordCodecBuilder.mapCodec(i -> i.group((App)Identifier.CODEC.fieldOf("texture").forGetter(Unbaked::texture), (App)CopperGolemStatueBlock.Pose.CODEC.fieldOf("pose").forGetter(Unbaked::pose)).apply((Applicative)i, Unbaked::new));
/*    */   }
/*    */   
/*    */   public Unbaked(WeatheringCopper.WeatherState state, CopperGolemStatueBlock.Pose pose) {
/* 35 */     this(CopperGolemOxidationLevels.getOxidationLevel(state).texture(), pose);
/*    */   }
/*    */   
/*    */   public MapCodec<Unbaked> type() {
/* 40 */     return MAP_CODEC;
/*    */   }
/*    */   
/*    */   public CopperGolemStatueSpecialRenderer bake(SpecialModelRenderer.BakingContext context) {
/* 45 */     CopperGolemStatueModel model = new CopperGolemStatueModel(context.entityModelSet().bakeLayer(getModel(this.pose)));
/* 46 */     return new CopperGolemStatueSpecialRenderer(model, this.texture);
/*    */   }
/*    */   
/*    */   private static ModelLayerLocation getModel(CopperGolemStatueBlock.Pose pose) {
/* 50 */     switch (CopperGolemStatueSpecialRenderer.null.$SwitchMap$net$minecraft$world$level$block$CopperGolemStatueBlock$Pose[pose.ordinal()]) {
/*    */       default:
/* 50 */         throw new MatchException(null, null);
/*    */       case 1:
/*    */       
/*    */       case 2:
/*    */       
/*    */       case 3:
/*    */       
/*    */       case 4:
/*    */         break;
/*    */     } 
/* 50 */     return 
/*    */       
/* 54 */       ModelLayers.COPPER_GOLEM_RUNNING;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\renderer\special\CopperGolemStatueSpecialRenderer$Unbaked.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */