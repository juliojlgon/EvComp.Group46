setwd(dir = "~/UvA/EC/EvComp.Group46/stats/")
raw <- read.csv("tuneRun2.csv", header = TRUE)
library(Boruta)
require(ggplot2)
theme_set(theme_classic())
require(dplyr)

boruta_output <- Boruta(Fitness ~., data = raw, doTrace = 2)
boruta_signif <- names(boruta_output$finalDecision[boruta_output$finalDecision %in% c("Confirmed", "Tentative")])
plot(boruta_output, cex.axis=.7, las=2, xlab="", main="Variable Importance")

p1 <- ggplot(raw,
             aes(x = factor(MutationChance),
                 y = mean(Fitness))) +
  geom_histogram(stat = "identity")
plot(p1)

mc_density <- ggplot(raw, aes(Fitness))+
  geom_density(aes(fill=factor(MutationChance)), alpha = 0.6)
plot(mc_density)

cc_density <- ggplot(raw, aes(Fitness))+
  geom_density(aes(fill=factor(CrossoverChance)), alpha = 0.6)
plot(cc_density)

lr_density <- ggplot(raw, aes(Fitness))+
  geom_density(aes(fill=factor(LearningRate)), alpha = 0.6)
plot(lr_density)

library(ggcorrplot)
corr <- round(cor(raw), 3)
ggcorrplot(corr, hc.order = TRUE, 
           type = "lower", 
           lab = TRUE, 
           lab_size = 3, 
           method="circle", 
           colors = c("tomato2", "white", "springgreen3"), 
           title="Correlogram of testRun", 
           ggtheme=theme_bw)

raw_bymc <- raw %>% group_by(MutationChance) %>% summarise(avg_Fitness = mean(Fitness))
raw_bycc <- raw %>% group_by(CrossoverChance) %>% summarise(avg_Fitness = mean(Fitness))
raw_bylr <- raw %>% group_by(LearningRate) %>% summarise(avg_Fitness = mean(Fitness))

best_by_mutation <- raw[which(raw$MutationChance == 0.9),]
boruta_output <- Boruta(Fitness ~., data = best_by_mutation, doTrace = 2)
boruta_signif <- names(boruta_output$finalDecision[boruta_output$finalDecision %in% c("Confirmed", "Tentative")])
plot(boruta_output, cex.axis=.7, las=2, xlab="", main="Variable Importance")
raw_bycc2 <- best_by_mutation %>% group_by(CrossoverChance) %>% summarise(avg_Fitness = mean(Fitness))
raw_bylr2 <- best_by_mutation %>% group_by(LearningRate) %>% summarise(avg_Fitness = mean(Fitness))

best_by_mutation_and_lr <- raw[which(raw$MutationChance == 0.9 & raw$LearningRate == 1),]

stats <- raw %>% group_by(MutationChance,LearningRate) %>%
  summarise(mean_fitness = mean(Fitness))

lr_by_mutationChance <- ggplot(stats, aes(LearningRate))+
  geom_smooth(aes(y= mean_fitness, color=factor(MutationChance)))
plot(lr_by_mutationChance)

stats2 <- raw %>% group_by(MutationChance,CrossoverChance) %>%
  summarise(mean_fitness = mean(Fitness))

cc_by_mutationChance <- ggplot(stats2, aes(CrossoverChance))+
  geom_smooth(aes(y= mean_fitness, color=factor(MutationChance)))
plot(cc_by_mutationChance)
