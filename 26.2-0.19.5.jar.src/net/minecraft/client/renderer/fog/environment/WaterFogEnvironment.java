/*    */ package net.minecraft.client.renderer.fog.environment;
/*    */ 
/*    */ import net.minecraft.client.Camera;
/*    */ import net.minecraft.client.DeltaTracker;
/*    */ import net.minecraft.client.multiplayer.ClientLevel;
/*    */ import net.minecraft.client.player.LocalPlayer;
/*    */ import net.minecraft.client.renderer.fog.FogData;
/*    */ import net.minecraft.world.attribute.EnvironmentAttributes;
/*    */ import net.minecraft.world.entity.Entity;
/*    */ import net.minecraft.world.level.material.FogType;
/*    */ 
/*    */ public class WaterFogEnvironment extends FogEnvironment {
/*    */   public void setupFog(FogData fog, Camera camera, ClientLevel level, float renderDistance, DeltaTracker deltaTracker) {
/* 16 */     float partialTicks = deltaTracker.getGameTimeDeltaPartialTick(false);
/* 17 */     fog.environmentalStart = ((Float)camera.attributeProbe().getValue(EnvironmentAttributes.WATER_FOG_START_DISTANCE, partialTicks)).floatValue();
/* 18 */     fog.environmentalEnd = ((Float)camera.attributeProbe().getValue(EnvironmentAttributes.WATER_FOG_END_DISTANCE, partialTicks)).floatValue();
/* 19 */     Entity entity = camera.entity();
/* 19 */     if (entity instanceof LocalPlayer) {
/* 19 */       LocalPlayer player = (LocalPlayer)entity;
/* 20 */       fog.environmentalEnd *= Math.max(0.25F, player.getWaterVision());
/*    */     } 
/* 22 */     fog.skyEnd = fog.environmentalEnd;
/* 23 */     fog.cloudEnd = fog.environmentalEnd;
/*    */   }
/*    */   
/*    */   public boolean isApplicable(FogType fogType, Entity entity) {
/* 28 */     return (fogType == FogType.WATER);
/*    */   }
/*    */   
/*    */   public int getBaseColor(ClientLevel level, Camera camera, int renderDistance, float partialTicks) {
/* 33 */     return ((Integer)camera.attributeProbe().getValue(EnvironmentAttributes.WATER_FOG_COLOR, partialTicks)).intValue();
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\renderer\fog\environment\WaterFogEnvironment.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */