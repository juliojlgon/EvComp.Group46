setwd(dir = "~/UvA/EC/FinalProject/EvComp.Group46/stats/")
data1 <- read.csv("RandomMigration.csv")
data2 <- read.csv("RankedMigration.csv")
data3 <- read.csv("RingMigration.csv")

data1$MigrationPolicy <- "Random"
data2$MigrationPolicy <- "Ranked"
data3$MigrationPolicy <- "Ring"

all <- rbind(data1, data2, data3)

mc_density <- ggplot(all, aes(Score))+
  geom_density(aes(fill = MigrationPolicy), alpha = 0.6)
plot(mc_density)
