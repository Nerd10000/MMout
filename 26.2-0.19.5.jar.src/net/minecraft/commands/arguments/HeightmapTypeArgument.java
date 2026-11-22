/*    */ package net.minecraft.commands.arguments;
/*    */ 
/*    */ import com.mojang.brigadier.context.CommandContext;
/*    */ import com.mojang.serialization.Codec;
/*    */ import java.util.Arrays;
/*    */ import java.util.Locale;
/*    */ import net.minecraft.commands.CommandSourceStack;
/*    */ import net.minecraft.util.StringRepresentable;
/*    */ import net.minecraft.world.level.levelgen.Heightmap;
/*    */ 
/*    */ public class HeightmapTypeArgument extends StringRepresentableArgument<Heightmap.Types> {
/*    */   private static final Codec<Heightmap.Types> LOWER_CASE_CODEC;
/*    */   
/*    */   static {
/* 14 */     LOWER_CASE_CODEC = (Codec<Heightmap.Types>)StringRepresentable.fromEnumWithMapping(HeightmapTypeArgument::keptTypes, s -> s.toLowerCase(Locale.ROOT));
/*    */   }
/*    */   
/*    */   private static Heightmap.Types[] keptTypes() {
/* 17 */     return (Heightmap.Types[])Arrays.<Heightmap.Types>stream(Heightmap.Types.values()).filter(Heightmap.Types::keepAfterWorldgen).toArray(x$0 -> new Heightmap.Types[x$0]);
/*    */   }
/*    */   
/*    */   private HeightmapTypeArgument() {
/* 21 */     super(LOWER_CASE_CODEC, HeightmapTypeArgument::keptTypes);
/*    */   }
/*    */   
/*    */   public static HeightmapTypeArgument heightmap() {
/* 25 */     return new HeightmapTypeArgument();
/*    */   }
/*    */   
/*    */   public static Heightmap.Types getHeightmap(CommandContext<CommandSourceStack> context, String name) {
/* 29 */     return (Heightmap.Types)context.getArgument(name, Heightmap.Types.class);
/*    */   }
/*    */   
/*    */   protected String convertId(String id) {
/* 34 */     return id.toLowerCase(Locale.ROOT);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\commands\arguments\HeightmapTypeArgument.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */