/*    */ package net.minecraft.commands.synchronization.brigadier;
/*    */ 
/*    */ import com.mojang.brigadier.arguments.ArgumentType;
/*    */ import com.mojang.brigadier.arguments.StringArgumentType;
/*    */ import net.minecraft.commands.CommandBuildContext;
/*    */ import net.minecraft.commands.synchronization.ArgumentTypeInfo;
/*    */ 
/*    */ public final class Template implements ArgumentTypeInfo.Template<StringArgumentType> {
/*    */   private final StringArgumentType.StringType type;
/*    */   
/*    */   public Template(StringArgumentType.StringType type) {
/* 14 */     this.type = type;
/*    */   }
/*    */   
/*    */   public StringArgumentType instantiate(CommandBuildContext context) {
/* 19 */     switch (StringArgumentSerializer.null.$SwitchMap$com$mojang$brigadier$arguments$StringArgumentType$StringType[this.type.ordinal()]) {
/*    */       default:
/* 19 */         throw new MatchException(null, null);
/*    */       case 1:
/*    */       
/*    */       case 2:
/*    */       
/*    */       case 3:
/*    */         break;
/*    */     } 
/* 19 */     return 
/*    */       
/* 22 */       StringArgumentType.greedyString();
/*    */   }
/*    */   
/*    */   public ArgumentTypeInfo<StringArgumentType, ?> type() {
/* 28 */     return StringArgumentSerializer.this;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\commands\synchronization\brigadier\StringArgumentSerializer$Template.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */