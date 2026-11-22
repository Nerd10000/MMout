/*     */ package net.minecraft.client.gui;
/*     */ 
/*     */ import net.minecraft.resources.Identifier;
/*     */ import net.minecraft.world.effect.MobEffects;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ 
/*     */ enum HeartType {
/* 881 */   CONTAINER(
/* 882 */     Identifier.withDefaultNamespace("hud/heart/container"), 
/* 883 */     Identifier.withDefaultNamespace("hud/heart/container_blinking"), 
/* 884 */     Identifier.withDefaultNamespace("hud/heart/container"), 
/* 885 */     Identifier.withDefaultNamespace("hud/heart/container_blinking"), 
/* 886 */     Identifier.withDefaultNamespace("hud/heart/container_hardcore"), 
/* 887 */     Identifier.withDefaultNamespace("hud/heart/container_hardcore_blinking"), 
/* 888 */     Identifier.withDefaultNamespace("hud/heart/container_hardcore"), 
/* 889 */     Identifier.withDefaultNamespace("hud/heart/container_hardcore_blinking")),
/* 891 */   NORMAL(
/* 892 */     Identifier.withDefaultNamespace("hud/heart/full"), 
/* 893 */     Identifier.withDefaultNamespace("hud/heart/full_blinking"), 
/* 894 */     Identifier.withDefaultNamespace("hud/heart/half"), 
/* 895 */     Identifier.withDefaultNamespace("hud/heart/half_blinking"), 
/* 896 */     Identifier.withDefaultNamespace("hud/heart/hardcore_full"), 
/* 897 */     Identifier.withDefaultNamespace("hud/heart/hardcore_full_blinking"), 
/* 898 */     Identifier.withDefaultNamespace("hud/heart/hardcore_half"), 
/* 899 */     Identifier.withDefaultNamespace("hud/heart/hardcore_half_blinking")),
/* 901 */   POISIONED(
/* 902 */     Identifier.withDefaultNamespace("hud/heart/poisoned_full"), 
/* 903 */     Identifier.withDefaultNamespace("hud/heart/poisoned_full_blinking"), 
/* 904 */     Identifier.withDefaultNamespace("hud/heart/poisoned_half"), 
/* 905 */     Identifier.withDefaultNamespace("hud/heart/poisoned_half_blinking"), 
/* 906 */     Identifier.withDefaultNamespace("hud/heart/poisoned_hardcore_full"), 
/* 907 */     Identifier.withDefaultNamespace("hud/heart/poisoned_hardcore_full_blinking"), 
/* 908 */     Identifier.withDefaultNamespace("hud/heart/poisoned_hardcore_half"), 
/* 909 */     Identifier.withDefaultNamespace("hud/heart/poisoned_hardcore_half_blinking")),
/* 911 */   WITHERED(
/* 912 */     Identifier.withDefaultNamespace("hud/heart/withered_full"), 
/* 913 */     Identifier.withDefaultNamespace("hud/heart/withered_full_blinking"), 
/* 914 */     Identifier.withDefaultNamespace("hud/heart/withered_half"), 
/* 915 */     Identifier.withDefaultNamespace("hud/heart/withered_half_blinking"), 
/* 916 */     Identifier.withDefaultNamespace("hud/heart/withered_hardcore_full"), 
/* 917 */     Identifier.withDefaultNamespace("hud/heart/withered_hardcore_full_blinking"), 
/* 918 */     Identifier.withDefaultNamespace("hud/heart/withered_hardcore_half"), 
/* 919 */     Identifier.withDefaultNamespace("hud/heart/withered_hardcore_half_blinking")),
/* 921 */   ABSORBING(
/* 922 */     Identifier.withDefaultNamespace("hud/heart/absorbing_full"), 
/* 923 */     Identifier.withDefaultNamespace("hud/heart/absorbing_full_blinking"), 
/* 924 */     Identifier.withDefaultNamespace("hud/heart/absorbing_half"), 
/* 925 */     Identifier.withDefaultNamespace("hud/heart/absorbing_half_blinking"), 
/* 926 */     Identifier.withDefaultNamespace("hud/heart/absorbing_hardcore_full"), 
/* 927 */     Identifier.withDefaultNamespace("hud/heart/absorbing_hardcore_full_blinking"), 
/* 928 */     Identifier.withDefaultNamespace("hud/heart/absorbing_hardcore_half"), 
/* 929 */     Identifier.withDefaultNamespace("hud/heart/absorbing_hardcore_half_blinking")),
/* 931 */   FROZEN(
/* 932 */     Identifier.withDefaultNamespace("hud/heart/frozen_full"), 
/* 933 */     Identifier.withDefaultNamespace("hud/heart/frozen_full_blinking"), 
/* 934 */     Identifier.withDefaultNamespace("hud/heart/frozen_half"), 
/* 935 */     Identifier.withDefaultNamespace("hud/heart/frozen_half_blinking"), 
/* 936 */     Identifier.withDefaultNamespace("hud/heart/frozen_hardcore_full"), 
/* 937 */     Identifier.withDefaultNamespace("hud/heart/frozen_hardcore_full_blinking"), 
/* 938 */     Identifier.withDefaultNamespace("hud/heart/frozen_hardcore_half"), 
/* 939 */     Identifier.withDefaultNamespace("hud/heart/frozen_hardcore_half_blinking"));
/*     */   
/*     */   private final Identifier full;
/*     */   
/*     */   private final Identifier fullBlinking;
/*     */   
/*     */   private final Identifier half;
/*     */   
/*     */   private final Identifier halfBlinking;
/*     */   
/*     */   private final Identifier hardcoreFull;
/*     */   
/*     */   private final Identifier hardcoreFullBlinking;
/*     */   
/*     */   private final Identifier hardcoreHalf;
/*     */   
/*     */   private final Identifier hardcoreHalfBlinking;
/*     */   
/*     */   HeartType(Identifier full, Identifier fullBlinking, Identifier half, Identifier halfBlinking, Identifier hardcoreFull, Identifier hardcoreFullBlinking, Identifier hardcoreHalf, Identifier hardcoreHalfBlinking) {
/* 953 */     this.full = full;
/* 954 */     this.fullBlinking = fullBlinking;
/* 955 */     this.half = half;
/* 956 */     this.halfBlinking = halfBlinking;
/* 957 */     this.hardcoreFull = hardcoreFull;
/* 958 */     this.hardcoreFullBlinking = hardcoreFullBlinking;
/* 959 */     this.hardcoreHalf = hardcoreHalf;
/* 960 */     this.hardcoreHalfBlinking = hardcoreHalfBlinking;
/*     */   }
/*     */   
/*     */   public Identifier getSprite(boolean isHardcore, boolean isHalf, boolean isBlink) {
/* 964 */     if (!isHardcore) {
/* 965 */       if (isHalf)
/* 966 */         return isBlink ? this.halfBlinking : this.half; 
/* 968 */       return isBlink ? this.fullBlinking : this.full;
/*     */     } 
/* 971 */     if (isHalf)
/* 972 */       return isBlink ? this.hardcoreHalfBlinking : this.hardcoreHalf; 
/* 974 */     return isBlink ? this.hardcoreFullBlinking : this.hardcoreFull;
/*     */   }
/*     */   
/*     */   private static HeartType forPlayer(Player player) {
/*     */     HeartType type;
/* 981 */     if (player.hasEffect(MobEffects.POISON)) {
/* 982 */       type = POISIONED;
/* 983 */     } else if (player.hasEffect(MobEffects.WITHER)) {
/* 984 */       type = WITHERED;
/* 985 */     } else if (player.isFullyFrozen()) {
/* 986 */       type = FROZEN;
/*     */     } else {
/* 988 */       type = NORMAL;
/*     */     } 
/* 990 */     return type;
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\gui\Hud$HeartType.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */