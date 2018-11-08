heuristic00 <- read.csv(file="config_0_mean.csv", header=FALSE, sep=",")
heuristic01 <- read.csv(file="config_1_mean.csv", header=FALSE, sep=",")
heuristic03 <- read.csv(file="config_2_mean.csv", header=FALSE, sep=",")
heuristic05 <- read.csv(file="config_3_mean.csv", header=FALSE, sep=",")
heuristic07 <- read.csv(file="config_4_mean.csv", header=FALSE, sep=",")
heuristic09 <- read.csv(file="config_5_mean.csv", header=FALSE, sep=",")

iteration <- c(heuristic00[1]$V1)

jpeg('result_time_mean_iteration.jpg')
yrange <- range(heuristic00[2]$V2, 
    heuristic01[2]$V2, 
    heuristic03[2]$V2, 
    heuristic05[2]$V2, 
    heuristic07[2]$V2,  
    heuristic09[2]$V2)

xrange <- range(heuristic00[3]$V3, 
    heuristic01[3]$V3, 
    heuristic03[3]$V3, 
    heuristic05[3]$V3, 
    heuristic07[3]$V3,  
    heuristic09[3]$V3)

plot(iteration, heuristic00[2]$V2, type="l", ylim=yrange, col="blue", ann=FALSE)
lines(iteration, heuristic01[2]$V2, type="l", col="green")
lines(iteration, heuristic03[2]$V2, type="l", col="chocolate1")
lines(iteration, heuristic05[2]$V2, type="l", col="burlywood3")
lines(iteration, heuristic07[2]$V2, type="l", col="aquamarine4")
lines(iteration, heuristic09[2]$V2, type="l", col="lightblue2")

title(main='Gleiche Joblänge auf Maschine 1 Heuristik 50 Jobs', col.main="red", font.main=4)
title(xlab='Iteration')
title(ylab='durchschnittliche Fertigstellungszeit')
legend("topright", legend=c("0.0", "0.1", "0.3", "0.5", "0.7", "0.9"), 
    col=c("blue", "green", "chocolate1", "burlywood3", "aquamarine4", "lightblue2"),
    lty=1:1, cex=0.8)

jpeg('result_time_mean_calculationtime.jpg')
plot(heuristic00[3]$V3, heuristic00[2]$V2, type="l", ylim=yrange, xlim=xrange, col="blue", ann=FALSE)
lines(heuristic01[3]$V3, heuristic01[2]$V2, type="l", col="green")
lines(heuristic03[3]$V3, heuristic03[2]$V2, type="l", col="chocolate1")
lines(heuristic05[3]$V3, heuristic05[2]$V2, type="l", col="burlywood3")
lines(heuristic07[3]$V3, heuristic07[2]$V2, type="l", col="aquamarine4")
lines(heuristic09[3]$V3, heuristic09[2]$V2, type="l", col="lightblue2")

box()

title(main='Gleiche Joblänge auf Maschine 1 Heuristik 50 Jobs', col.main="red", font.main=4)
title(xlab='Laufzeit in s')
title(ylab='durchschnittliche Fertigstellungszeit')
legend("topright", legend=c("0.0", "0.1", "0.3", "0.5", "0.7", "0.9"), 
    col=c("blue", "green", "chocolate1", "burlywood3", "aquamarine4", "lightblue2"),
    lty=1:1, cex=0.8)