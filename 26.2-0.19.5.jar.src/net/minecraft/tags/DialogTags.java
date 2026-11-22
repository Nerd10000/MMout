/*    */ package net.minecraft.tags;
/*    */ 
/*    */ import net.minecraft.core.registries.Registries;
/*    */ import net.minecraft.resources.Identifier;
/*    */ import net.minecraft.server.dialog.Dialog;
/*    */ 
/*    */ public class DialogTags {
/* 11 */   public static final TagKey<Dialog> PAUSE_SCREEN_ADDITIONS = create("pause_screen_additions");
/*    */   
/* 12 */   public static final TagKey<Dialog> QUICK_ACTIONS = create("quick_actions");
/*    */   
/*    */   private static TagKey<Dialog> create(String name) {
/* 15 */     return TagKey.create(Registries.DIALOG, Identifier.withDefaultNamespace(name));
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\tags\DialogTags.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */