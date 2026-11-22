/*    */ package net.minecraft.client.gui.components.debug;
/*    */ 
/*    */ import com.mojang.blaze3d.platform.GLX;
/*    */ import com.mojang.blaze3d.systems.DeviceInfo;
/*    */ import com.mojang.blaze3d.systems.DeviceType;
/*    */ import com.mojang.blaze3d.systems.RenderSystem;
/*    */ import java.util.List;
/*    */ import java.util.Locale;
/*    */ import net.minecraft.client.Minecraft;
/*    */ import net.minecraft.resources.Identifier;
/*    */ import net.minecraft.world.level.Level;
/*    */ import net.minecraft.world.level.chunk.LevelChunk;
/*    */ 
/*    */ public class DebugEntrySystemSpecs implements DebugScreenEntry {
/* 17 */   private static final Identifier GROUP = Identifier.withDefaultNamespace("system");
/*    */   
/*    */   public void display(DebugScreenDisplayer displayer, Level serverOrClientLevel, LevelChunk clientChunk, LevelChunk serverChunk) {
/* 21 */     DeviceInfo deviceInfo = RenderSystem.getDevice().getDeviceInfo();
/* 23 */     displayer.addToGroup(GROUP, List.of(
/* 24 */           String.format(Locale.ROOT, "Java: %s", new Object[] { System.getProperty("java.version") }), String.format(Locale.ROOT, "CPU: %s", new Object[] { GLX._getCpuInfo() }), String.format(Locale.ROOT, "Display: %dx%d (%s)", new Object[] { Integer.valueOf(Minecraft.getInstance().getWindow().getWidth()), Integer.valueOf(Minecraft.getInstance().getWindow().getHeight()), deviceInfo.vendorName() }), String.format(Locale.ROOT, "%s%s", new Object[] { deviceInfo.name(), typeName(deviceInfo.type()) }), String.format(Locale.ROOT, "%s %s", new Object[] { deviceInfo.backendName(), firstLine(deviceInfo.driverInfo()) })));
/*    */   }
/*    */   
/*    */   private String firstLine(String value) {
/* 33 */     return value.lines().findFirst().orElse(value);
/*    */   }
/*    */   
/*    */   private String typeName(DeviceType type) {
/* 37 */     switch (type) {
/*    */       default:
/* 37 */         throw new MatchException(null, null);
/*    */       case OTHER:
/*    */       
/*    */       case INTEGRATED:
/*    */       
/*    */       case DISCRETE:
/*    */       
/*    */       case VIRTUAL:
/*    */       
/*    */       case CPU:
/*    */         break;
/*    */     } 
/* 37 */     return 
/*    */       
/* 42 */       " (software)";
/*    */   }
/*    */   
/*    */   public boolean isAllowed(boolean reducedDebugInfo) {
/* 48 */     return true;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\gui\components\debug\DebugEntrySystemSpecs.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */