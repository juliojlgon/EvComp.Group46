island_generational_plot <-function(data)
  {
  intra_epoch <- nrow(stats[which(stats$Epoch == 1 & stats$IslandIndex == 1),])
  last_generation <- max(stats$Generation)
  
  evo_plot <- ggplot(stats,
                     aes(x= factor(Generation),
                         y= max_fitness,
                         color= factor(IslandIndex)))+
    geom_point() +
    scale_x_discrete(breaks = seq(1, last_generation, intra_epoch))
  
  return(evo_plot)
}

island_mean_generational_plot <-function(data)
{
  intra_epoch <- nrow(stats[which(stats$Epoch == 1 & stats$IslandIndex == 1),])
  last_generation <- max(stats$Generation)
  
  evo_plot <- ggplot(stats,
                     aes(x= factor(Generation),
                         y= avg_fitness,
                         color= factor(IslandIndex)))+
    geom_point() +
    scale_x_discrete(breaks = seq(1, last_generation, intra_epoch))
  
  return(evo_plot)
}

island_growth_per_generation <- function(data){
  
  st <- data %>%
    group_by(Epoch, IslandIndex) %>%
    summarise(FitnessIncrease = max(max_fitness) - min(max_fitness))
  
  pl <- ggplot(st, aes(x= factor(Epoch), y= FitnessIncrease))+
    geom_bar(stat = "identity", aes(fill=factor(IslandIndex)), position = "dodge")
  
  return(pl)
}