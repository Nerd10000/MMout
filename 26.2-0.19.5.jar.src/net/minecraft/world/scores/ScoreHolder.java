/*    */ package net.minecraft.world.scores;
/*    */ 
/*    */ import com.mojang.authlib.GameProfile;
/*    */ import net.minecraft.network.chat.Component;
/*    */ import net.minecraft.network.chat.HoverEvent;
/*    */ import net.minecraft.network.chat.MutableComponent;
/*    */ import net.minecraft.network.chat.Style;
/*    */ 
/*    */ public interface ScoreHolder {
/*    */   public static final String WILDCARD_NAME = "*";
/*    */   
/* 11 */   public static final ScoreHolder WILDCARD = new ScoreHolder() {
/*    */       public String getScoreboardName() {
/* 14 */         return "*";
/*    */       }
/*    */     };
/*    */   
/*    */   default Component getDisplayName() {
/* 21 */     return null;
/*    */   }
/*    */   
/*    */   default Component getFeedbackDisplayName() {
/* 25 */     Component displayName = getDisplayName();
/* 26 */     if (displayName != null)
/* 27 */       return (Component)displayName.copy().withStyle(style -> style.withHoverEvent((HoverEvent)new HoverEvent.ShowText((Component)Component.literal(getScoreboardName())))); 
/* 29 */     return (Component)Component.literal(getScoreboardName());
/*    */   }
/*    */   
/*    */   static ScoreHolder forNameOnly(final String name) {
/* 33 */     if (name.equals("*"))
/* 34 */       return WILDCARD; 
/* 37 */     final MutableComponent feedbackName = Component.literal(name);
/* 38 */     return new ScoreHolder() {
/*    */         public String getScoreboardName() {
/* 41 */           return name;
/*    */         }
/*    */         
/*    */         public Component getFeedbackDisplayName() {
/* 46 */           return feedbackName;
/*    */         }
/*    */       };
/*    */   }
/*    */   
/*    */   static ScoreHolder fromGameProfile(GameProfile profile) {
/* 52 */     final String name = profile.name();
/* 53 */     return new ScoreHolder() {
/*    */         public String getScoreboardName() {
/* 56 */           return name;
/*    */         }
/*    */       };
/*    */   }
/*    */   
/*    */   String getScoreboardName();
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\scores\ScoreHolder.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */