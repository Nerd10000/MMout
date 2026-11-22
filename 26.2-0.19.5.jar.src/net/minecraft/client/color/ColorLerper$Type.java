/*    */ package net.minecraft.client.color;
/*    */ 
/*    */ import com.google.common.collect.Maps;
/*    */ import java.util.Arrays;
/*    */ import java.util.Map;
/*    */ import java.util.stream.Collectors;
/*    */ import net.minecraft.world.item.DyeColor;
/*    */ 
/*    */ public enum Type {
/* 47 */   SHEEP(25, DyeColor.values(), 0.75F),
/* 48 */   MUSIC_NOTE(30, ColorLerper.MUSIC_NOTE_COLORS, 1.25F);
/*    */   
/*    */   private final int colorDuration;
/*    */   
/*    */   private final Map<DyeColor, Integer> colorByDye;
/*    */   
/*    */   private final DyeColor[] colors;
/*    */   
/*    */   Type(int colorDuration, DyeColor[] colors, float brightness) {
/* 55 */     this.colorDuration = colorDuration;
/* 56 */     this.colorByDye = Maps.newHashMap((Map)Arrays.<DyeColor>stream(colors).collect(Collectors.toMap(d -> d, color -> Integer.valueOf(ColorLerper.getModifiedColor(color, brightness)))));
/* 57 */     this.colors = colors;
/*    */   }
/*    */   
/*    */   public final int getColor(DyeColor dyeColor) {
/* 61 */     return ((Integer)this.colorByDye.get(dyeColor)).intValue();
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\color\ColorLerper$Type.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */