/*    */ package net.minecraft.commands.arguments;
/*    */ 
/*    */ import com.mojang.brigadier.context.CommandContext;
/*    */ import net.minecraft.commands.CommandSourceStack;
/*    */ import net.minecraft.world.level.block.Rotation;
/*    */ 
/*    */ public class TemplateRotationArgument extends StringRepresentableArgument<Rotation> {
/*    */   private TemplateRotationArgument() {
/*  9 */     super(Rotation.CODEC, Rotation::values);
/*    */   }
/*    */   
/*    */   public static TemplateRotationArgument templateRotation() {
/* 13 */     return new TemplateRotationArgument();
/*    */   }
/*    */   
/*    */   public static Rotation getRotation(CommandContext<CommandSourceStack> context, String name) {
/* 17 */     return (Rotation)context.getArgument(name, Rotation.class);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\commands\arguments\TemplateRotationArgument.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */